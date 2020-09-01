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
	}
};

export default function appReducer(state = initialState, action) {

	console.log("Size user map: "+state.userMapHolder.userMap.size);
	let data = action.data;
	switch (action.type) {
		case 'LOGIN_SUCCEEDED':
			console.log("Login action");	
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
			console.log("userListFetched action");
			state = userListFetched(state, data);
			break;
		case 'CURRENT_SESSIONID':
			console.log("CURRENT_SESSIONID action");
			return {
				...state,
				currentSessionId: data
			};
		case 'WS_CONNECTED':
			console.log("WS_CONNECTED action");
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
	console.log("Size of userlist fetched: "+userList.size);
	let userMap = new Map();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of userList) {
		console.log("Size of userlist fetched: "+user.userId);
	  userMap.set(user.userId, user);
	  if (!chatMessages.has(user.userId)) {
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