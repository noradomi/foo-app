import grpcApi from './grpcApi';
import { loadFriendListAction, setSelectedUserAction } from '../actions/fooAction';
import store from '../store/fooStore';

export function resetUnseen(userId) {
	return new Promise((resolve, reject) => {
		grpcApi.resetUnseen(userId, (err, response) => {
			if (err) reject(err);
			else {
				console.log('Reset unseen response: ' + response.getStatus().getCode());
				resolve();
			}
		});
	});
}
