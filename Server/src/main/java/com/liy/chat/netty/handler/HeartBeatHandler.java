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
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 心跳的handler
 * data: 2019/6/4 15:28
 **/
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent ,用于触发用户事件，包含读空闲，写空闲，读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲请求");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("写空闲请求");
            } else if (event.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                channel.close(); // 关闭，防止资源浪费
                logger.info("当前Channel数量为：" + ChatHandler.clients.size());
            }
        }

    }
}
