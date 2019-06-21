package com.liy.chat.netty.pojo.msgenum;

/**
 * @author Liy @date 2019/6/6 14:34
 **/

public enum MsgHandleEnum {
    /**
     * 标明 消息已经处理
     */
    PROCESSED(1, "已处理"),

    /**
     * 标明 消息未处理
     */
    UNTREATED(2, "未处理");

    public Integer type;
    public String des;

    MsgHandleEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }


}
