package com.liy.chat.netty.pojo;

import com.liy.chat.netty.pojo.MsgEnum.ConnectionEnum;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * data: 2019/6/5 17:24
 * 存放 Channel 与 用户Id的关系
 * <p>
 * <p>
 * 前端问题，在点击用户 进行聊天时，虽然打开了两个窗口，但是她们的socket出现了问题，当切换窗口后 socket 就断了。再切换回去会重新连接
 * 已解决：使用 watch 监听 路由变化
 * <p>
 **/

public class ChannelMap {

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    private static HashMap<String, Channel> channelMap = new HashMap<>();

    // 封装key
    public static void put(ConnectionEnum type, String senderId, String receiverId, Channel channel) {
        String key = type + "-" + senderId + "-" + receiverId;
        put(key, channel);
    }

    private static void put(String key, Channel channel) {
        channelMap.put(key, channel);
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

}
