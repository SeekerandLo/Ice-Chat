<template>
  <div class="tag-message-list">
    <div v-if="requests.length > 0">
      <div
        v-for="(message,index) in requests"
        :key="index"
        style="margin-bottom:10px;"
        class="d2-panel-search-item el-card"
        :class="hoverMode ? 'can-hover' : ''"
        flex
      >
        <div class="d2-panel-search-item__info" flex-box="1" flex="dir:top">
          <div class="d2-panel-search-item__info-title" flex-box="1" flex="cross:center">
            <div class="request-tag">
              时间
              : {{message.dateTime}}
            </div>
            <div class="request-tag">
              用户名:
              {{message.senderName}}
            </div>
            <div class="request-tag">
              验证信息:
              {{ message.msg }}
            </div>
            <div class="request-button">
              <el-button type="primary" size="small" @click="handleAgree(message)">同意</el-button>
              <el-button type="default" size="small" @click="handleRefuse(message)">拒绝</el-button>
            </div>
          </div>
        </div>
      </div>

      <el-button type="text" class="show-more">查看全部消息</el-button>
      <!-- <el-tab-pane label="待我处理" name="todo">
          <p class="placeholder"> 暂无数据 </p>
      </el-tab-pane>-->
    </div>
    <div v-else>
      <h1 class="index-noFriend">没有请求...</h1>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  name: 'TagMessageList',
  data () {
    return {
      activeTab: 'message',
      messageList: [],
      hoverMode: {
        default: false
      }
    }
  },
  created () {},
  computed: {
    ...mapState('d2admin/log', ['requests'])
  },
  methods: {
    ...mapActions('d2admin/log', ['agree', 'refuse', 'getUntreatedRequest']),
    handleClick (tab, event) {
      console.log(tab.name)
    },
    handleAgree (message) {
      var senderId = message.senderId
      var receiverId = message.receiverId
      // console.log(message)

      this.agree({
        senderId,
        receiverId
      }).then(() => {
        this.$message({
          type: 'success',
          message: '已同意'
        })
      })
    },
    handleRefuse (message) {
      console.log(message)
    }
  }
}
</script>
<style scoped lang="scss">
@import "~normalize.css/normalize.css";

.index-noFriend {
  color: rgb(139, 139, 139);
}

.request-button {
  margin-left: 20px;
}

.request-tag {
  margin-right: 20px;
}

.tag-message-list {
  .el-tabs__content {
    color: #666;
    font-size: 14px;
  }

  .message-item {
    display: flex;
    justify-content: space-between;
    padding: 15px 0;
    border-bottom: 1px solid rgb(244, 244, 244);
    // 文字垂直居中
    a.title {
      color: #666;
      text-decoration: none;
    }
    .date {
      font-size: 12px;
    }
  }
  .show-more {
    display: block;
    margin: 20px auto 0;
    padding: 0;
  }
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
