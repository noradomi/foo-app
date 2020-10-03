import { getJwtFromStorage, getUserIdFromStorage, getUserFullNameToStorage } from '../utils/utils';

let initialState = {
	activeTabKey: '1',
	user: {
		jwt: getJwtFromStorage(),
		userId: getUserIdFromStorage(),
		name: getUserFullNameToStorage()
	},
	userList: [],
	friendList: [],
	selectedUser: {
		id: null,
		name: null,
		avatar: null
	},
	userMapHolder: {
		userMap: new Map()
	},
	webSocket: {
		webSocket: null,
		send: null
	},
	chatMessagesHolder: {
		chatMessages: new Map()
	},
	scrollFlag: true
};

export default function appReducer(state = initialState, action) {
	let data = action.data;
	switch (action.type) {
		case 'USER_LOGIN_SUCCEEDED':
			state = loginSucceeded(state, data);
			break;
		case 'USER_LOGOUT':
			state = logOut(state);
			break;
		case 'USERLIST_FETCHED':
			state = userListFetched(state, data);
			break;
		case 'FRIENDLIST_FETCHED':
			state = friendListFetched(state, data);
			break;
		case 'USER_SELECTED':
			state = setSelectedUser(state, data);
			break;
		case 'SEND_MESSAGE':
			state = sendMessage(state, data);
			break;
		case 'RECEIVE_MESSAGE':
			state = receiveMessage(state, data);
			break;
		case 'WEBSOCKET_FETCHED':
			state = setWebSocket(state, data);
			break;
		case 'MESSAGE_LIST_FETCHED':
			state = messageListFetched(state, data);
			break;
		case 'CHANGE_STATUS':
			state = changeUserStatus(state, data);
			break;
		default:
			break;
	}
	return state;
}

function loginSucceeded(state, data) {
	return Object.assign({}, state, {
		user: { jwt: data.jwt, userId: data.userId, name: data.name }
	});
}

function logOut(state) {
	if (state.webSocket.webSocket !== null) {
		state.webSocket.webSocket.close();
	}
	return Object.assign({}, state, {
		user: {
			jwt: null,
			userId: null
		},
		userList: [],
		userMapHolder: {
			userMap: new Map()
		},
		selectedUser: {
			id: null,
			name: null,
			avatar: null
		},
		webSocket: {
			webSocket: null,
			send: null
		},
		chatMessagesHolder: {
			chatMessages: new Map()
		}
	});
}

function setSelectedUser(state, user) {
	return Object.assign({}, state, {
		selectedUser: {
			id: user.id,
			name: user.name,
			avatar: user.avatar
		}
	});
}

function userListFetched(state, userList) {
	let userMap = new Map();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of userList) {
		userMap.set(user.userId, user);
		if (!chatMessages.has(user.userId)) {
			chatMessages.set(user.userId, []);
		}
	}
	return Object.assign({}, state, {
		userList,
		userMapHolder: {
			userMap
		},
		chatMessagesHolder: {
			chatMessages
		}
	});
}

function friendListFetched(state, friendList) {
	let userMap = new Map();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of friendList) {
		userMap.set(user.userId, user);
		if (!chatMessages.has(user.userId)) {
			chatMessages.set(user.userId, []);
		}
	}
	return Object.assign({}, state, {
		friendList,
		userMapHolder: {
			userMap
		},
		chatMessagesHolder: {
			chatMessages
		}
	});
}

function receiveMessage(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.senderId);
	listMessage.push(data);
	return Object.assign({}, state, {
		chatMessagesHolder: { chatMessages },
		scrollFlag: !state.scrollFlag
	});
}

function sendMessage(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.receiverId);
	listMessage.push(data);
	return Object.assign({}, state, {
		chatMessagesHolder: { chatMessages },
		scrollFlag: !state.scrollFlag
	});
}

function setWebSocket(state, data) {
	let newState = Object.assign({}, state, {
		webSocket: {
			webSocket: data.webSocket,
			send: data.send
		}
	});
	return newState;
}

function messageListFetched(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.friendId);
	chatMessages.set(data.friendId, [ ...data.items, ...listMessage ]);
	return Object.assign({}, state, {
		chatMessagesHolder: {
			chatMessages
		}
	});
}

function changeUserStatus(state, data) {
	let userMap = state.userMapHolder.userMap;
	if (userMap === null) {
		return state;
	}

	let user = userMap.get(data.userId);
	if (user === undefined) return state;
	user.online = data.status;
	userMap.set(data.userId, user);
	return Object.assign({}, state, {
		userMapHolder: {
			userMap
		}
	});
}
