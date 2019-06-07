package com.liy.chat.netty.pojo;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * data: 2019/6/5 17:24
 * 存放 Channel 与 用户Id的关系
 * <p>
 * TODO 问题出现了，当两个好友 同时打开对你的聊天窗口时，发送的消息全转到了 一个聊天窗口中
 * <p>
 * 前端问题，在点击用户 进行聊天时，虽然打开了两个窗口，但是她们的socket出现了问题，当切换窗口后 socket 就断了。再切换回去会重新连接
 * 已解决：使用 watch 监听 路由变化
 * <p>
 * TODO 想实现单对单的聊天 要好好思考 key 如何处理
 **/

public class ChannelMap {

    private static HashMap<String, Channel> channelMap = new HashMap<>();

    // 封装key
    public static void put(String senderId, String receiverId, Channel channel) {
        String key = senderId + "-" + receiverId;
        put(key, channel);
    }

    private static void put(String key, Channel channel) {
        channelMap.put(key, channel);
    }

    private static Channel get(String key) {
        return channelMap.get(key);
    }

    public static Channel getChannel(String senderId, String receiverId) {
        String key = receiverId + "-" + senderId;
        return get(key);
    }


}
