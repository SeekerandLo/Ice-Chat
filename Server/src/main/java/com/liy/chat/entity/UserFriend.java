package com.liy.chat.entity;

import com.liy.chat.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * data: 2019/6/4 17:45
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class UserFriend extends BaseEntity{

    private String userId;

    private List<UserVO> friends;
}
