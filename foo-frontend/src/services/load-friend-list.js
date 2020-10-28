import grpcApi from './grpcApi';
import { loadFriendListAction, setSelectedUserAction } from '../actions/fooAction';
import store from '../store/fooStore';
import { processUsernameForAvatar } from '../utils/utils';

export function getFriendList() {
	return new Promise((resolve, reject) => {
		grpcApi.getFriendList((err, response) => {
			const dataResponse = response;
			const items = dataResponse.getData().getItemsList();
			let result = [];
			items.forEach((item) => {
				let userItem = {
					userId: item.getUserId(),
					name: item.getName(),
					avatar: processUsernameForAvatar(item.getName()),
					unreadMessages: item.getUnreadMessages(),
					lastMessage: item.getLastMessage(),
					online: item.getIsOnline()
				};
				result.push(userItem);
			});
			if (result.length > 0) {
				// Sap xep danh sach friends theo tu tu dien theo ten.
				result.sort(function(a, b) {
					if (a.unreadMessages < b.unreadMessages) return 1;
					if (a.unreadMessages > b.unreadMessages) return -1;
					return 0;
				});

				store.dispatch(loadFriendListAction(result));
				const initSelectedUser = {
					id: result[0].userId,
					name: result[0].name,
					avatar: result[0].avatar
				};
				store.dispatch(setSelectedUserAction(initSelectedUser));
			}
			resolve(result);
		});
	});
}
