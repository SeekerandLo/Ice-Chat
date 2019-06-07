package com.liy.chat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FriendServiceTest {

    @Autowired
    FriendService friendService;

    @Test
    public void mutualAddFriend() {
        friendService.mutualAddFriend("5cf6871d12941a25508f4392", "5cf7e04eaeb1fc2018c78d21");
    }
}