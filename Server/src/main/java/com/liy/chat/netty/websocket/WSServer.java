package com.liy.chat.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

/**
 * data: 2019/6/4 15:11
 **/
@Component
public class WSServer {

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    private final static WSServer wsServer = new WSServer();

    private EventLoopGroup boss;
    private EventLoopGroup sub;

    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;


    public WSServer(){
        boss = new NioEventLoopGroup();
        sub = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,sub).channel(NioServerSocketChannel.class).childHandler(new WSServerInitializer());
    }

    public void start(){
        future = serverBootstrap.bind(8088);
        logger.info("netty 启动成功");
    }

    public static WSServer getWsServer(){
        return wsServer;
    }

}
