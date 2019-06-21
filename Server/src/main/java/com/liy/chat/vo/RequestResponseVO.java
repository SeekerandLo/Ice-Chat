package com.liy.chat.vo;

import com.liy.chat.netty.pojo.msgenum.MsgHandleEnum;
import com.liy.chat.netty.pojo.msgenum.RequestActionEnum;
import lombok.Data;

/**
 * @author Liy
 * @date 2019/6/8 13:17
 * 对好友请求做的响应，通过msgId去删除vuex中的消息
 **/
@Data
public class RequestResponseVO {

    private String msgId;

    private MsgHandleEnum msgHandleEnum;

    private RequestActionEnum requestActionEnum;

}
