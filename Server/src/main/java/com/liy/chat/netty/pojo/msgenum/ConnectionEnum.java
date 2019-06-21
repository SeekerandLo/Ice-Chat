package com.liy.chat.netty.pojo.msgenum;

public enum ConnectionEnum {

    /**
     * 标明连接的类型-聊天
     */
    CHAT(1, "聊天"),

    /**
     * 标明连接的类型-接收其他请求
     */
    RECEIVE_REQUEST(2, "接收其他请求");

    public Integer type;
    public String des;

    ConnectionEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }
}
