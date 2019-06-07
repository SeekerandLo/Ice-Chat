import { GetFriendList } from '@api/sys.friend'

export default {
  namespaced: true,
  state: {
    // 用户信息
    info: {},
    friends: [],
    chatWindows: []
  },
  actions: {
    /**
     * @description 设置用户数据
     * @param {Object} state vuex state
     * @param {*} info info
     */
    set ({ state, dispatch }, info) {
      return new Promise(async resolve => {
        // console.log('user/set  ↓')
        // console.log(info)

        // store 赋值
        state.info = info
        // 持久化
        await dispatch('d2admin/db/set', {
          dbName: 'sys',
          path: 'user.info',
          value: info,
          user: true
        }, { root: true })
        // end
        resolve()
      })
    },
    /**
     * @description 从数据库取用户数据
     * @param {Object} state vuex state
     */
    load ({ state, dispatch }) {
      return new Promise(async resolve => {
        // store 赋值
        state.info = await dispatch('d2admin/db/get', {
          dbName: 'sys',
          path: 'user.info',
          defaultValue: {},
          user: true
        }, { root: true })
        // end
        resolve()
      })
    },

    getFriendList ({ state, dispatch }, {
      userId = ''
    } = {}) {
      return new Promise((resolve, reject) => {
        GetFriendList({
          userId
        }).then(async res => {
          // TODO 加到计算属性中
          state.friends = res
          // console.log(res)
          resolve()
        })
          .catch(err => {
            console.log('err: ', err)
            reject(err)
          })
      })
    }
  }
}
