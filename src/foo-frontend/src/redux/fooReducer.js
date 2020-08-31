import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';

let initialState = {
	user: {
		jwt: getJwtFromStorage(),
		userId: getUserIdFromStorage()
	},
	userList: [],
	currentSessionId: null
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
			break;
		case 'USERLIST_FETCHED':
			state = userListFetched(state, data);
			break;
		case 'CURRENT_SESSIONID':
			return {
				...state,
				currentSessionId: data
			};
			break;
    }
    return state;
}

function loginSucceeded(state, data) {
	return Object.assign({}, state, {
		user: { jwt: data.jwt, userId: data.userId }
	});
}

function userListFetched(state, userList) {
	return Object.assign({}, state, { userList });
}
