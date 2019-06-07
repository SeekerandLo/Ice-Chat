package com.liy.chat.service;

import com.liy.chat.entity.Message;
import com.liy.chat.netty.pojo.ChatMsg;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * data: 2019/6/6 9:19
 **/
@Service
public class MsgService {

    @Autowired
    MongoTemplate mongoTemplate;

    // 保存聊天记录
    public String saveMsgRecord(ChatMsg chatMsg) {
        Message message = new Message();
        try {
            BeanUtils.copyProperties(message, chatMsg);
            message.setSendTime(new Date());
            mongoTemplate.insert(message, "message");
            return message.getId().toHexString();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


}
