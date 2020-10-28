import { loadUserListAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { api } from './api';
import { processUsernameForAvatar } from '../utils/utils';

export function getNotFriendList(offset) {
	return new Promise((resolve, reject) => {
		api
			.authGet(`/api/protected/users/${offset}`, null)
			.then((response) => {
				let items = response.data.data.items;
				// const userMapHolder = store.getState().userMapHolder;
				let result = [];
				items.forEach((item) => {
					var userItem = {
						userId: item.userId,
						name: item.name,
						avatar: processUsernameForAvatar(item.name),
						online: item.online
					};
					result.push(userItem);
				});

				if (result.length > 0) {
					// Sap xep danh sach user theo tu tu dien theo ten.
					result.sort(function(a, b) {
						if (a.name < b.name) return -1;
						if (a.name > b.name) return 1;
						return 0;
					});

					store.dispatch(loadUserListAction(result));
				}

				resolve(result.length);
			})
			.catch((reason) => {
				reject('Fetch user list failed');
			});
	});
}
