import { func } from "prop-types";

export function login(jwt, userId) {
	return {
		type: 'LOGIN_SUCCEEDED',
		data: {
			jwt,
			userId
		}
	};
}
export function logout() {
	return {
		type: 'SIGNOUT'
	};
}
export function fetchUserList(userList){
	return {
		type: 'USERLIST_FETCHED',
		data: userList
	}
}

export function updateCurrSessionId(userId){
	return {
		type: 'CURRENT_SESSIONID',
		data: userId
	}
}

export function webSocketConnected(webSocket, send) {
	return {
	  type: 'WS_CONNECTED',
	  data: {
		webSocket,
		send
	  }
	}
  }
  
  export function receiveMessage(message) {
	return {
	  type: 'CHAT_RECEIVE',
	  data: message
	}
  }
  
  export function sendbackMessage(message) {
	return {
	  type: 'CHAT_SENDBACK',
	  data: message
	}
  }