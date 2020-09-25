import { api } from './api';
import { loginSuccessAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { setJwtToStorage, setUserIdToStorage } from '../utils/utils';
import greeterApi from './greeterApi';

export function hanleLogin(username, password) {
	greeterApi.sayHello(username, (err, response) => {
		// setContent(response.getMessage());
		console.log(response.getMessage());
	});

	return new Promise((resolve, reject) => {
		api
			.post('/api/public/login', {
				username,
				password
			})
			.then((response) => {
				let data = response.data.data;
				store.dispatch(loginSuccessAction(data.jwtToken, data.userId));
				setJwtToStorage(data.token);
				setUserIdToStorage(data.userId);
				resolve(data);
			})
			.catch((reason) => {
				reject('Login failed');
			});
	});
}
