export default {
  namespaced: true,
  state: {
    // 错误日志
    // + 日志条目的属性
    //   - message 必须 日志信息
    //   - type 非必须 类型 success | warning | info(默认) | danger
    //   - time 必须 日志记录时间
    //   - meta 非必须 其它携带信息
    requests: []
  },
  getters: {
    /**
     * @description 返回现存 log (all) 的条数
     * @param {*} state vuex state
     */
    // TODO 修改为request的length
    length (state) {
      return state.requests.length
    }
  },
  actions: {
    // TODO 修改state
    addFriendRequest ({ state, commit }, { request }) {

    }
  },
  mutations: {
    receiverRequest (state, request) {
      state.requests.push(request)
    }
  }
}
