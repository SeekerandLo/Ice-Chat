package com.liy.chat.controller;

import com.liy.chat.service.FriendService;
import com.liy.chat.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * data: 2019/6/5 10:31
 **/
@Controller
@RequestMapping("/api/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @GetMapping("/search")
    public ResponseEntity<?> findTargetUser(@RequestParam String searchText, @RequestParam String me) {
        System.out.println(searchText + "   " + me);
        List<UserVO> targetUsers = friendService.searchFriend(searchText, me);
        return ResponseEntity.ok(targetUsers);
    }

    /**
     * TODO　添加好友　是能在线收到的，只要发送请求，就持久化，但是有状态位，标记是否处理
     * 没有处理的话就一直保留，每次页面初始化的时候会请求一次
     * TODO 不能添加自己
     * TODO　不能重复添加
     * TODO　用户不存在返回空
     */
    @GetMapping("add")
    public ResponseEntity<?> addFriend(@RequestParam String requestUserId, @RequestParam String receiveUserId) {


        return null;


    }

    @GetMapping("/list")
    public ResponseEntity<?> getFriends(@RequestParam String userId) {
        return ResponseEntity.ok(friendService.getFriends(userId));
    }


}
