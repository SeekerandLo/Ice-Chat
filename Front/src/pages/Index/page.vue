<template>
  <d2-container class="page">
    <div v-for="(friend,index) in friends" :key="index" style="margin-bottom:10px;"  @click="goChat(friend)" class="d2-panel-search-item el-card" :class="hoverMode ? 'can-hover' : ''" flex>
      <div class="d2-panel-search-item__icon" flex-box="0">
        <div class="d2-panel-search-item__icon-box" flex="main:center cross:center">
          <d2-icon></d2-icon>
        </div>
      </div>
      <div class="d2-panel-search-item__info" flex-box="1" flex="dir:top">
        <div class="d2-panel-search-item__info-title" flex-box="1" flex="cross:center">
          <span>{{ friend.username }}</span>
        </div>
      </div>
    </div>
  </d2-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import util from '@/libs/util.js'

export default {
  components: {
  },
  created () {
    console.log('初始化')
    // TODO 把自己的id传过去

    var userId = util.cookies.get('objectid')
    // console.log(userId)
    this.getFriendList({
      userId: userId
    })
  },
  computed: {
    ...mapState('d2admin/user', ['friends'])
  },
  data () {
    return {
      hoverMode: {
        default: false
      },
      filename: __filename,
      tableData: [
        {
          name: '你好',
          id: '5cf7b7971cc0e41488e5cbbd'
        }
      ],
      visible: false
    }
  },
  methods: {
    ...mapActions('d2admin/user', [
      'getFriendList'
    ]),
    goChat (friend) {
      // console.log(friend)
      this.$router.push({
        // 核心语句
        path: '/chat/', // 跳转的路径
        query: {
          // 路由传参时push和query搭配使用 ，作用时传递参数
          friend: friend,
          username: friend.username,
          userId: friend.userId
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  .page__logo {
    width: 120px;
  }
  .page__btn-group {
    color: $color-text-placehoder;
    font-size: 12px;
    margin-top: 0px;
    margin-bottom: 20px;
    span {
      color: $color-text-sub;
      &:hover {
        color: $color-text-main;
      }
    }
  }
}

.friend-wrapper {
  width: 30%;
  text-align: left;
}

.friend {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
  margin-bottom: 10px;
  cursor: pointer;
}

.d2-panel-search-item {
  height: 64px;
  margin: 0px -20px;
  &.can-hover {
    @extend %unable-select;
    margin: 0px;
    &:hover {
      background-color: #f5f7fa;
      .d2-panel-search-item__icon {
        .d2-panel-search-item__icon-box {
          i {
            font-size: 24px;
            color: $color-primary;
          }
        }
      }
      .d2-panel-search-item__info {
        .d2-panel-search-item__info-title {
          color: $color-text-main;
        }
        .d2-panel-search-item__info-fullTitle {
          color: $color-text-normal;
        }
        .d2-panel-search-item__info-path {
          color: $color-text-normal;
        }
      }
    }
  }
  .d2-panel-search-item__icon {
    width: 64px;
    .d2-panel-search-item__icon-box {
      height: 64px;
      width: 64px;
      border-right: 1px solid $color-border-3;
      i {
        font-size: 20px;
        color: $color-text-sub;
      }
      svg {
        height: 20px;
        width: 20px;
      }
    }
  }
  .d2-panel-search-item__info {
    margin-left: 10px;
    .d2-panel-search-item__info-title {
      font-size: 16px;
      line-height: 16px;
      font-weight: bold;
      color: $color-text-normal;
    }
    .d2-panel-search-item__info-fullTitle {
      font-size: 10px;
      line-height: 14px;
      color: $color-text-placehoder;
    }
    .d2-panel-search-item__info-path {
      margin-bottom: 4px;
      font-size: 10px;
      line-height: 14px;
      color: $color-text-placehoder;
    }
  }
}
</style>
