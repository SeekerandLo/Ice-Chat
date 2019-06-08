export function CHAT_TYPE () {
  var CHAT_TYPE = {
    CONNECT: 1,
    CHAT: 2,
    SIGNED: 3,
    KEEPALIVE: 4,
    FRIEND_REQUEST: 5
  }
  return CHAT_TYPE
}

export function REQUEST_ACTION () {
  var REQUEST_ACTION = {
    AGREE: 1,
    REFUSE: 2,
    IGNORE: 3
  }
  return REQUEST_ACTION
}
