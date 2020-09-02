import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';
import { ConsoleSqlOutlined } from '@ant-design/icons';

let initialState = {
	user: {
		jwt: getJwtFromStorage(),
		userId: getUserIdFromStorage()
	},
	userList: [],
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
	scrollFlag: true
};

export default function appReducer(state = initialState, action) {
	let data = action.data;
	switch (action.type) {
		case 'LOGIN_SUCCEEDED':
			console.log('Login action');
			state = loginSucceeded(state, data);
			break;
		case 'SIGNOUT':
			return {
				...state,
				user: {
					jwt: null,
					userId: null
				}
			};

		case 'USERLIST_FETCHED':
			console.log('userListFetched action');
			state = userListFetched(state, data);
			break;
		case 'CURRENT_SESSIONID':
			console.log('CURRENT_SESSIONID action');
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

// >>>
function userListFetched(state, userList) {
	// console.log('Size of userlist fetched: ' + userList.size);
	let userMap = new Map();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of userList) {
		// console.log('Size of userlist fetched: ' + user.userId);
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

// >>>
function receiveMessage(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.sender_id);
	listMessage.push(data); // <<<
	return Object.assign({}, state, {
		chatMessagesHolder: { chatMessages },
		scrollFlag: !state.scrollFlag
	});
}

// >>>
function sendMessage(state, data) {
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.receiver_id);
	listMessage.push(data); // <<<
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
