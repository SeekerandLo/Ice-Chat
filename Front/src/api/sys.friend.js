import request from '@/plugin/axios'
import { REQUEST_ACTION } from '@/chat.config'

var action = REQUEST_ACTION()

export function GetFriendList(data) {
  var userId = data.userId
  return request({
    url: `/friend/list`,
    method: 'get',
    params: {
      userId
    }
  })
}

// 同意好友请求
export function Agree(data) {
  var senderId = data.senderId;
  var receiverId = data.receiverId;
  return request({
    url: `/friend/action`,
    method: 'get',
    params: {
      "senderId": senderId,
      "receiverId": receiverId,
      "action": action.AGREE
    }
  })
}

// 拒绝好友请求
export function Refuse(data) {
  var senderId;
  var receiverId;
  return request({
    url: `/friend/action`,
    method: 'get',
    params: {
      "senderId": senderId,
      "receiverId": receiverId,
      "action": action.REFUSE
    }
  })
}

// 获取未处理的请求
export function GetUntreatedRequest(data){
  var userId = data['userId']
  return request({
    url: `/friend/untreated`,
    method: 'get',
    params: {
      userId
    }
  })
}


