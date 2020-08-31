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