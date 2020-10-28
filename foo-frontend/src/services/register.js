import { api } from './api';

export function handleRegister(username,name, password) {
	return new Promise((resolve, reject) => {
		api
			.post('/api/public/signup', {
                username,
                name,
                password
			})
			.then((response) => {
				let data = response.data.data;
				resolve(data);
			})
			.catch((reason) => {
				reject("Register failed");
			});
	});
}