<template>
  <div>
    <d2-container>
      <chat-view :receiver="receiver" :TEMP_CONNENTS="CONTENTS"></chat-view>
    </d2-container>
  </div>
</template>

<script>
import ChatView from '../../components/chat-window'
import { mapState } from 'vuex';
export default {
  // 如果需要缓存页面
  // name 字段需要设置为和本页路由 name 字段一致
  name: 'chat',
  components: {
    ChatView
  },
  data () {
    return {
      receiver: {}
    }
  },
  computed: {
    ...mapState('d2admin/socket', ['CONTENTS'])
  },
  watch: {
    // 切换路由时 建立的
    $route () {
      var receiver = {
        userId: this.$route.query.userId,
        username: this.$route.query.username
      }
      this.receiver = receiver
    }
  },
  // 初次打开时建立
  created () {
    var query = this.$route.query
    this.receiver = {
      userId: query.userId,
      username: query.username
    }

    // console.log(this.$store.state.d2admin.socket.CONTENTS)
    // TODO 把聊天记录全部保存到 vuex 中在这里把取出，然后传递给子组件，子组件渲染，同时更新vuex
  }
}
</script>
