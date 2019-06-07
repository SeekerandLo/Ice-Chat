import util from '@/libs/util.js'
import { addLocale } from 'core-js';

export default {
  namespaced: true,
  state: {
    // socket集合
    SOCKET_STORE: [

    ],
    CONTENTS: {

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
    chatAt({ state, dispatch }, { chatMsg, receiver }) {
      // console.log(chatMsg)
      // console.log(receiver)

      // 判断与该用户的聊天记录 是否存在
      // console.log(Object.keys(state.CONTENTS))
      // 如果包含
      if (Object.keys(state.CONTENTS).indexOf(receiver.userId) > -1) {
        var receiverId = receiver.userId
        state.CONTENTS[receiverId].push(chatMsg)
      } else {
        var content = []
        content.push(chatMsg)

        state.CONTENTS[receiver.userId] = content
      }
      console.log(state.CONTENTS)
    },
    onMessage({ state, dispatch }, { chatMsg, receiver }) {
      if (Object.keys(state.CONTENTS).indexOf(receiver.userId) > -1) {
        var receiverId = receiver.userId
        state.CONTENTS[receiverId].push(chatMsg)
      } else {
        var content = []
        content.push(chatMsg)
        state.CONTENTS[receiver.userId] = content
      }
    }
  }
}
