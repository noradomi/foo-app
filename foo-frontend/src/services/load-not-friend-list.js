import { loadUserListAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { api } from './api';

export function getNotFriendList() {
	return new Promise((resolve, reject) => {
		api
			.authGet('/api/protected/users', null)
			.then((response) => {
				console.log('Get all user list');
				let items = response.data.data.items;
				const userMapHolder = store.getState().userMapHolder;
				let result = [];
				items.forEach((item) => {
					if (userMapHolder.userMap.get(item.userId) === undefined) {
						var userItem = {
							userId: item.userId,
							name: item.name,
							avatar: processUsernameForAvatar(item.name),
							online: item.online
						};
						result.push(userItem);
					}
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

				resolve(result);
			})
			.catch((reason) => {
				console.log(reason);
				reject('Fetch user list failed');
			});
	});
}

function processUsernameForAvatar(username) {
	var x1 = username.charAt(0);
	var x2 = username.charAt(1);
	return x1 + ' ' + x2;
}
