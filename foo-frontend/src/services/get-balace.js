import { setWalletAction } from '../actions/fooAction';
import store from '../store/fooStore';
import grpcApi from './grpcApi';

export function getBalace() {
	return new Promise((resolve, reject) => {
		grpcApi.getBalance((err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				// Ok
				const dataResponse = response.getData();
				const balance = dataResponse.getBalance();
				const lastUpdated = dataResponse.getLastUpdated();
				store.dispatch(setWalletAction(balance, lastUpdated));
				resolve();
			} else {
				reject('Get balance failed');
			}
		});
	});
}
