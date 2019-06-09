## Ice-Chat 

<a href="https://github.com/d2-projects/d2-admin" target="_blank"><img src="https://raw.githubusercontent.com/FairyEver/d2-admin/master/doc/image/d2-admin@2x.png" width="200"></a>

&emsp;&emsp;为了完成网络作业写的小项目，前端以飞冰的 [D2Admin](https://github.com/d2-projects/d2-admin) 为基础，增加了聊天、好友请求、搜索好友功能，后端使用Spring Boot、Netty、MongoDB。


## TODO
- [ ] WebSocket 组件化
- [ ] 再次登录聊天消息持久化
- [ ] 离线消息保存
- [ ] 消息签收 

## 安装
- 前端
```
# 安装依赖
npm install

# 开发环境运行 localhost:8081
npm run dev

# 构建
npm run build
```
- 后端  
导入IDEA。**File** -> **New** -> **Project from Existing Source...**，选择项目的 Server 文件夹，使用 [Gradle](https://gradle.org/) 管理依赖。进入后等待下载依赖，运行项目在 8080 端口

## 前端部分

### WebSocket 管理 (待组件化)
- 项目种使用了两种 WebSocket 连接，一种是与好友聊天使用的连接，一种是接收请求的连接，因为想实现实时接收好友请求等其他请求信息，现在的做法是建立一个WebSocket 连接用来做除了聊天之外的东西，保持着与服务器的联系。

- WebSocket 创建

    - 👉[Front/src/components/chat-window/index.vue]()  
    聊天的连接写在这里，每打开一个聊天窗口都会创建一个连接

    - 👉[Front/src/layouts/HeaderAside/components/header-log/index.vue]()  
    接收请求的连接写在这里，当打开网页的时候创建，持续发送心跳请求，保持与服务器的连接

- Vuex 管理

    - 详情代码可看 👉[socket.js](https://github.com/SeekerandLo/Ice-Chat/blob/develop/Front/src/store/modules/d2admin/modules/socket.js)，使用 `vuex` 管理 WebSocket 的好处时能在不同的组件种对它进行管理，例如当打开聊天页面时创建 WebSocket 连接，在点击窗口的关闭按钮时关掉这个连接，可以在窗口按钮组件中提交一个请求给 vuex。    

### 聊天
- 自定义组件 chat-window/index.vue
  - 页面自动滚动到底部
      ```js
      updated () {
          this.$nextTick(() => {
          // 获取需要滚动的容器    
          let msgContent = document.getElementsByClassName(
              'd2-container-full__body'
          )[1]
          msgContent.scrollTop = msgContent.scrollHeight // 滚动高度
          })
      },
      ```

  - 关于如何展示一左一右的聊天消息  
  创建了两个 `class`，在渲染聊天记录时根据是本人发送的还是好友发送的赋予不用的`class`，一个左浮动一个右浮动

- 消息处理(前端)  
  - 现在的处理办法，是将用户收到的全部聊天消息存放到 vuex 中，在加载聊天记录的时候使用 `计算属性` 根据用户 id 及对面好友 id 获取记录并渲染...👉[socket.js](https://github.com/SeekerandLo/Ice-Chat/blob/develop/Front/src/store/modules/d2admin/modules/socket.js)

  - 目标改进方向：vuex 中维护一个消息的集合，以接收者的 objectId 为 key 创建以对象分类的数组，类似下面样子，这样当在查看与某个用户聊天记录的时候，在组件的`created()`中加载对应的聊天记录即可，再渲染到界面上...
      ```js
      contents:{
          "user-1-objectId":[


          ],
          "user-2-objectId":[

          ]
      }
      ```
## 后端部分

### 数据结构
- User.java 用户  

  Message.java 消息实体  

  RequestMessage.java 请求实体  
  
  MessageRecord.java 存放用户间的聊天记录  
  
  UserFriend.java 存放用户的好友们  👉[entity](https://github.com/SeekerandLo/Ice-Chat/tree/develop/Server/src/main/java/com/liy/chat/entity)

- ChannelManager.java 存放通信使用的 Channel，单对单聊天或群聊天都可以通过它来实现，设计了两种 `key` 对应前端的两种 WebSocket。例如：
  ```java
  // 下为一个Map的key和value
  
  // 接收请求使用的Channel，每当用户登录系统会创建此种类型的Channel，并前端会发送心跳保持此Channel的生命
  channel Key: RECEIVE_REQUEST-5cf6871d12941a25508f4392-server channel Value: [id: 0xf688d04f, L:/127.0.0.1:8088 - R:/127.0.0.1:57057]

  // 聊天时使用的Channel，当用户点开与好友的聊天界面时，会创建此类型的Channel，key 的命名规则为 CHAT - 发送者id - 接收者id 
  channel Key: CHAT-5cf6871d12941a25508f4392-5cf7e04eaeb1fc2018c78d21 channel Value: [id: 0xf81d3b67, L:/127.0.0.1:8088 - R:/127.0.0.1:57073]
  ```
- ChatMsg.java 消息实体，保存发送者、接收者、消息内容、消息id

  DataContent.java  前后端通信数据体，标志该消息交给谁处理  👉[pojo](https://github.com/SeekerandLo/Ice-Chat/tree/develop/Server/src/main/java/com/liy/chat/netty/pojo)

### 消息转发

- Netty 是通过重写控制器来实现通信，后端写了两个控制器控制，一个监听心跳，一个处理具体消息  

  消息类型判断及转发 👉[ChatHandler](https://github.com/SeekerandLo/Ice-Chat/blob/develop/Server/src/main/java/com/liy/chat/netty/handler/ChatHandler.java)   

  心跳 👉[HeartBeatHandler](https://github.com/SeekerandLo/Ice-Chat/blob/develop/Server/src/main/java/com/liy/chat/netty/handler/HeartBeatHandler.java)

### Channel 维护  
- ChannelManager.java 中有两个 Map，一个以上述的具有标识意义的 `行为+用户Id` 作为 key，Channel 作为 value，另一个 Map 是它的反转，以 Channel 为 key，这个存在的意义是当用户在关闭 WebSocket 时通过获取该 Channel，继而获取上述的 `行为+用户Id key` ，然后在 Map 移除
    ```java
    public static void removeInvalidChannel(Channel channel) {
        String key = reversalChannelMap.get(channel);
        channelMap.remove(key);
        reversalChannelMap.remove(channel);
    }
    ```

- 当有用户发送创建 WebSocket 连接时，识别请求的类型，是建立连接请求还是发送消息请求，组装该用户的 key，将创建的 Channel 放入 channelMap 中。
请求的类型在 👉[MsgTypeEnum](https://github.com/SeekerandLo/Ice-Chat/blob/master/Server/src/main/java/com/liy/chat/netty/pojo/MsgEnum/MsgTypeEnum.java)