(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-30303a0a"],{"0650":function(e,t,n){},4853:function(e,t,n){"use strict";var s=n("0650");n.n(s).a},"6cd5":function(e,t,n){"use strict";n.r(t);var s=n("1607"),a=n("c276"),c={data:function(){return{CHAT_TYPE:null,userId:null}},created:function(){this.CHAT_TYPE=Object(s.a)(),this.userId=a.a.cookies.get("objectid")},props:{item:{default:function(){return{}}},hoverMode:{default:!1},keepAliveSocket:{}},methods:{handleAddFriend:function(){var a=this;this.$prompt("输入验证信息","添加好友",{confirmButtonText:"确定",cancelButtonText:"取消"}).then(function(e){var t=e.value,n=a.chactMsg(a.userId,a.item.userId,t,null),s=a.dataContent(a.CHAT_TYPE.FRIEND_REQUEST,n,null);a.keepAliveSocket.send(JSON.stringify(s)),a.$message({type:"success",message:"已发送"})}).catch(function(){a.$message({type:"info",message:"取消输入"})})},chactMsg:function(e,t,n,s){return{senderId:e,receiverId:t,msg:n,msgId:s}},dataContent:function(e,t,n){var s={action:e,chatMsg:t,extend:n};return s}}},i=(n("4853"),n("2877")),o=function(e){e.options.__source="src/layouts/HeaderAside/components/panel-search/components/panel-search-item/index.vue"},r=Object(i.a)(c,function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"d2-panel-search-item el-card",class:e.hoverMode?"can-hover":"",staticStyle:{"margin-bottom":"10px"},attrs:{flex:""},on:{click:e.handleAddFriend}},[n("div",{staticClass:"d2-panel-search-item__icon",attrs:{"flex-box":"0"}},[n("div",{staticClass:"d2-panel-search-item__icon-box",attrs:{flex:"main:center cross:center"}},[n("d2-icon")],1)]),n("div",{staticClass:"d2-panel-search-item__info",attrs:{"flex-box":"1",flex:"dir:top"}},[n("div",{staticClass:"d2-panel-search-item__info-title",attrs:{"flex-box":"1",flex:"cross:center"}},[n("span",[e._v(e._s(e.item.username))])])])])},[],!1,null,"6a24df13",null);"function"==typeof o&&o(r);t.default=r.exports}}]);