import { logout } from '../redux/fooAction';
import store from '../redux/fooStore';
import {clearStorage} from '../utils/utils';
import { api } from './api';

export function hanldeLogout() {
	return new Promise((resolve, reject) => {
		api
			.authPost('/api/protected/signout', null)
			.then((response) => {
				console.log('Sign out successfully');
				// let data = response.data.data;
				store.dispatch(logout());
				// setJwtToStorage(data.token);
                // setUserIdToStorage(data.userId);
                clearStorage();
				resolve();
			})
			.catch((reason) => {
				console.log(reason);
				reject("Failed");
			});
	});
}