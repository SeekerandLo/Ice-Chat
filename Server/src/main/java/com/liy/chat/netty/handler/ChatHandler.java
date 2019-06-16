package com.liy.chat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.liy.chat.netty.pojo.ChannelManager;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.DataContent;
import com.liy.chat.netty.pojo.MsgEnum.ConnectionEnum;
import com.liy.chat.netty.pojo.MsgEnum.MsgTypeEnum;
import com.liy.chat.service.FriendService;
import com.liy.chat.service.MsgService;
import com.liy.chat.utils.MsgUtils;
import com.liy.chat.utils.SpringUtils;
import com.liy.chat.vo.FriendRequestVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 处理消息的handler
 * data: 2019/6/4 15:28
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private Logger logger = LoggerFactory.getLogger(Logger.class);
    // 管理所有客户端
    static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // TODO 当一个连接断开的时候，是否应该将它们从map中移除，还是保留在Map中，如不移除再创建的时候是覆盖了
    // TODO 代码抽离
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();

        // 封装数据 TODO 判断
        DataContent dataContent = MsgUtils.toDataContent(content);
        Integer action = dataContent.getAction();
        ChatMsg chatMsg = dataContent.getChatMsg();

        if (action.equals(MsgTypeEnum.CONNECT.type)) {
            //当 webSocket 第一次初始化channel时，把用户Id和它关联
            String senderId = chatMsg.getSenderId();
            String receiverId = chatMsg.getReceiverId();
            // 判断chatMsg的 receiverId 是否是 server
            if (chatMsg.getReceiverId().equals("server")) {
                ChannelManager.put(ConnectionEnum.RECEIVE_REQUEST, senderId, receiverId, currentChannel);
            } else {
                ChannelManager.put(ConnectionEnum.CHAT, senderId, receiverId, currentChannel);
            }
            ChannelManager.out();
        } else if (action.equals(MsgTypeEnum.CHAT.type)) {
            ApplicationContext applicationContext = SpringUtils.getApplicationContext();
            MsgService msgService = (MsgService) applicationContext.getBean("msgService");
            // TODO 要不要把记录存到以双方为主的数据库中
            // 保存到聊天记录
            String msgId = msgService.saveMsgRecord(dataContent.getChatMsg());
            chatMsg.setMsgId(msgId);
            sendMsg(chatMsg, ConnectionEnum.CHAT);
        } else if (action.equals(MsgTypeEnum.FRIEND_REQUEST.type)) {
            ApplicationContext applicationContext = SpringUtils.getApplicationContext();
            FriendService friendService = (FriendService) applicationContext.getBean("friendService");
            // 保存到请求记录
            String msgId = friendService.saveFriendRequest(dataContent.getChatMsg());
            chatMsg.setMsgId(msgId);

            // 首先保存到 数据库中，谁请求的 ，谁接收到的，然后通过 netty 转发，如果在线，通过接收请求的的 channel 发送给目标用户
            sendMsg(chatMsg, ConnectionEnum.RECEIVE_REQUEST);

        } else if (action.equals(MsgTypeEnum.SIGNED.type)) {
            // 签收消息
        } else if (action.equals(MsgTypeEnum.KEEPALIVE.type)) {
            // 保持连接
            logger.info("Bon Bon：" + ctx.channel().id().asShortText());


        }


    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取用户所在的channel，添加到 channelGroup 管理
        Channel channel = ctx.channel();
        clients.add(channel);
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 离开,当触发 移除时  会自动移除
        ChannelManager.removeInvalidChannel(ctx.channel());
        clients.remove(ctx.channel());
        logger.info("关闭channel: " + ctx.channel().id().asLongText());
        ChannelManager.out();
        super.handlerRemoved(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常后关闭连接
        ctx.channel().close();
        clients.remove(ctx.channel());
        super.exceptionCaught(ctx, cause);
    }


    // TODO 问题：如果出现多种聊天类型就需要加很多判断同时增加 send（...）Msg 方法
    private void sendMsg(ChatMsg chatMsg, ConnectionEnum connectionEnum) {
        if (connectionEnum.equals(ConnectionEnum.CHAT)) {
            Channel receiverChannel = ChannelManager.getChannel(ConnectionEnum.CHAT, chatMsg.getSenderId(), chatMsg.getReceiverId());
            sendChatMsg(receiverChannel, chatMsg);
        } else if (connectionEnum.equals(ConnectionEnum.RECEIVE_REQUEST)) {
            Channel receiverChannel = ChannelManager.getChannel(ConnectionEnum.RECEIVE_REQUEST, "server", chatMsg.getReceiverId());
            sendFriendRequestMsg(receiverChannel, chatMsg);
        }
    }


    // 发送聊天消息
    private void sendChatMsg(Channel receiverChannel, ChatMsg chatMsg) {
        if (receiverChannel == null) {
            // 用户离线，保存，
        } else {
            Channel findChannel = clients.find(receiverChannel.id());
            if (findChannel != null) {
                // 用户在线,发送消息
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatMsg)));
            } else {
                // 用户离线，保存
            }
        }
    }

    // 发送好友请求
    private void sendFriendRequestMsg(Channel receiverChannel, ChatMsg chatMsg) {
        ApplicationContext applicationContext = SpringUtils.getApplicationContext();
        MsgService msgService = (MsgService) applicationContext.getBean("msgService");

        FriendRequestVO friendRequestVO = msgService.packageFriendRequestVO(chatMsg);
        if (receiverChannel == null) {
            // 用户离线，保存，
        } else {
            Channel findChannel = clients.find(receiverChannel.id());
            if (findChannel != null) {
                // 用户在线,发送消息
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(friendRequestVO)));
            } else {
                // 用户离线，保存
            }
        }

    }
}
