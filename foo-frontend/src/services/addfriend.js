import { addNewFriendAction } from '../actions/fooAction';
import store from '../store/fooStore';
import grpcApi from './grpcApi';
import { processUsernameForAvatar } from '../utils/utils';

export function doAddFriend(userId) {
	return new Promise((resolve, reject) => {
		grpcApi.addFriend(userId, (err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				const newFriend = response.getData().getUser();
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
