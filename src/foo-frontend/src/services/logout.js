import { logout } from '../redux/fooAction';
import store from '../redux/fooStore';
import {clearStorage} from '../utils/utils';
import { api } from './api';

export function hanldeLogout() {
	return new Promise((resolve, reject) => {
		api
			.authPost('/api/protected/logout', null)
			.then((response) => {
				store.dispatch(logout());
                clearStorage();
				resolve();
			})
			.catch((reason) => {
				reject("Log out failed");
			});
	});
}