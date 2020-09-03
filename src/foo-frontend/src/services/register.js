import { api } from './api';

export function handleRegister(username,fullname, password) {
	return new Promise((resolve, reject) => {
		api
			.post('/api/public/signup', {
                username,
                fullname,
                password
			})
			.then((response) => {
				console.log('Register successfully');
				let data = response.data.data;
				// store.dispatch(login(data.jwtToken, data.userId));
				// setJwtToStorage(data.token);
				// setUserIdToStorage(data.userId);
				resolve(data);
			})
			.catch((reason) => {
				console.log(reason);
				reject("Register failed");
			});
	});
}