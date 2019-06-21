package com.liy.chat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.Date;

/**
 * @author Liy
 * @date 2019/6/4 17:47
 * TODO Message 可以和 chatMsg 合并的
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class Message extends BaseEntity implements Comparable<Message> {

    private String senderId;

    private String receiverId;

    private String msg;

    private Date sendTime;


    @Override
    public int compareTo(Message o2) {
        if (this.getSendTime().before(o2.getSendTime())) {
            return -1;
        } else if (this.getSendTime().after(o2.getSendTime())) {
            return 1;
        } else {
            return 0;
        }
    }
}
