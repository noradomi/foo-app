/*
 * @Author: mikey.zhaopeng 
 * @Date: 2020-08-31 14:52:09 
 * @Last Modified by:   mikey.zhaopeng 
 * @Last Modified time: 2020-08-31 14:52:09 
 */
import {getJwtFromStorage,getUserIdFromStorage} from "../utils/utils";


let initialState = {
    user: {
        jwt : getJwtFromStorage(),
        userId: getUserIdFromStorage()
    }
}

export default function appReducer(state = initialState,action){
    let data = action.data;
    switch (action.type){
        case 'LOGIN_SUCCESS':
            state = loginSuccess(state,data);
    }
}

function loginSuccess(state,data){
    return Object.assign({},state,{
        user: {jwt: data.jwt, userId: data.userId}
    });
}