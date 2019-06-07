package com.liy.chat.netty.websocket;

import com.liy.chat.netty.handler.ChatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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

        // 支持WebSocket，指定路由
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义
        channelPipeline.addLast(new ChatHandler());


    }
}
