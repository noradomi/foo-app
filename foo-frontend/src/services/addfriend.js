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
				// store.dispatch(addNewFriendAction(newFriend));
				resolve();
			} else {
				reject();
			}
		});
	});
}
