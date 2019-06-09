package com.liy.chat.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//            if (event.state() == IdleState.READER_IDLE) {
//            } else if (event.state() == IdleState.WRITER_IDLE) {
//            } else
            if (event.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                channel.close(); // 关闭，防止资源浪费
                logger.info("当前Channel数量为：" + ChatHandler.clients.size());
            }
        }

    }
}
