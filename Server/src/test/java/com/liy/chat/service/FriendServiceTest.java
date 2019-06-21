package com.liy.chat.service;

import com.liy.chat.netty.pojo.msgenum.RequestActionEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FriendServiceTest {

    @Autowired
    FriendService friendService;

    @Test
    public void mutualAddFriend() {
        friendService.mutualAddFriend("5cf6871d12941a25508f4392", "5cf7e04eaeb1fc2018c78d21");
    }

    @Test
    public void searchFriend() {
        System.out.println(friendService.searchFriend("ad", "5cf6871d12941a25508f4392"));
    }

    @Test
    public void changeState() {
        friendService.changeState("5cf7e04eaeb1fc2018c78d21", "5cf6871d12941a25508f4392");

    }

    @Test
    public void getUntreatedFriendRequest() {
        System.out.println(friendService.getUntreatedFriendRequest("5cf6871d12941a25508f4392").size());
    }

    @Test
    public void processRequestTest() {
        String senderId = "5cf6871d12941a25508f4392";
        String receiverId = "5cf7b7971cc0e41488e5cbbd";

        RequestActionEnum action = RequestActionEnum.AGREE;

        friendService.processRequest(senderId, receiverId, action.type);


    }
}