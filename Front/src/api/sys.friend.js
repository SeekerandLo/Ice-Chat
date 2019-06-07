import request from '@/plugin/axios'

export function GetFriendList (data) {
  var userId = data.userId
  return request({
    url: `/friend/list`,
    method: 'get',
    params: {
      userId
    }
  })
}
