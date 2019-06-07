<template>
	<d2-container>
		<template slot="header">
			<div class="chat_header">
				<img src="../../../public/image/avatar/avatar.jpeg" class="img_avatar">
				<span class="chat_header_name">{{receiver.username}}</span>
			</div>
			<!-- TODO 为何在渲染页面时 在计算属性中不行呢？ -->
			<!-- VUE 不能检测 对象属性的增加和删除 -->
		</template>
		<div class="chat_content">
			<div v-for="(mes,index) in getTargetMsgSS" :key="index">
				<div :class="[mes.receiverId === info.id ? receive_Class : send_Class ]" style>{{mes.msg}}</div>
			</div>
		</div>
		<div></div>
		<template slot="footer" class="chat_footer">
			<div class="chat_footer_fun">功能区</div>
			<el-input v-model="message" placeholder style="width: 93%; margin-right:17px"></el-input>
			<el-button type="primary" @click="chat">发送</el-button>
		</template>
	</d2-container>
</template>

<script>
/**
 * MessageContent:聊天内容，包括接收到的消息和发送的消息
 * receiver：接收者，就是当前聊天的好友
 * message：输入的消息内容
 *
 * send_Class：发送消息的class
 * receive_Class：接收消息的class
 *
 * CHAT_TYPE：消息类型
 *
 * webSocket：socket连接
 *
 * 更新路由时 覆盖了，再切换回去没有回，因为打开的
 *
 */

// TODO 输入文字之后才能发送，按钮变色，变可用
import { mapState, mapActions } from "vuex";
import { CHAT_TYPE } from "../../chat.config";
// TODO 聊天消息 计算属性的监听 第一次发送时没有显示 ，但是 第二次之后 才正常显示

