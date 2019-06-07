import request from '@/plugin/axios'

export function SearchFriend (data) {
  var searchText = data.searchText
  return request({
    url: `/friend/search`,
    method: 'get',
    params: {
      searchText
    }
  })
}
