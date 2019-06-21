package com.liy.chat.util;

import com.alibaba.fastjson.JSON;
import com.liy.chat.entity.Message;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.DataContent;
import org.springframework.beans.BeanUtils;

/**
 * @author Liy
 * @date 2019/6/5 19:18
 **/

public class MsgUtils {

    // 转换为传输结构
    public static DataContent toDataContent(String jsonString) {
        return JSON.parseObject(jsonString, DataContent.class);
    }

    public static ChatMsg toChatMsg(Message message) {
        ChatMsg chatMsg = new ChatMsg();

        BeanUtils.copyProperties(message, chatMsg);

        return chatMsg;
    }

}
