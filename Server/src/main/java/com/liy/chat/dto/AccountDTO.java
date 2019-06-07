package com.liy.chat.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * data: 2019/6/4 19:51
 **/
@Data
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = -1900284792313548034L;

    private String username;

    private String password;

}
