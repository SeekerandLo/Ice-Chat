package com.liy.chat.netty.pojo.MsgEnum;

/**
 * 请求操作
 * data: 2019/6/6 14:28
 **/

public enum RequestActionEnum {

    AGREE(1, "同意"),
    REFUSE(2, "拒绝"),
    IGNORE(3, "忽略");

    public Integer type;
    public String des;

    RequestActionEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }


}
