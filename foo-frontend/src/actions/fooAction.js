export function loginSuccessAction(jwt, userId, name) {
	return {
		type: 'USER_LOGIN_SUCCEEDED',
		data: {
			jwt,
			userId,
			name
		}
	};
}
export function logOutAction() {
	return {
		type: 'USER_LOGOUT'
	};
}
export function loadUserListAction(userList) {
	return {
		type: 'USERLIST_FETCHED',
		data: userList
	};
}

export function loadFriendListAction(friendList) {
	return {
		type: 'FRIENDLIST_FETCHED',
		data: friendList
	};
}

export function setSelectedUserAction(user) {
	return {
		type: 'USER_SELECTED',
		data: user
	};
}

export function setWebSocketAction(webSocket, send) {
	return {
		type: 'WEBSOCKET_FETCHED',
		data: {
			webSocket,
			send
		}
	};
}

export function receiveMessageAction(message) {
	return {
		type: 'RECEIVE_MESSAGE',
		data: message
	};
}

export function sendMessageAction(message) {
	return {
		type: 'SEND_MESSAGE',
		data: message
	};
}

export function loadMessageListAction(data, friendId) {
	data.friendId = friendId;
	return {
		type: 'MESSAGE_LIST_FETCHED',
		data: data
	};
}

export function setUserStatusAction(userId, status) {
	return {
		type: 'CHANGE_STATUS',
		data: {
			userId,
			status
		}
	};
}

export function updateLastMessage(userId, message) {
	return {
		type: 'UPDATE_LAST_MESSAGE',
		data: {
			userId,
			message
		}
	};
}

export function setUnseenMessages(userId, type) {
	return {
		type: 'SET_UNSEEN_MESSAGES',
		data: {
			userId,
			type
		}
	};
}

export function addNewFriendAction(newFriend) {
	return {
		type: 'ADD_NEW_FRIEND',
		data: {
			newFriend
		}
	};
}

export function setWalletAction(balance, lastUpdated) {
	return {
		type: 'SET_WALLET',
		data: {
			balance,
			lastUpdated
		}
	};
}

export function loadTranctionHistoryAction(items) {
	return {
		type: 'LOAD_TRANSACTION_HISTORY',
		data: {
			items
		}
	};
}

export function appendTranctionHistoryAction(item) {
	return {
		type: 'APPEND_TRANSACTION_HISTORY',
		data: {
			item
		}
	};
}
export function setActiveTabKeyAction(key) {
	return {
		type: 'SET_ACTIVE_TAB_KEY',
		data: {
			key
		}
	};
}

export function setNewUnseenChatUserAction(userId) {
	return {
		type: 'SET_NEW_UNSEEN_CHAT_USER',
		data: { userId }
	};
}

export function updateUnseenChatUsersAction(userId) {
	return {
		type: 'UPDATE_UNEEN_CHAT_USERS',
		data: { userId }
	};
}

export function setHavingUnseenTransferAction(status) {
	return {
		type: 'SET_HAVING_UNSEEN_TRANSFER',
		data: { status }
	};
}
