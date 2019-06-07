import util from '@/libs/util.js'
import { addLocale } from 'core-js';

export default {
  namespaced: true,
  state: {
    // socket集合
    SOCKET_STORE: [

    ],
    CONTENTS: [

    ]
  },
  mutations: {
    addContent(state, chatMsg) {
      state.CONTENTS.push(chatMsg)
    }
  },
  actions: {
    // 初始化
    init({ state, dispatch }, { receiver, websocket }) {
      return new Promise(async resolve => {
        var userId = util.cookies.get('objectid')

        // 创建管理对象 将来在关闭的时候是通过这个来的
        var userSocket = {
          socket: websocket,
          userId: userId,
          reveiverId: receiver.userId
        }

        state.SOCKET_STORE.push(userSocket)

        resolve()
      })
    },
    close({ state, dispatch }, { receiver }) {
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
    chatAt({ state, dispatch,commit }, { chatMsg, receiver }) {
      commit('addContent', chatMsg)
    },
    onMessage({ state, dispatch, commit }, { chatMsg, receiver }) {
      commit('addContent', chatMsg)
    }
  }
}
