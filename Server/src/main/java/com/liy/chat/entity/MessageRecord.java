package com.liy.chat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Liy
 * @date 2019/6/4 17:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class MessageRecord extends BaseEntity{

    private String senderId;

    private String receiverId;

    private List<String> messageIds;

}
