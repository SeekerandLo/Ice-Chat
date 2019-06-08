<template>
  <!-- d2-container 文档 https://doc.d2admin.fairyever.com/zh/sys-components/container.html -->
  <d2-container class="page-request">
    <tag-message-list :messageList="messageList"/>
  </d2-container>
</template>

<script>

// 组件
import TagMessageList from './components/TagMessageList/index'
import { mapActions } from 'vuex'

// 请删除下面代码中你不需要的部分
export default {
  name: 'request',
  components: {
    TagMessageList
  },
  created () {
  },
  // 数据
  data () {
    return {
      messageList: []
    }
  },
  // 计算属性
  computed: {},
  // 侦听器
  watch: {},
  // 生命周期钩子
  beforeCreate () {},
  beforeMount () {},
  mounted () {},
  beforeUpdate () {},
  updated () {},
  activated () {},
  deactivated () {},
  beforeDestroy () {},
  destroyed () {},
  // 事件处理方法
  methods: {
    ...mapActions('d2admin/log', ['getUntreatedRequest'])
  }
}
</script>

<style lang="scss" scoped>
@import './Request.scss';
</style>
