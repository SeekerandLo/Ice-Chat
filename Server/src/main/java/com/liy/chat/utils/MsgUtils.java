package com.liy.chat.utils;

import com.alibaba.fastjson.JSON;
import com.liy.chat.entity.Message;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.DataContent;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

/**
 * data: 2019/6/5 19:18
 **/

public class MsgUtils {

    // 转换为传输结构
    public static DataContent toDataContent(String jsonString) {
        return JSON.parseObject(jsonString, DataContent.class);
    }

    public static ChatMsg toChatMsg(Message message) {
        ChatMsg chatMsg = new ChatMsg();
        try {
            BeanUtils.copyProperties(chatMsg, message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return chatMsg;
    }

}
