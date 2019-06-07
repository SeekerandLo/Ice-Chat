package com.liy.chat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * data: 2019/6/4 17:37
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class User extends BaseEntity{

    private String username;

    private String password;

}
