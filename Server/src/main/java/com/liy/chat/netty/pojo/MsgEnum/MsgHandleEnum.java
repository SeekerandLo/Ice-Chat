package com.liy.chat.netty.pojo.MsgEnum;

/**
 * data: 2019/6/6 14:34
 **/

public enum MsgHandleEnum {

    PROCESSED(1, "已处理"),
    UNTREATED(2, "未处理");

    public Integer type;
    public String des;

    MsgHandleEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }


}
