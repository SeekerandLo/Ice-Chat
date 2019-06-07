<template>
  <div class="tag-message-list">
    <basic-container>
       <el-tabs v-model="activeTab" @tab-click="handleClick">
        <el-tab-pane label="我的消息" name="message">
        <div class="message-item" v-for="message in requests" :key="message.title">
          <!-- TODO 切块  -->
          <div class="d2-text-center">{{message.dateTime}}</div>
          <!-- 好友请求，谁请求的，理由是啥 -->
          <a href="##" class="title .d2-text-center">
            {{message.senderName}}
          </a>
          <div class=".d2-text-center">
            {{ message.msg }}
          </div>
          <!-- 下为时间 -->
          <!-- 按钮 -->
          <div>
            <el-button type="primary" size="small" @click="handleAgree">同意</el-button>
            <el-button type="default" size="small" @click="handleRefuse">拒绝</el-button>
          </div>

        </div>
        <el-button type="text" class="show-more"> 查看全部消息</el-button>
        </el-tab-pane>
        <!-- <el-tab-pane label="待我处理" name="todo">
          <p class="placeholder"> 暂无数据 </p>
        </el-tab-pane> -->
      </el-tabs>
    </basic-container>
  </div>
</template>

<script>
// TODO 在created（）时发送请求，获取接收者是当前用户的请求，且是未处理的
import BasicContainer from '@vue-materials/basic-container'
import { mapState, mapActions } from 'vuex'

export default {
  components: { BasicContainer },
  name: 'TagMessageList',
  data () {
    return {
      activeTab: 'message',
      messageList: [
        
      ]
    }
  },
  computed: {
    ...mapState('d2admin/log', ['requests']),
  },
  methods: {
    ...mapActions('d2admin/log',[]),
    handleClick (tab, event) {
      console.log(tab.name)
    },
    handleAgree(){

    },
    handleRefuse(){

    }
  }
}
</script>
<style scoped lang="scss">
@import '~normalize.css/normalize.css';
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
</style>
