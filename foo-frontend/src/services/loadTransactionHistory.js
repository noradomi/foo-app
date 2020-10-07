import { setWalletAction, loadTranctionHistoryAction } from '../actions/fooAction';
import store from '../store/fooStore';
import grpcApi from './grpcApi';

export function loadTransactionHistory(pageSize, pageToken) {
	return new Promise((resolve, reject) => {
		grpcApi.getHistory(pageSize, pageToken, (err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				const history = response.getData().getItemsList();
				const nextPageToken = response.getData().getNextPageToken();

				if (nextPageToken !== 0) {
					const items = [];
					history.forEach((x) => {
						console.log(x.getUserId());
						const item = {
							userId: x.getUserId(),
							description: x.getDescription(),
							amount: x.getAmount(),
							recordedTime: x.getRecordedTime(),
							transferType: x.getTransferType()
						};
						items.push(item);
						store.dispatch(loadTranctionHistoryAction(items));
					});
				}

				resolve(nextPageToken);
			} else {
				reject('Load transaction history failed');
			}
		});
	});
}
