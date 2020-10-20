import { getJwtFromStorage, getUserIdFromStorage, getUserFullNameToStorage } from '../utils/utils';

let initialState = {
	activeTabKey: '1',
	unseenChat: new Set(),
	unseenTransfer: sessionStorage.getItem('unseenTransfer') === 'true',
	user: {
		jwt: getJwtFromStorage(),
		userId: getUserIdFromStorage(),
		name: getUserFullNameToStorage()
	},
	wallet: {
		balance: 0,
		lastUpdated: 0
	},
	transactionHistory: [],
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
	scrollFlag: true,
	currentStep: null,
	stepFormData: {
		amount: null,
		description: null
	},
	transferCodeResponse: null
};

export default function appReducer(state = initialState, action) {
	let data = action.data;
	switch (action.type) {
		case 'SAVE_CURRENT_STEP':
			return Object.assign({}, state, {
				currentStep: data.payload
			});
		case 'SAVE_STEP_FORM_DATA':
			return Object.assign({}, state, {
				stepFormData: {
					amount: data.amount,
					description: data.description
				}
			});
		case 'SAVE_TRANSFER_CODE_RESPONSE':
			return Object.assign({}, state, {
				transferCodeResponse: data.payload
			});
		case 'RESET_STEP_FORM':
			return Object.assign({}, state, {
				currentStep: null,
				stepFormData: {
					amount: null,
					description: null
				},
				transferCodeResponse: null
			});
		case 'SET_ACTIVE_TAB_KEY':
			return Object.assign({}, state, {
				activeTabKey: data.key,
				transactionHistory: []
			});
		case 'SET_NEW_UNSEEN_CHAT_USER': {
			let unseenChat = new Set();
			state.unseenChat.forEach((userId) => unseenChat.add(userId));
			unseenChat.add(data.userId);
			return Object.assign({}, state, {
				unseenChat
			});
		}
		case 'UPDATE_UNEEN_CHAT_USERS': {
			let unseenChat = new Set();
			state.unseenChat.forEach((userId) => unseenChat.add(userId));
			if (unseenChat.has(data.userId)) {
				unseenChat.delete(data.userId);
			}
			return Object.assign({}, state, {
				unseenChat
			});
		}

		case 'SET_HAVING_UNSEEN_TRANSFER': {
			sessionStorage.setItem('unseenTransfer', data.status);
			return Object.assign({}, state, {
				unseenTransfer: data.status
			});
		}
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
		case 'UPDATE_LAST_MESSAGE':
			state = updateLastMessage(state, data);
			break;
		case 'SET_UNSEEN_MESSAGES':
			state = setUnseenMessages(state, data);
			break;
		case 'ADD_NEW_FRIEND':
			state = addNewFriend(state, data);
			break;
		case 'SET_WALLET':
			state = setWallet(state, data);
			break;

		case 'LOAD_TRANSACTION_HISTORY':
			state = loadTransactionHistory(state, data);
			break;

		case 'APPEND_TRANSACTION_HISTORY':
			state = appendTransactionHistory(state, data);
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
		...initialState
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
	return Object.assign({}, state, {
		userList
	});
}

function friendListFetched(state, friendList) {
	let userMap = new Map();
	let unseenChat = new Set();
	let chatMessages = state.chatMessagesHolder.chatMessages;
	for (let user of friendList) {
		userMap.set(user.userId, user);
		if (!chatMessages.has(user.userId)) {
			chatMessages.set(user.userId, []);
		}
		if (user.unreadMessages > 0) {
			unseenChat.add(user.userId);
		}
	}

	return Object.assign({}, state, {
		unseenChat,
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

function updateLastMessage(state, data) {
	let userMap = state.userMapHolder.userMap;
	if (userMap === null) {
		return state;
	}

	let user = userMap.get(data.userId);
	if (user === undefined) return state;
	user.lastMessage = data.message;
	userMap.set(data.userId, user);
	return Object.assign({}, state, {
		userMapHolder: {
			userMap
		}
	});
}

function setUnseenMessages(state, data) {
	let userMap = state.userMapHolder.userMap;
	if (userMap === null) {
		return state;
	}

	let user = userMap.get(data.userId);
	if (user === undefined) return state;
	const value = user.unreadMessages;
	user.unreadMessages = data.type === 0 ? 0 : value + 1;
	userMap.set(data.userId, user);
	return Object.assign({}, state, {
		userMapHolder: {
			userMap
		}
	});
}

function addNewFriend(state, data) {
	const newFriend = data.newFriend;
	let friendList = state.friendList;
	friendList.push(newFriend);
	let userMap = state.userMapHolder.userMap;
	let chatMessages = state.chatMessagesHolder.chatMessages;
	userMap.set(newFriend.userId, newFriend);
	if (!chatMessages.has(newFriend.userId)) {
		chatMessages.set(newFriend.userId, []);
	}
	return Object.assign({}, state, {
		friendList: friendList,
		userMapHolder: {
			userMap
		},
		chatMessagesHolder: {
			chatMessages
		}
	});
}

function setWallet(state, data) {
	return Object.assign({}, state, {
		wallet: {
			balance: data.balance,
			lastUpdated: data.lastUpdated
		}
	});
}

function loadTransactionHistory(state, data) {
	let transactionHistory = state.transactionHistory;
	const newTransactionHistory = [ ...transactionHistory, ...data.items ];
	return Object.assign({}, state, {
		transactionHistory: newTransactionHistory
	});
}

function appendTransactionHistory(state, data) {
	return Object.assign({}, state, {
		transactionHistory: [ data.item, ...state.transactionHistory ]
	});
}
