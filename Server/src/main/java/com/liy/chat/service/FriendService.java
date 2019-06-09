package com.liy.chat.service;

import com.liy.chat.entity.RequestMessage;
import com.liy.chat.entity.User;
import com.liy.chat.entity.UserFriend;
import com.liy.chat.netty.pojo.ChatMsg;
import com.liy.chat.netty.pojo.MsgEnum.MsgHandleEnum;
import com.liy.chat.netty.pojo.MsgEnum.MsgTypeEnum;
import com.liy.chat.netty.pojo.MsgEnum.RequestActionEnum;
import com.liy.chat.vo.FriendRequestVO;
import com.liy.chat.vo.RequestResponseVO;
import com.liy.chat.vo.UserVO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * data: 2019/6/5 10:28
 **/
@Service
public class FriendService {

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired
    AccountService accountService;


    @Autowired
    MsgService msgService;

    public List<UserVO> searchFriend(String username, String me) {
        Query myQuery = new Query().addCriteria(Criteria.where("userId").is(me));
        UserFriend currentUserFriend = mongoTemplate.findOne(myQuery, UserFriend.class);
        List<UserVO> userVOS = new ArrayList<>();

        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + username + ".*$", Pattern.CASE_INSENSITIVE);

        if (currentUserFriend == null) {
            query.addCriteria(Criteria.where("username").regex(pattern));
        } else {
            List<UserVO> friends = currentUserFriend.getFriends();
            // 把好友转换为map，但是不包含自己
            Map<String, String> userFriendMap = friends.stream().collect(Collectors.toMap(UserVO::getUserId, UserVO::getUsername));
            userFriendMap.put(me, null);
            query.addCriteria(Criteria.where("username").regex(pattern).and("_id").nin(userFriendMap.keySet()));
        }

        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(user -> {
            userVOS.add(accountService.packageUserVO(user));
        });

        return userVOS;
    }


    public RequestResponseVO processRequest(String senderId, String receiverId, Integer action) {
        if (action.equals(RequestActionEnum.AGREE.type)) {
            // 同意->把请求状态改为已处理，且相互添加好友
            // TODO 考虑两个用户重复的发请求
            String msgId = changeState(senderId, receiverId);
            mutualAddFriend(senderId, receiverId);

            RequestResponseVO responseVO = new RequestResponseVO();
            responseVO.setMsgHandleEnum(MsgHandleEnum.PROCESSED);
            responseVO.setRequestActionEnum(RequestActionEnum.AGREE);
            responseVO.setMsgId(msgId);

            return responseVO;

        } else if (action.equals(RequestActionEnum.REFUSE.type)) {
            // 拒绝
            String msgId = changeState(senderId, receiverId);


            RequestResponseVO responseVO = new RequestResponseVO();
            responseVO.setMsgHandleEnum(MsgHandleEnum.PROCESSED);
            responseVO.setRequestActionEnum(RequestActionEnum.REFUSE);
            responseVO.setMsgId(msgId);

            return responseVO;

        } else if (action.equals(RequestActionEnum.IGNORE.type)) {
            // 忽略
            String msgId = changeState(senderId, receiverId);


            RequestResponseVO responseVO = new RequestResponseVO();
            responseVO.setMsgHandleEnum(MsgHandleEnum.PROCESSED);
            responseVO.setRequestActionEnum(RequestActionEnum.IGNORE);
            responseVO.setMsgId(msgId);

            return responseVO;

        }
        return null;

    }

    // 修改状态
    public String changeState(String senderId, String receiverId) {
        Query query = new Query().addCriteria(Criteria.where("senderId").is(senderId).and("receiverId").is(receiverId));
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.limit(1);

        RequestMessage requestMessage = mongoTemplate.findOne(query, RequestMessage.class);

        Update update = Update.update("msgHandle", MsgHandleEnum.PROCESSED);
        mongoTemplate.findAndModify(query, update, RequestMessage.class);
        return requestMessage.getId().toHexString();
    }


    //TODO 添加好友  ，同意得话 删除以前得请求，在两个人得好友中添加上对方
    public String saveFriendRequest(ChatMsg chatMsg) {
        // String senderId, String receiverId, String msg
        RequestMessage requestMessage = new RequestMessage();

        requestMessage.setAction(MsgTypeEnum.FRIEND_REQUEST);
        requestMessage.setMsg(chatMsg.getMsg());
        requestMessage.setSenderId(chatMsg.getSenderId());
        requestMessage.setReceiverId(chatMsg.getReceiverId());
        requestMessage.setMsgHandle(MsgHandleEnum.UNTREATED);
        requestMessage.setSendTime(new Date());

        mongoTemplate.insert(requestMessage, "requestMessage");
        return requestMessage.getId().toHexString();
    }

    public List<UserVO> getFriends(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        UserFriend userFriend = mongoTemplate.findOne(query, UserFriend.class);
        if (userFriend == null) {
            return null;
        }
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


    // 每次登录获取未处理的好友请求
    public List<FriendRequestVO> getUntreatedFriendRequest(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("receiverId").is(userId).and("action").is(MsgTypeEnum.FRIEND_REQUEST).and("msgHandle").is(MsgHandleEnum.UNTREATED));

        List<RequestMessage> requestMessages = mongoTemplate.find(query, RequestMessage.class);
        List<FriendRequestVO> requestMsgs = new ArrayList<>();
        requestMessages.forEach(requestMessage -> {
            requestMsgs.add(msgService.packageFriendRequestVO(requestMessage));
        });

        return requestMsgs;
    }
}
