package com.liy.chat.netty.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端 传给后台 的消息体，包括聊天消息体(或请求内容)及行为
 * data: 2019/6/5 15:44
 **/
@Data
public class DataContent implements Serializable {
    private static final long serialVersionUID = 2296536608584650171L;

    private Integer action; // 行为

    private ChatMsg chatMsg;

    private String extend; // 扩展

}
