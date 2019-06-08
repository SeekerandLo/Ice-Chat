import request from '@/plugin/axios'

export function SearchFriend (data) {
  var searchText = data.searchText
  var me = data.me
  return request({
    url: `/friend/search`,
    method: 'get',
    params: {
      'searchText': searchText,
      'me': me
    }
  })
}
