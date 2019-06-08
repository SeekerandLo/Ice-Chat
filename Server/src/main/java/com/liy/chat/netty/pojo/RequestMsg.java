package com.liy.chat.netty.pojo;

import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * data: 2019/6/7 21:12
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RequestMsg extends ChatMsg implements Serializable {
    private static final long serialVersionUID = 1066914562286977404L;

    private String senderName;

    private String dateTime;

    private MsgHandleEnum msgHandleEnum;

}
