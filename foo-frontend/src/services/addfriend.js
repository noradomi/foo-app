import { addNewFriendAction } from '../actions/fooAction';
import store from '../store/fooStore';
import grpcApi from './grpcApi';

export function doAddFriend(userId) {
	return new Promise((resolve, reject) => {
		grpcApi.addFriend(userId, (err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				// OK
				console.log('Add friend OK');
				const newFriend = response.getData().getUser();
				console.log(newFriend);
				let userItem = {
					userId: newFriend.getUserId(),
					name: newFriend.getName(),
					avatar: processUsernameForAvatar(newFriend.getName()),
					unreadMessages: newFriend.getUnreadMessages(),
					lastMessage: newFriend.getLastMessage(),
					online: newFriend.getIsOnline()
				};
				store.dispatch(addNewFriendAction(userItem));
				resolve();
			} else {
				reject();
			}
		});
	});
}

function processUsernameForAvatar(username) {
	var x1 = username.charAt(0);
	var x2 = username.charAt(1);
	return x1 + ' ' + x2;
}
