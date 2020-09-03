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
	};
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
	};
  }
  
  export function receiveMessage(message) {
	return {
	  type: 'FETCH_MESSAGE',
	  data: message
	};
  }
  
  export function sendbackMessage(message) {
	return {
	  type: 'SEND_MESSAGE',
	  data: message
	};
  }

  export function fetchMessageList(data,friendId) {
	data.friendId = friendId;
	return {
	  type: 'FETCH_MESSAGE_LIST',
	  data: data
	};
  }

  export function changeUserStatus(userId,status){
	  return {
		  type: 'ONLINE_OFFLINE',
		  data: {
			  userId,status
		  }
	  };
  }