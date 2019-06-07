package com.liy.chat.service;

import com.liy.chat.entity.RequestMessage;
import com.liy.chat.entity.User;
import com.liy.chat.entity.UserFriend;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import com.liy.chat.netty.pojo.MsgEnum.MsgTypeEnum;
import com.liy.chat.vo.UserVO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * data: 2019/6/5 10:28
 **/
@Service
public class FriendService {

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired
    AccountService accountService;

    public List<UserVO> searchFriend(String username, String me) {

        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + username + ".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("username").regex(pattern).and("_id").ne(me));
        List<User> users = mongoTemplate.find(query, User.class);

        List<UserVO> userVOS = new ArrayList<>();
        users.forEach(user -> {
            userVOS.add(accountService.packageUserVO(user));
        });

        return userVOS;
    }

    //TODO 添加好友  ，同意得话 删除以前得请求，在两个人得好友中添加上对方
    public String saveFriendRequest(ChatMsg chatMsg) {
        // String senderId, String receiverId, String reason
        RequestMessage requestMessage = new RequestMessage();

        requestMessage.setAction(MsgTypeEnum.FRIEND_REQUEST);
        requestMessage.setReason(chatMsg.getMsg());
        requestMessage.setSenderId(chatMsg.getSenderId());
        requestMessage.setReceiverId(chatMsg.getReceiverId());
        requestMessage.setMsgHandle(MsgHandleEnum.UNTREATED);

        mongoTemplate.insert(requestMessage, "requestMessage");
        return requestMessage.getId().toHexString();
    }

    public List<UserVO> getFriends(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        UserFriend userFriend = mongoTemplate.findOne(query, UserFriend.class);
        return userFriend.getFriends();

    }


    // 相互添加好友
    private void addFriend(String requestUserId, String receiverUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(requestUserId));
        UserFriend userFriend = mongoTemplate.findOne(query, UserFriend.class);
        if (userFriend == null) {
            // 创建一个
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUserId(requestUserId);
            List<UserVO> userVOS = new ArrayList<>();
            // TODO 判断是否存在 应该在查找的时候判断
            User receiver = mongoTemplate.findById(new ObjectId(receiverUserId), User.class);
            UserVO receiverVo = accountService.packageUserVO(receiver);
            userVOS.add(receiverVo);
            newUserFriend.setFriends(userVOS);

            mongoTemplate.insert(newUserFriend, "userFriend");
        } else {
            User receiver = mongoTemplate.findById(new ObjectId(receiverUserId), User.class);
            UserVO receiverVo = accountService.packageUserVO(receiver);
            // 使用 push 直接操作数组
            Update update = new Update().push("friends", receiverVo);
            mongoTemplate.upsert(query, update, UserFriend.class);
        }
    }

    // 互相加位好友，创建请求保存到数据库中，然后等别人同意 ，同意之后才能互相加为好友
    public void mutualAddFriend(String requestUserId, String receiverUserId) {
        addFriend(requestUserId, receiverUserId);
        addFriend(receiverUserId, requestUserId);
    }
}
