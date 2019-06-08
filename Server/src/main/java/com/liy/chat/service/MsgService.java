package com.liy.chat.service;

import com.liy.chat.entity.Message;
import com.liy.chat.entity.RequestMessage;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import com.liy.chat.utils.DateUtils;
import com.liy.chat.vo.FriendRequestVO;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * data: 2019/6/6 9:19
 * 处理消息记录
 **/
@Service
public class MsgService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AccountService accountService;

    // 保存聊天记录一条一个
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


    public FriendRequestVO packageFriendRequestVO(RequestMessage requestMessage) {
        FriendRequestVO requestMsg = new FriendRequestVO();
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

    // 封装成展示的VO，包含发送者姓名
    public FriendRequestVO packageFriendRequestVO(ChatMsg chatMsg) {
        FriendRequestVO requestMsg = new FriendRequestVO();

        try {
            BeanUtils.copyProperties(requestMsg, chatMsg);
            requestMsg.setMsgHandleEnum(MsgHandleEnum.UNTREATED);
            String username = accountService.getUsername(chatMsg.getSenderId());
            requestMsg.setSenderName(username);
            requestMsg.setDateTime(DateUtils.dateToString(new Date()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return requestMsg;
    }

}
