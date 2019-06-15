import request from '@/plugin/axios'

export function GetMsgHistory(userId,receiverId) {



  return request({
    url: '/message/history',
    method: 'get',
    params: {
      "userId": userId,
      "friendId": receiverId
    }
  })
} 