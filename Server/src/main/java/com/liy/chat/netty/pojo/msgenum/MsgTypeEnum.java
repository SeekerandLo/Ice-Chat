package com.liy.chat.netty.pojo.msgenum;

/**
 * 消息种类
 */
public enum MsgTypeEnum {

    /**
     * 消息种类-第一次连接
     */
    CONNECT(1, "第一次连接"),

    /**
     * 消息种类-聊天消息
     */
    CHAT(2, "聊天消息"),

    /**
     * 消息种类-消息签收
     */
    SIGNED(3, "消息签收"),

    /**
     * 消息种类-保持心跳
     */
    KEEPALIVE(4, "保持心跳"),

    /**
     * 消息种类-好友请求
     */
    FRIEND_REQUEST(5,"好友请求");


    public final Integer type;
    public final String des;

    MsgTypeEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }
}
