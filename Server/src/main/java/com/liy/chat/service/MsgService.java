package com.liy.chat.service;

import com.liy.chat.entity.Message;
import com.liy.chat.entity.RequestMessage;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.msgenum.MsgHandleEnum;
import com.liy.chat.util.DateUtils;
import com.liy.chat.util.MsgUtils;
import com.liy.chat.vo.FriendRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Liy
 * @date 2019/6/6 9:19
 * 处理消息记录
 **/
@Service
public class MsgService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AccountService accountService;

    /**
     * 保存聊天记录一条一个
     *
     * @param chatMsg 消息
     * @return msg.id
     */
    public String saveMsgRecord(ChatMsg chatMsg) {
        Message message = new Message();
        BeanUtils.copyProperties(chatMsg, message);
        message.setSendTime(new Date());
        mongoTemplate.insert(message, "message");
        return message.getId().toHexString();
    }


    FriendRequestVO packageFriendRequestVO(RequestMessage requestMessage) {
        FriendRequestVO requestMsg = new FriendRequestVO();

        BeanUtils.copyProperties(requestMessage, requestMsg);

        String username = accountService.getUsername(requestMessage.getSenderId());
        requestMsg.setSenderName(username);
        requestMsg.setDateTime(DateUtils.dateToString(requestMessage.getSendTime()));
        return requestMsg;
    }

    /**
     * 封装成展示的VO，包含发送者姓名
     * @param chatMsg 消息
     * @return VO
     */
    public FriendRequestVO packageFriendRequestVO(ChatMsg chatMsg) {
        FriendRequestVO requestMsg = new FriendRequestVO();

        BeanUtils.copyProperties(chatMsg, requestMsg);

        requestMsg.setMsgHandleEnum(MsgHandleEnum.UNTREATED);
        String username = accountService.getUsername(chatMsg.getSenderId());
        requestMsg.setSenderName(username);
        requestMsg.setDateTime(DateUtils.dateToString(new Date()));


        return requestMsg;
    }

    public List<ChatMsg> getHistoryMsg(String userId, String friendId) {
        List<ChatMsg> chatMessages = new ArrayList<>();
        Query msgQuery = new Query();

        Criteria userReceiveCriteria = Criteria.where("senderId").is(friendId).and("receiverId").is(userId);
        Criteria userSendCriteria = Criteria.where("senderId").is(userId).and("receiverId").is(friendId);
        // 合并条件
        Criteria arrangementCriteria = new Criteria().orOperator(userSendCriteria, userReceiveCriteria);

        Sort sort = Sort.by(Sort.Direction.DESC, "sendTime");

        msgQuery.addCriteria(arrangementCriteria).with(sort).limit(20);
        List<Message> historyMessages = mongoTemplate.find(msgQuery, Message.class);

        Collections.sort(historyMessages);

        historyMessages.forEach(message -> {
            chatMessages.add(MsgUtils.toChatMsg(message));
        });

        return chatMessages;
    }


}
