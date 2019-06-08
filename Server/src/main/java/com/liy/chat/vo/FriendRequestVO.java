package com.liy.chat.vo;

import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 展示好友请求
 * TODO 将RequestMsg分清楚
 * data: 2019/6/8 15:42
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendRequestVO extends ChatMsg implements Serializable {
    private static final long serialVersionUID = 4146580530200515553L;

    private String senderName;

    private String dateTime;

    private MsgHandleEnum msgHandleEnum;
}

