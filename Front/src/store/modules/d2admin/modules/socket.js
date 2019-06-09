import util from '@/libs/util.js'

export default {
  namespaced: true,
  state: {
    // socket集合
    SOCKET_STORE: [

    ],
    // 消息集合
    CONTENTS: [

    ],
    // 保持连接
    KEEPALIVE_SOCKET: null
  },
  mutations: {
    addContent (state, chatMsg) {
      state.CONTENTS.push(chatMsg)
    }
  },
  actions: {
    // 初始化
    init ({ state, dispatch }, { receiver, websocket }) {
      return new Promise(async resolve => {
        var userId = util.cookies.get('objectid')
        // 创建管理对象 将来在关闭的时候是通过这个来的
        if (receiver.userId === 'server') {
          state.KEEPALIVE_SOCKET = websocket
        } else {
          var userSocket = {
            socket: websocket,
            userId: userId,
            reveiverId: receiver.userId
          }

          state.SOCKET_STORE.push(userSocket)
        }
        resolve()
      })
    },
    // 点击窗口的关闭按钮时提交的
    close ({ state, dispatch }, { receiver }) {
      return new Promise(async resolve => {
        state.SOCKET_STORE.map((SOCKET, index) => {
          if (SOCKET.reveiverId === receiver.userId) {
            SOCKET.socket.close()
            state.SOCKET_STORE.splice(index, 1)
          }
        })
        resolve()
      })
    },
    // 发送消息
    chatAt ({ state, dispatch, commit }, { chatMsg, receiver }) {
      commit('addContent', chatMsg)
    },
    // 接收消息
    onMessage ({ state, dispatch, commit }, { chatMsg, receiver }) {
      commit('addContent', chatMsg)
    }
  }
}
