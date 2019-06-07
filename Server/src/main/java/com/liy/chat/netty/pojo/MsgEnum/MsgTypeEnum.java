package com.liy.chat.netty.pojo.MsgEnum;

/**
 * 消息种类
 */
public enum MsgTypeEnum {

    CONNECT(1, "第一次连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "保持心跳"),
    FRIEND_REQUEST(5,"好友请求");


    public final Integer type;
    public final String des;

    MsgTypeEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }
}
