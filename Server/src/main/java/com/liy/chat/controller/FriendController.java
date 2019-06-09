package com.liy.chat.controller;

import com.liy.chat.service.FriendService;
import com.liy.chat.vo.FriendRequestVO;
import com.liy.chat.vo.RequestResponseVO;
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
        List<UserVO> targetUsers = friendService.searchFriend(searchText, me);
        return ResponseEntity.ok(targetUsers);
    }

    /**
     * 没有处理的话就一直保留，每次页面初始化的时候会请求一次
     * TODO　不能重复添加
     */
    @GetMapping("/action")
    public ResponseEntity<?> processRequest(@RequestParam String senderId, @RequestParam String receiverId, @RequestParam Integer action) {
        RequestResponseVO responseVO = friendService.processRequest(senderId, receiverId, action);
        return ResponseEntity.ok(responseVO);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFriends(@RequestParam String userId) {
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    @GetMapping("/untreated")
    public ResponseEntity<?> getUntreatedRequest(@RequestParam String userId) {
        List<FriendRequestVO> requestMessages = friendService.getUntreatedFriendRequest(userId);
        return ResponseEntity.ok(requestMessages);
    }


}
