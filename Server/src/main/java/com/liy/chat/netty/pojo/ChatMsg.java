package com.liy.chat.netty.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息内容
 * @author Liy
 * @date 2019/6/5 15:45
 **/
@Data
public class ChatMsg implements Serializable {
    private static final long serialVersionUID = 3873276727533351105L;

    private String msg;

    private String senderId;

    private String receiverId;

    private String msgId;

    private Date sendTime;
}
