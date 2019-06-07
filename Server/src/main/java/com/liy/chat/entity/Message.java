package com.liy.chat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * data: 2019/6/4 17:47
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class Message extends BaseEntity{

    private String senderId;

    private String receiverId;

    private String msg;

    private Date sendTime;
}
