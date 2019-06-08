package com.liy.chat.controller;

import com.liy.chat.dto.AccountDTO;
import com.liy.chat.exception.RepeatUserException;
import com.liy.chat.vo.UserVO;
import com.liy.chat.exception.NoUserException;
import com.liy.chat.exception.PasswordError;
import com.liy.chat.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * data: 2019/6/4 19:50
 **/
@RequestMapping("/api/account")
@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountDTO accountDTO) {
        UserVO userVO = null;
        try {
            userVO = accountService.loginService(accountDTO);
            userVO.setToken("Ice-Chat");
        } catch (NoUserException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("不存在的用户");
        } catch (PasswordError e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("密码错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(userVO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.register(accountDTO);
            return ResponseEntity.ok(accountDTO);
        } catch (RepeatUserException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
