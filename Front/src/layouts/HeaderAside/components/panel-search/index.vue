<template>
  <div class="panel-search" flex="dir:top">
    <div
      class="panel-search__input-group"
      flex-box="0"
      flex="dir:top main:center cross:center"
      @click.self="handlePanelClick"
    >
      <d2-icon-svg class="panel-search__logo" name="d2-admin-text"/>
      <el-autocomplete
        class="panel-search__input"
        ref="input"
        v-model="searchText"
        suffix-icon="el-icon-search"
        placeholder="搜索好友"
        :fetch-suggestions="querySearch"
        :trigger-on-focus="false"
        :clearable="true"
        @keydown.esc.native="handleEsc"
        @keydown.enter.native="handleEnter"
        @select="handleSelect"
      >
        <!-- <d2-panel-search-item slot-scope="{ item }" :item="item"/> -->
      </el-autocomplete>
    </div>

    <!--  TODO 显示  -->

    <div v-if="searchResult.length> 0">
      <el-card>
        <div class="panel-search__results-group-inner">
          <d2-panel-search-item
            v-for="(item, index) in searchResult"
            :key="index"
            :item="item"
            :hover-mode="true"

          />
        </div>
      </el-card>

    </div>
  </div>
</template>

<script>
import Fuse from 'fuse.js'
import { mapState, mapActions } from 'vuex'

import mixin from '../mixin/menu'
export default {
  mixins: [mixin],
  components: {
    'd2-panel-search-item': () =>
      import('./components/panel-search-item/index.vue')
  },
  data () {
    return {
      searchText: '',
      results: []
    }
  },
  destroyed () {
    this.clearSearchResult()
  },
  computed: {
    // 混入 data 中
    ...mapState('d2admin/search', ['hotkey', 'pool', 'searchResult'])
  },
  methods: {
    ...mapActions('d2admin/search', ['search', 'clearSearchResult']),

    /**
     * @description 过滤选项 这个方法在每次输入框的值发生变化时会触发
     */
    querySearch (queryString, callback) {
      var pool = this.pool
      const results = this.query(queryString ? pool : [], queryString)
      this.results = results
      callback(results)
    },
    /**
     * @description 指定的数据源中根据指定的查询字符串过滤数据
     * @param {Object} pool 需要过滤的数据
     * @param {String} queryString 查询字符串
     */
    query (pool, queryString) {
      return new Fuse(pool, {
        shouldSort: true,
        tokenize: true,
        threshold: 0.6,
        location: 0,
        distance: 100,
        maxPatternLength: 32,
        minMatchCharLength: 1,
        keys: ['fullTitle', 'path']
      })
        .search(queryString)
        .map(e => ({
          value: e.fullTitle,
          ...e
        }))
    },
    /**
     * @description 聚焦输入框
     */
    focus () {
      this.input = ''
      setTimeout(() => {
        if (this.$refs.input) {
          this.$refs.input.focus()
        }
        // 还原
        this.searchText = ''
        this.results = []
      }, 500)
    },
    /**
     * @description 接收用户在列表中选择项目的事件
     * TODO 修改为查看好友信息
     */
    handleResultsGroupItemClick (path) {
      // 如果用户选择的就是当前页面 就直接关闭搜索面板
      if (path === this.$route.path) {
        this.handleEsc()
        return
      }
      // 用户选择的是其它页面
      this.handleMenuSelect(path)
    },
    /**
     * @description 接收用户在下拉菜单中选中事件
     */
    handleSelect ({ path }) {
      // 如果用户选择的就是当前页面 就直接关闭搜索面板
      if (path === this.$route.path) {
        this.handleEsc()
        return
      }
      // 用户选择的是其它页面
      this.$nextTick(() => {
        this.handleMenuSelect(path)
      })
    },
    /**
     * @augments 关闭输入框的下拉菜单
     */
    closeSuggestion () {
      if (this.$refs.input.activated) {
        this.$refs.input.suggestions = []
        this.$refs.input.activated = false
      }
    },
    /**
     * @augments 接收用户点击空白区域的关闭
     */
    handlePanelClick () {
      this.handleEsc()
    },
    /**
     * @augments 接收用户触发的关闭
     */
    handleEsc () {
      this.closeSuggestion()
      this.$nextTick(() => {
        this.$emit('close')
      })
    },
    /**
     * 监听回车事件
     */
    handleEnter () {
      this.search({
        searchText: this.searchText
      }).then(() => {})
    },

    handleAddFriend () {
      console.log('asdad')
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-search {
  margin: 20px;
  width: 100%;
  .panel-search__input-group {
    height: 240px;
    .panel-search__logo {
      width: 80px;
      height: 80px;
      margin-bottom: 20px;
    }
    .panel-search__input {
      width: 500px;
    }
    .panel-search__tip {
      @extend %unable-select;
      margin-top: 20px;
      margin-bottom: 40px;
      font-size: 12px;
      color: $color-text-sub;
      .panel-search__key {
        padding: 1px 5px;
        margin: 0px 2px;
        border-radius: 2px;
        background-color: $color-text-normal;
        color: $color-bg;
      }
    }
  }
  .panel-search__results-group {
    overflow: auto;
    margin-bottom: -20px;
    .panel-search__results-group-inner {
      margin: -20px;
    }
  }
}
</style>
