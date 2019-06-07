<template>
	<el-tooltip effect="dark" :content="tooltipContent" placement="bottom">
		<el-button class="d2-ml-0 d2-mr btn-text can-hover" type="text" @click="handleClick">
			<el-badge v-if="requestNumber > 0" :max="99" :value="requestNumber" :is-dot="requestNumber === 0">
				<d2-icon :name="requestNumber === 0 ? 'dot-circle-o' : 'bug'" style="font-size: 20px"/>
			</el-badge>
			<d2-icon v-else name="dot-circle-o" style="font-size: 20px"/>
		</el-button>
	</el-tooltip>
</template>

<script>
//　TODO 将心跳做成组件在全局注入
import { mapGetters, mapMutations, mapActions } from "vuex";
import { CHAT_TYPE } from "@/chat.config";
import util from "@/libs/util.js";

export default {
	data() {
		return {
			CHAT_TYPE: {},
			userId: ""
		};
	},
	created() {
		this.CHAT_TYPE = CHAT_TYPE();
		console.log("一直存在吗");
		this.userId = util.cookies.get("objectid");

		if (
			this.webSocket !== null &&
			this.webSocket !== undefined &&
			this.webSocket.readyState === WebSocket.OPEN
		) {
		} else {
			this.initWebSocket();
		}
	},
	computed: {
		...mapGetters("d2admin", {
			logLength: "log/length",
			logLengthError: "log/lengthError",
			requestNumber:"log/lengthRequest"
		}),
		// tooltipContent() {
		// 	return this.logLength === 0
		// 		? "没有日志或异常"
		// 		: `${this.logLength} 条日志${
		// 				this.logLengthError > 0
		// 					? ` | 包含 ${this.logLengthError} 个异常`
		// 					: ""
		// 		  }`;
		// }
		tooltipContent() {
			return this.requestNumber === 0
				? "没有请求"
				: `${this.requestNumber} 条请求`;
		}
	},
	methods: {
		...mapMutations("d2admin/log", ["clean"]),
		handleClick() {
			this.$router.push({
				name: "request"
			});
		},
		...mapActions("d2admin/socket", ["init"]),
		...mapActions("d2admin/log",["addFriendRequest"]),
		// websocket初始化
		initWebSocket() {
			const uri = process.env.VUE_APP_WEBSOCKETURI;
			this.webSocket = new WebSocket(uri);

			this.webSocket.onopen = this.webSocketOpen;
			this.webSocket.onerror = this.webSocketError;
			this.webSocket.onmessage = this.webSocketOnMessage;
			this.webSocket.onclose = this.webSocketClose;

			var receiver = {
				userId: "server"
			};
			// TODO 初始化 进入 vuex
			this.init({
				receiver: receiver,
				websocket: this.webSocket
			});
		},
		// websocket创建连接
		webSocketOpen() {
			// TODO 不需要传递 receiverId
			console.log("WebSocket连接成功");
			var chatMsg = this.chactMsg(this.userId, "server", null, null);
			var dataContent = this.dataContent(this.CHAT_TYPE.CONNECT, chatMsg, null);
			this.webSocketSend(JSON.stringify(dataContent));

			this.keepAlive();
		},
		// TODO websocket发生错误时要 从vuex中删除
		webSocketError(e) {
			console.log("WebSocket连接错误");
			console.log(e);
		},
		// websocket接收消息 TODO 心跳不接收信息
		webSocketOnMessage(receivedMessage) {
			console.log(receivedMessage.data)
			var request = JSON.parse(receivedMessage.data)
			console.log(request)
			// TODO 将好友请求保存到 vuex	
			this.addFriendRequest({
				request
			})

		
		},
		// websocket发送消息
		webSocketSend(dataContent) {
			this.webSocket.send(dataContent);
		},
		// websocket关闭 当关闭整个窗口的时候调用
		webSocketClose(e) {
			console.log(e);
			console.log("连接关闭");
		},
		// 发送心跳
		sendHeatBeat() {
			console.log("发送心跳");
			// 发给后台的
			var chatMsg = this.chactMsg(this.userId, "server", "Bong", null);
			var dataContent = this.dataContent(
				this.CHAT_TYPE.KEEPALIVE,
				chatMsg,
				null
			);
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

		// 保持心跳
		keepAlive() {
			setInterval(this.sendHeatBeat, 50000);
		}
	}
};
</script>
