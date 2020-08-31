/*
 * @Author: mikey.zhaopeng 
 * @Date: 2020-08-31 14:38:22 
 * @Last Modified by:   mikey.zhaopeng 
 * @Last Modified time: 2020-08-31 14:38:22 
 */
import axios from 'axios';
import {getJwtFromStorage,isEmptyString} from "../utils/utils";

export const host = 'http://localhost:8055';
export const ws_host = 'ws://localhost:9009/';
const auth_type = 'Bearer';

const instance = axios.create({
  baseURL: host
});

export const api = {

  get: (url) => {
    var jwt = getJwtFromStorage();
    if (!isEmptyString(jwt)) {
      jwt = auth_type + ' ' + jwt;
    } else {
      jwt = '';
    };
    return instance.get(`${url}`, {headers: {'Authorization': jwt}});
  },
  post: (url, req) => {
    var jwt = getJwtFromStorage();
    if (!isEmptyString(jwt)) {
      jwt = auth_type + ' ' + jwt;
    } else {
      jwt = '';
    };
    return instance.post(`${url}`, req, {headers: {'Authorization': jwt}});
  }
}
