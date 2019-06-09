package com.liy.chat.netty.pojo;

import com.liy.chat.netty.pojo.MsgEnum.ConnectionEnum;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * data: 2019/6/5 17:24
 * 存放Channel与用户Id的关系
 * 目前有两类
 * 接收请求：Key: RECEIVE_REQUEST-5cf7e04eaeb1fc2018c78d21-server
 * 聊天： Key: CHAT-5cf7e04eaeb1fc2018c78d21-5cf6871d12941a25508f4392
 **/

public class ChannelManager {


    private static HashMap<String, Channel> channelMap = new HashMap<>();

    // 存放channel 与 key的关系，移除的时候使用
    private static HashMap<Channel, String> reversalChannelMap = new HashMap<>();

    // 封装key
    public static void put(ConnectionEnum type, String senderId, String receiverId, Channel channel) {
        String key = type + "-" + senderId + "-" + receiverId;
        put(key, channel);
    }

    private static void put(String key, Channel channel) {
        channelMap.put(key, channel);
        reversalChannelMap.put(channel, key);
    }

    private static Channel get(String key) {
        return channelMap.get(key);
    }

    public static Channel getChannel(ConnectionEnum type, String senderId, String receiverId) {
        String key = type + "-" + receiverId + "-" + senderId;
        return get(key);
    }

    // 输出
    public static void out() {
        System.out.println("当前ChannelMap中的数量：" + channelMap.size());
        for (Map.Entry<String, Channel> channelEntry : channelMap.entrySet()) {
            System.out.println(" channel Key: " + channelEntry.getKey() + " channel Value: " + channelEntry.getValue());
        }
    }

    public static void removeInvalidChannel(Channel channel) {
        String key = reversalChannelMap.get(channel);
        channelMap.remove(key);
        reversalChannelMap.remove(channel);
    }

}
