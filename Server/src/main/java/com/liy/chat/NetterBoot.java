package com.liy.chat;

import com.liy.chat.netty.websocket.WsServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author liy
 * @date 2019/6/4 17:59
 **/
@Component
public class NetterBoot implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            WsServer.getWsServer().start();
        }
    }
}