export default {
	data() {
		return {
			webSocket: null,
			message: "",
			MessageContent: [],
			send_Class: "send_mes",
			receive_Class: "receive_mes",
			CHAT_TYPE: {}
		};
	},
	props: ["receiver"],

	created() {
		this.CHAT_TYPE = CHAT_TYPE();
		if (
			this.webSocket !== null &&
			this.webSocket !== undefined &&
			this.webSocket.readyState === WebSocket.OPEN
		) {
		} else {
			this.initWebSocket();
		}
		// }
	},
	// watch: {
	// 	$route(to, from) {
	// 		console.log(this.CONTENTS);
	// 		this.MessageContent = this.getTargetMsg(this.CONTENTS);
	// 		// 每当路由变化，
	// 	}
	// },
	updated() {
		// 更新滚动跳
		this.$nextTick(() => {
			let msgContent = document.getElementsByClassName(
				"d2-container-full__body"
			)[1];
			msgContent.scrollTop = msgContent.scrollHeight; // 滚动高度
		});
	},
	computed: {
		...mapState("d2admin/user", ["info"]),
		...mapState("d2admin/socket", ["CONTENTS"]),
		// TODO 现在的问题是，虽然我把它放在了计算属性中，但是却没有 更新它，所以不会变化，
		// this.MessageContent = this.getTargetMsg()
		getTargetMsgSS() {
			console.log(this.CONTENTS);
			var a = this.CONTENTS.filter(element => {
				console.log(element);

				if (
				element.receiverId === this.info.id &&	element.senderId === this.receiver.userId 
					
				) {
					return element;
				} else if (element.receiverId === this.receiver.userId) {
					return element;
				}
			});
			console.log(a);
			return a;
		}
	},
	methods: {
		...mapActions("d2admin/socket", ["init", "chatAt", "onMessage"]),
		// websocket初始化
		initWebSocket() {
			const uri = process.env.VUE_APP_WEBSOCKETURI;
			this.webSocket = new WebSocket(uri);

			this.webSocket.onopen = this.webSocketOpen;
			this.webSocket.onerror = this.webSocketError;
			this.webSocket.onmessage = this.webSocketOnMessage;
			this.webSocket.onclose = this.webSocketClose;

			this.init({
				receiver: this.receiver,
				websocket: this.webSocket
			});
		},
		// websocket创建连接
		webSocketOpen() {
			console.log("WebSocket连接成功");
			var chatMsg = this.chactMsg(
				this.info.id,
				this.receiver.userId,
				null,
				null
			);
			var dataContent = this.dataContent(this.CHAT_TYPE.CONNECT, chatMsg, null);
			this.webSocketSend(JSON.stringify(dataContent));
		},
		// websocket发生错误
		webSocketError(e) {
			console.log("WebSocket连接错误");
			console.log(e);
		},
		// websocket接收消息
		webSocketOnMessage(receivedMessage) {
			var receivedMessageContent = JSON.parse(receivedMessage.data);

			// console.log(receivedMessageContent);

			this.MessageContent.push(receivedMessageContent);
			this.onMessage({
				chatMsg: JSON.parse(receivedMessage.data),
				receiver: this.receiver
			});
		},
		// websocket发送消息
		webSocketSend(dataContent) {
			// console.log(dataContent);
			this.webSocket.send(dataContent);
		},
		// websocket关闭
		webSocketClose(e) {
			console.log(e);
			console.log("连接关闭");
		},
		// 聊天方法
		chat() {
			var msg = this.message;
			// 发给后台的
			var chatMsg = this.chactMsg(
				this.info.id,
				this.receiver.userId,
				msg,
				null
			);
			this.message = "";

			this.MessageContent.push(chatMsg);

			// 前端

			this.chatAt({
				chatMsg: chatMsg,
				receiver: this.receiver
			});

			var dataContent = this.dataContent(this.CHAT_TYPE.CHAT, chatMsg, null);
			this.webSocketSend(JSON.stringify(dataContent));
		},
		// 封装消息体
		chactMsg(senderId, receiverId, msg, msgId) {
			var chatMsg = {
				senderId: senderId,
				receiverId: receiverId,
				msg: msg,
				msgId: msgId
			};

			return chatMsg;
		},
		// 封装发送内容
		dataContent(action, chatMsg, extend) {
			var dataContent = {
				action: action,
				chatMsg: chatMsg,
				extend: extend
			};

			return dataContent;
		},

		getTargetMsg(CONTENTS) {
			var a = [];
			console.log("调用");
			return CONTENTS.filter(element => {
				if (element.receiverId === this.receiver.userId) {
					a.push(element);
				}
				if (
					element.sendId === this.receiver.userId &&
					element.receiverId === this.info.id
				) {
					a.push(element);
				}
				return a;
			});
		}
	}
};
</script>

<style>
.chat_header {
	display: flex;
	align-items: center;
}
.chat_header_name {
	font-weight: 600;
}
.img_avatar {
	height: 50px;
	width: 50px;
	border-radius: 50%;
	border: 1px solid #f1f1f1;

	margin-right: 10px;
}
.chat_footer {
	display: flex;
}
.chat_footer_fun {
	width: 100%;
	border: 1px solid #f1f1f1;
	margin-bottom: 10px;
	height: 30px;
}

/*  配置在一行  */
.send_mes {
	float: right;
	margin-left: 50%;

	background: #fff;
	min-width: 150px;
	border-radius: 4px;
	border: 1px solid #ebeef5;
	padding: 12px;
	z-index: 2000;
	color: #606266;
	line-height: 1.4;
	text-align: justify;
	font-size: 14px;
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	word-break: break-all;

	margin-bottom: 20px;
}

.receive_mes {
	float: left;
	margin-right: 50%;

	background: #fff;
	min-width: 150px;
	border-radius: 4px;
	border: 1px solid #ebeef5;
	padding: 12px;
	z-index: 2000;
	color: #606266;
	line-height: 1.4;
	text-align: justify;
	font-size: 14px;
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	word-break: break-all;

	margin-bottom: 20px;
}
</style>
