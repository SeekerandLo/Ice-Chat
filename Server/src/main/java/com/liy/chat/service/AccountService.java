package com.liy.chat.service;

import com.liy.chat.dto.AccountDTO;
import com.liy.chat.exception.RepeatUserException;
import com.liy.chat.vo.UserVO;
import com.liy.chat.entity.User;
import com.liy.chat.exception.NoUserException;
import com.liy.chat.exception.PasswordError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * data: 2019/6/4 20:52
 **/
@Service
public class AccountService {

    @Autowired
    MongoTemplate mongoTemplate;

    // 注册
    // TODO 密码加密
    public void register(AccountDTO accountDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(accountDTO.getUsername()));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            User newUser = new User();
            newUser.setUsername(accountDTO.getUsername());
            newUser.setPassword(accountDTO.getPassword());
            mongoTemplate.insert(newUser, "user");
        } else {
            throw new RepeatUserException();
        }

    }

    // 登录
    public UserVO loginService(AccountDTO accountDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(accountDTO.getUsername()));
        User user = Optional.ofNullable(mongoTemplate.findOne(query, User.class)).orElseThrow(NoUserException::new);
        String password = user.getPassword();

        if (password.equals(user.getPassword())) {
            return packageUserVO(user);
        } else {
            throw new PasswordError();
        }
    }

    public UserVO packageUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setUsername(user.getUsername());
        userVO.setUserId(user.getId().toHexString());
        return userVO;
    }

    public UserVO packageUserVO(AccountDTO accountDTO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(accountDTO.getUsername());
        return userVO;
    }


}
