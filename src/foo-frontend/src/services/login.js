import { api } from './api';
import { login } from '../redux/fooAction';
import { store } from '../redux/fooStore';
import {setJwtToStorage, setUserIdToStorage} from "../utils/utils";

export function hanleLogin(username, passwor) {
	return new Promise((resolve, reject) => {
		api.post('/api/public/login', {
			username,
			password
		}).then(response => {
            console.log("Login successfully");
            let data = response.data.data;
            store.dispatch(login(data.jwtToken, data.userId));
            setJwtToStorage(data.token);
            setUserIdToStorage(data.userId);
            resolve(data);
        }).catch(reason => {
      console.log(reason);
      reject('Login failed');
    });
	});
}
