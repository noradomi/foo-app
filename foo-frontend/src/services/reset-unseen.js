import grpcApi from './grpcApi';

export function resetUnseen(userId) {
	return new Promise((resolve, reject) => {
		grpcApi.resetUnseen(userId, (err, response) => {
			if (err) reject(err);
			else {
				resolve();
			}
		});
	});
}
