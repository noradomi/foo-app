import store from '../store/fooStore';
import { api } from './api';

export function hanldeLogout() {
	return new Promise((resolve, reject) => {
		api
			.authPost('/api/protected/logout', null)
			.then((response) => {
				resolve();
			})
			.catch((reason) => {
				reject('Log out failed');
			});
	});
}

export function closeWebSocket() {
	if (store.getState().webSocket.webSocket !== null) {
		store.getState().webSocket.webSocket.close();
	}
}
