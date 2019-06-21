package com.liy.chat.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Liy
 * @date 2019/6/4 15:11
 **/
@Component
public class WsServer {

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    private final static WsServer WS_SERVER = new WsServer();

    private EventLoopGroup boss;
    private EventLoopGroup sub;

    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;


    public WsServer(){
        boss = new NioEventLoopGroup();
        sub = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,sub).channel(NioServerSocketChannel.class).childHandler(new WsServerInitializer());
    }

    public void start(){
        future = serverBootstrap.bind(8088);
        logger.info("netty 启动成功");
    }

    public static WsServer getWsServer(){
        return WS_SERVER;
    }

}
