package com.liy.chat.controller;

import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消息
 * data: 2019/6/14 20:52
 **/
@Controller
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MsgService msgService;

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<?> getChatHistory(@RequestParam String userId, @RequestParam String friendId) {
        List<ChatMsg> chatMsgs = msgService.getHistoryMsg(userId, friendId);

        return ResponseEntity.ok(chatMsgs);
    }

}
