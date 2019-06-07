import setting from '@/setting.js'
import { SearchFriend } from '@api/sys.search'

export default {
  namespaced: true,
  state: {
    // 搜索面板激活状态
    active: false,
    // 快捷键
    hotkey: {
      open: setting.hotkey.search.open,
      close: setting.hotkey.search.close
    },
    // 所有可以搜索的页面
    pool: [],
    searchResult: []
  },
  mutations: {
    /**
     * @description 切换激活状态
     * @param {Object} state vuex state
     */
    toggle (state) {
      state.active = !state.active
    },
    /**
     * @description 设置激活模式
     * @param {Object} state vuex state
     * @param {Boolean} active active
     */
    set (state, active) {
      state.active = active
    },
    /**
     * @description 初始化
     * @param {Object} state vuex state
     * @param {Array} menu menu
     */
    init (state, menu) {
      const pool = []
      const push = function (menu, titlePrefix = []) {
        menu.forEach(m => {
          if (m.children) {
            push(m.children, [...titlePrefix, m.title])
          } else {
            pool.push({
              ...m,
              fullTitle: [...titlePrefix, m.title].join(' / ')
            })
          }
        })
      }
      push(menu)
      state.pool = pool
    }
  },
  actions: {
    search ({ dispatch, state }, {
      searchText = ''
    } = {}) {
      return new Promise((resolve, reject) => {
        SearchFriend({
          searchText
        }).then(async res => {
          // TODO 加到计算属性中去
          console.log('找到的朋友 ↓')
          console.log(res)
          state.searchResult = res
          resolve()
        }).catch(err => {
          console.log('err: ', err)
          reject(err)
        })
      })
    },
    clearSearchResult ({ dispatch, state }) {
      state.searchResult = []
    }
  }
}
