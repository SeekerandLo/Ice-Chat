package com.liy.chat.netty.pojo.msgenum;

/**
 * 请求操作
 * @author Liy @date 2019/6/6 14:28
 **/

public enum RequestActionEnum {
    /**
     * 请求处理操作
     */
    AGREE(1, "同意"),

    /**
     * 请求处理操作
     */
    REFUSE(2, "拒绝"),

    /**
     * 请求处理操作
     */
    IGNORE(3, "忽略");

    public Integer type;
    public String des;

    RequestActionEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }

    public Integer getType() {
        return type;
    }


}
