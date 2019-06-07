package com.liy.chat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.liy.chat.netty.pojo.ChannelMap;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.DataContent;
import com.liy.chat.netty.pojo.MsgEnum.MsgTypeEnum;
import com.liy.chat.service.FriendService;
import com.liy.chat.service.MsgService;
import com.liy.chat.utils.MsgUtils;
import com.liy.chat.utils.SpringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.context.ApplicationContext;

/**
 * 处理消息的handler
 * data: 2019/6/4 15:28
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 管理所有客户端
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // TODO 当一个连接断开的时候，应该将它们从map中移除
    // TODO　代码抽离
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

            ChannelMap.put(senderId, receiverId, currentChannel);

        } else if (action.equals(MsgTypeEnum.CHAT.type)) {
            ApplicationContext applicationContext = SpringUtils.getApplicationContext();
            MsgService msgService = (MsgService) applicationContext.getBean("msgService");
            // TODO 要不要把记录存到以双方为主的数据库中
            String msgId = msgService.saveMsgRecord(dataContent.getChatMsg());
            chatMsg.setMsgId(msgId);

            sendMsg(chatMsg);

            // 将记录保存到数据库，然后转发
        } else if (action.equals(MsgTypeEnum.FRIEND_REQUEST.type)) {
            ApplicationContext applicationContext = SpringUtils.getApplicationContext();
            FriendService friendService = (FriendService) applicationContext.getBean("friendService");
            String msgId = friendService.sendFriendRequest(dataContent.getChatMsg());
            chatMsg.setMsgId(msgId);
            // 发给接收者
            sendMsg(chatMsg);

        } else if (action.equals(MsgTypeEnum.SIGNED.type)) {
            // 签收消息
        } else if (action.equals(MsgTypeEnum.KEEPALIVE.type)) {
            // 保持连接
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
        clients.remove(ctx.channel());
        System.out.println("关闭" + ctx.channel().id().asLongText());
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


    private void sendMsg(ChatMsg chatMsg) {
        Channel receiverChannel = ChannelMap.getChannel(chatMsg.getSenderId(), chatMsg.getReceiverId());
        if (receiverChannel == null) {
            // 用户离线，保存，
        } else {
            Channel findChannel = clients.find(receiverChannel.id());
            if (findChannel != null) {
                // 用户在线,发送消息
                System.out.println(JSONObject.toJSONString(chatMsg));
                System.out.println(chatMsg.toString());

                receiverChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatMsg)));
            } else {
                // 用户离线，保存
            }
        }
    }
}
