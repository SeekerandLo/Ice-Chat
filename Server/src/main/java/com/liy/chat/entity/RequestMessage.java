package com.liy.chat.entity;

import com.liy.chat.netty.pojo.msgenum.MsgHandleEnum;
import com.liy.chat.netty.pojo.msgenum.MsgTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Liy
 * @date 2019/6/6 13:35
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class RequestMessage extends BaseEntity {

    private MsgTypeEnum action;

    private String senderId;

    private String receiverId;

    private String msg;

    private MsgHandleEnum msgHandle;

    private Date sendTime;
}
