package com.liy.chat.service;

import com.liy.chat.entity.Message;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.RequestMsg;
import com.liy.chat.utils.DateUtils;
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

    @Autowired
    AccountService accountService;

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

    // 封装成加好友记录 TODO 接收异常
    public RequestMsg packageRequestMsg(ChatMsg chatMsg) throws InvocationTargetException, IllegalAccessException {
        RequestMsg requestMsg = new RequestMsg();
        BeanUtils.copyProperties(requestMsg, chatMsg);

        String username = accountService.getUsername(chatMsg.getSenderId());
        requestMsg.setSenderName(username);
        requestMsg.setDateTime(DateUtils.dateToString(new Date()));

        return requestMsg;
    }


}
