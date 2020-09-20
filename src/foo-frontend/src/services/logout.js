import { logOutAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { clearStorage } from '../utils/utils';
import { api } from './api';

export function hanldeLogout() {
	return new Promise((resolve, reject) => {
		api
			.authPost('/api/protected/logout', null)
			.then((response) => {
				store.dispatch(logOutAction());
				clearStorage();
				resolve();
			})
			.catch((reason) => {
				reject('Log out failed');
			});
	});
}
