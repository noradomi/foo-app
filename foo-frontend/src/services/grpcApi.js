import { FintechServiceClient } from '../grpc/fintech_grpc_web_pb.js';
import { GetBalanceRequest } from '../grpc/fintech_pb.js';
import { getJwtFromStorage } from '../utils/utils.js';

const URL = 'http://' + window.location.hostname + ':8080';

const client = new FintechServiceClient(URL, null, null);

export default {
	getBalance: (name, callback) => {
		const jwtToken = getJwtFromStorage();

		console.log('Prepare token: ' + jwtToken);

		const metadata = { jwt: jwtToken };
		const getBalanceRequest = new GetBalanceRequest();
		client.getBalance(getBalanceRequest, metadata, callback);
	}
};
