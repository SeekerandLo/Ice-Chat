package com.liy.chat.service;

import com.liy.chat.netty.pojo.ChatMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgServiceTest {

    @Autowired
    MsgService msgService;

    @Test
    public void saveMsgRecord() {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setMsg("你好啊");
        chatMsg.setReceiverId("dasdsasa");
        chatMsg.setSenderId("eqweqwqw");
        msgService.saveMsgRecord(chatMsg);
    }

    @Test
    public void getHistoryMsg(){
        msgService.getHistoryMsg("5cf6871d12941a25508f4392","5cf7b7971cc0e41488e5cbbd");
    }

}