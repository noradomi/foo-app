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
	console.log('Action: load messages');
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
