package com.liy.chat.vo;

import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import com.liy.chat.netty.pojo.MsgEnum.MsgTypeEnum;
import com.liy.chat.netty.pojo.MsgEnum.RequestActionEnum;
import lombok.Data;

/**
 * data: 2019/6/8 13:17
 **/
@Data
public class RequestResponseVO {

    private String msgId;

    private MsgHandleEnum msgHandleEnum;

    private RequestActionEnum requestActionEnum;

}
