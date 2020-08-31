import { api } from './api';
import { updateCurrSessionId,fetchUserList } from '../redux/fooAction';
import store from '../redux/fooStore';


export function getUserList() {
	return new Promise((resolve, reject) => {
		api
			.authGet('/api/protected/userlist', null)
			.then((response) => {
				console.log('Get User list successfully');
				let items = response.data.data.items;
				let result = [];

				items.forEach((item) => {
					var userItem = {
						userId: item.userId,
						name: item.name,
						avatar: processUsernameForAvatar(item.name)
					};
					result.push(userItem);
				});

				// Sap xep danh sach user theo tu tu dien theo ten.
				result.sort(function(a, b) {
					if (a.name < b.name) return -1;
					if (a.name > b.name) return 1;
					return 0;
                });

                store.dispatch(fetchUserList(result));
                store.dispatch(updateCurrSessionId(result[0].userId));
				
				resolve(data);
			})
			.catch((reason) => {
				console.log(reason);
				reject('Login failed');
			});
	});
}

function processUsernameForAvatar(username) {
	var x1 = username.charAt(0);
	var x2 = username.charAt(1);
	return x1 + ' ' + x2;
}
