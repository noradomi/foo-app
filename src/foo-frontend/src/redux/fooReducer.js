import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';

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
	}
};

export default function appReducer(state = initialState, action) {
	let data = action.data;
	switch (action.type) {
		case 'LOGIN_SUCCEEDED':
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
			state = userListFetched(state, data);
			break;
		case 'CURRENT_SESSIONID':
			return {
				...state,
				currentSessionId: data
			};
		case 'WS_CONNECTED':
			state = handleWsConnected(state,data);
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

// >>>
function userListFetched(state, userList) {
	let userMap = new Map();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of userList) {
	  userMap.set(user.id, user);
	  if (!chatMessages.has(user.id)) {
		chatMessages.set(user.id, []);
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
function receiveMessage(state,data){
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.sender_id);
	listMessage.push(data); // <<<
	return Object.assign({},state, {
		chatMessagesHolder: {chatMessages}
	});
}

// >>> 
function sendMessage(state,data){
	let chatMessages = state.chatMessagesHolder.chatMessages;
	let listMessage = chatMessages.get(data.sender_id);
	listMessage.push(data); // <<<
	return Object.assign({},state, {
		chatMessagesHolder: {chatMessages}
	});
}

function handleWsConnected(state, data) {
	console.log('ws connect', data);
	let newState = Object.assign({}, state, {
	  webSocket: {
		webSocket: data.webSocket,
		send: data.send
	  }
	});
	console.log(newState);
	return newState;
  }