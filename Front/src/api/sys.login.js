import request from '@/plugin/axios'

export function AccountLogin (data) {
  console.log(data)
  return request({
    url: '/account/login',
    method: 'post',
    data
  })
}

export function AccountRegister (data) {
  console.log('用户注册')
  return request({
    url: '/account/register',
    method: 'post',
    data
  })
}
