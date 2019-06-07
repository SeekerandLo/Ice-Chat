package com.liy.chat.netty.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * data: 2019/6/5 15:44
 **/
@Data
public class DataContent implements Serializable {
    private static final long serialVersionUID = 2296536608584650171L;

    private Integer action; // 行为

    private ChatMsg chatMsg;

    private String extend; // 扩展

}
