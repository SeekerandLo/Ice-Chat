/* eslint-disable */
import util from './libs/util.ice'; // 菜单配置
// 侧栏菜单配置
// ice 会在新建页面的时候 push 数据
// ice 自动添加的菜单记录是以下格式：(不会有嵌套)
const asideMenuConfig = [
  {
    name: '功能',
    icon: 'folder-o',
    children: [
    ],
  },
  {
    name: '请求',
    path: '/request',
    icon: 'home',
  },
]; // 顶栏菜单配置
// ice 不会修改 headerMenuConfig
// 如果你需要功能开发之前就配置出菜单原型，可以只设置 name 字段
// D2Admin 会自动添加不重复 id 生成菜单，并在点击时提示这是一个临时菜单

const headerMenuConfig = [
  
]; // 请根据自身业务逻辑修改导出设置，并在合适的位置赋给对应的菜单
// 参考
// 设置顶栏菜单的方法 (vuex)
// $store.commit('d2adminMenuHeaderSet', menus)
// 设置侧边栏菜单的方法 (vuex)
// $store.commit('d2adminMenuAsideSet', menus)
// 你可以在任何地方使用上述方法修改顶栏和侧边栏菜单
// 导出顶栏菜单

export const menuHeader = util.recursiveMenuConfig(headerMenuConfig); // 导出侧边栏菜单

export const menuAside = util.recursiveMenuConfig(asideMenuConfig);
