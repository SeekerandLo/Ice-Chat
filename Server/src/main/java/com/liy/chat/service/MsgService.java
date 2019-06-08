package com.liy.chat.service;

import com.liy.chat.entity.Message;
import com.liy.chat.entity.RequestMessage;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
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

    // 保存聊天记录，单维护一个集合，以接收者和发送者为key保存记录的集合
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
        requestMsg.setMsgHandleEnum(MsgHandleEnum.UNTREATED);
        String username = accountService.getUsername(chatMsg.getSenderId());
        requestMsg.setSenderName(username);
        requestMsg.setDateTime(DateUtils.dateToString(new Date()));

        return requestMsg;
    }

    public RequestMsg packageRequestMsg(RequestMessage requestMessage) {
        RequestMsg requestMsg = new RequestMsg();
        try {
            BeanUtils.copyProperties(requestMsg, requestMessage);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String username = accountService.getUsername(requestMessage.getSenderId());
        requestMsg.setSenderName(username);
        requestMsg.setDateTime(DateUtils.dateToString(requestMessage.getSendTime()));


        return requestMsg;
    }

}
