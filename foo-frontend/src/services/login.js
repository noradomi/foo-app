import { loginSuccessAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { setJwtToStorage, setUserIdToStorage, setUserFullNameToStorage } from '../utils/utils';
import { api } from './api';

export function hanleLogin(username, password) {
	return new Promise((resolve, reject) => {
		api
			.post('/api/public/login', {
				username,
				password
			})
			.then((response) => {
				let data = response.data.data;
				store.dispatch(loginSuccessAction(data.jwtToken, data.userId, data.name));
				setJwtToStorage(data.token);
				setUserIdToStorage(data.userId);
				setUserFullNameToStorage(data.name);
				resolve(data);
			})
			.catch((reason) => {
				reject('Login failed');
			});
	});
}
