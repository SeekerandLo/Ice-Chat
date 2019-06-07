package com.liy.chat.netty.websocket;

import com.liy.chat.netty.handler.ChatHandler;
import com.liy.chat.netty.handler.HeartBeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * data: 2019/6/4 15:15
 **/

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        // 编解码器
        channelPipeline.addLast(new HttpServerCodec());
        // 大数据流
        channelPipeline.addLast(new ChunkedWriteHandler());
        // httpMessage 聚合器，聚合成 FullHttpRequest 或 FullHttpResponse，常用
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
        // 以上为支持 Http 协议

        // 激活状态的Handler，如果客户端在4/6s内没有向服务器发送读写请求不处理，如果60s内没有发送心跳，则断开
        channelPipeline.addLast(new IdleStateHandler(20, 40, 60));

        // 增加心跳支持 start
        channelPipeline.addLast(new HeartBeatHandler());
        // 增加心跳支持 end

        // 支持WebSocket，指定路由
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义
        channelPipeline.addLast(new ChatHandler());


    }
}
