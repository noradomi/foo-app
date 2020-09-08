import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';

let initialState = {
	activeTabKey: "1",
	user: {
		jwt: getJwtFromStorage(),
		userId: getUserIdFromStorage()
	},
	userList: [],
	currSelectedUser: null,
	currentSessionId: null,
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
	scrollFlag: true,
	notifyFlag: false
};

export default function appReducer(state = initialState, action) {
	let data = action.data;
	switch (action.type) {
		case 'LOGIN_SUCCEEDED':
			state = loginSucceeded(state, data);
			break;
		case 'LOGOUT':
			state = signOut(state);
			break;
		case 'USERLIST_FETCHED':
			state = userListFetched(state, data);
			break;
		case 'CURRENT_SESSIONID':
			return {
				...state,
				currentSessionId: data
			};
		case 'SEND_MESSAGE':
			state = sendMessage(state, data);
			break;
		case 'FETCH_MESSAGE':
			state = receiveMessage(state, data);
			break;
		case 'WS_CONNECTED':
			state = handleWsConnected(state, data);
			break;
		case 'FETCH_MESSAGE_LIST':
			state = fetchMessageList(state, data);
			break;
		case 'ONLINE_OFFLINE':
			state = handleUserStatus(state,data);
			break;
		default:
			break;
	}
	return state;
}

function loginSucceeded(state, data) {
	return Object.assign({}, state, {
		user: { jwt: data.jwt, userId: data.userId }
	});
}

function signOut(state) {
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
		currentSessionId: null,
		webSocket: {
			webSocket: null,
			send: null
		},
		chatMessagesHolder: {
			chatMessages: new Map()
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

function handleWsConnected(state, data) {
	let newState = Object.assign({}, state, {
		webSocket: {
			webSocket: data.webSocket,
			send: data.send
		}
	});
	return newState;
}

function fetchMessageList(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.friendId);
	chatMessages.set(data.friendId, [ ...data.items, ...listMessage ]);
	return Object.assign({}, state, {
		chatMessagesHolder: {
			chatMessages
		}
	});
}

function handleUserStatus(state,data) {
	let userMap = state.userMapHolder.userMap;
	if (userMap === null) {
		return state;
	  }

	let user = userMap.get(data.userId);
	if(user === undefined) return state;
	user.online = data.status;
	userMap.set(data.userId,user);
	return Object.assign({}, state, {
		userMapHolder: {
		  userMap
		}
	  });
}
