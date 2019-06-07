package com.liy.chat.netty.pojo.MsgEnum;

public enum ConnectionEnum {

    CHAT(1, "聊天"),
    RECEIVE_REQUEST(2, "接收其他请求");

    public Integer type;
    public String des;

    ConnectionEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }
}
