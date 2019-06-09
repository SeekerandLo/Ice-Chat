<template>
  <div
    class="d2-panel-search-item el-card"
    style="margin-bottom:10px"
    :class="hoverMode ? 'can-hover' : ''"
    flex
    @click="handleAddFriend"
  >
    <div class="d2-panel-search-item__icon" flex-box="0">
      <div class="d2-panel-search-item__icon-box" flex="main:center cross:center">
        <d2-icon></d2-icon>
      </div>
    </div>
    <div class="d2-panel-search-item__info" flex-box="1" flex="dir:top">
      <div class="d2-panel-search-item__info-title" flex-box="1" flex="cross:center">
        <span>{{item.username}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { CHAT_TYPE } from '@/chat.config'
import util from '@/libs/util.js'

export default {
  data () {
    return {
      CHAT_TYPE: null,
      userId: null
    }
  },
  created () {
    this.CHAT_TYPE = CHAT_TYPE()
    this.userId = util.cookies.get('objectid')
  },
  props: {
    item: {
      default: () => ({})
    },
    hoverMode: {
      default: false
    },
    keepAliveSocket: {}
  },
  methods: {
    handleAddFriend () {
      this.$prompt('输入验证信息', '添加好友', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        var chactMsg = this.chactMsg(
          this.userId,
          this.item.userId,
          value,
          null
        )

        var dataContent = this.dataContent(
          this.CHAT_TYPE.FRIEND_REQUEST,
          chactMsg,
          null
        )
        // 未做任何处理的请求
        this.keepAliveSocket.send(JSON.stringify(dataContent))

        this.$message({
          type: 'success',
          message: '已发送'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    // 封装消息体
    chactMsg (senderId, receiverId, msg, msgId) {
      var chatMsg = {
        senderId: senderId,
        receiverId: receiverId,
        msg: msg,
        msgId: msgId
      }

      return chatMsg
    },
    // 封装发送内容
    dataContent (action, chatMsg, extend) {
      var dataContent = {
        action: action,
        chatMsg: chatMsg,
        extend: extend
      }

      return dataContent
    }
  }
}
</script>

<style lang="scss" scoped>
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
