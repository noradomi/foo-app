import axios from 'axios';
import { getJwtFromStorage, isEmptyString } from '../utils/utils';

export const host = 'http://localhost:8055';
export const wsHost = 'ws://localhost:9009/';
const auth_type = 'Bearer';

const instance = axios.create({
	baseURL: host
});

export const api = {
	post: (url, req) => {
		return instance.post(`${url}`, req);
	},
	authGet: (url) => {
		var jwt = getJwtFromStorage();
		if (!isEmptyString(jwt)) {
			jwt = auth_type + ' ' + jwt;
		} else {
			jwt = '';
		}
		return instance.get(`${url}`, { headers: { Authorization: jwt } });
	},
	authPost: (url, req) => {
		var jwt = getJwtFromStorage();
		if (!isEmptyString(jwt)) {
			jwt = auth_type + ' ' + jwt;
		} else {
			jwt = '';
		}
		return instance.post(`${url}`, req, { headers: { Authorization: jwt } });
	}
};
