export function setUserIdToStorage(userId) {
	sessionStorage.setItem('userId', userId);
}

export function getUserIdFromStorage() {
	var data = sessionStorage.getItem('userId');
	return data;
}

export function setUserFullNameToStorage(name) {
	sessionStorage.setItem('name', name);
}

export function getUserFullNameToStorage() {
	var data = sessionStorage.getItem('name');
	return data;
}

export function setJwtToStorage(jwt) {
	sessionStorage.setItem('jwt', jwt);
}

export function getJwtFromStorage() {
	var data = sessionStorage.getItem('jwt');
	return data;
}

export function clearStorage() {
	sessionStorage.clear();
}

export function isAuthenticated() {
	var jwt = getJwtFromStorage();
	return isEmptyString(jwt);
}

export function isEmptyString(prop) {
	if (prop === null || prop === '') {
		return true;
	} else {
		return false;
	}
}

export function processUsernameForAvatar(username) {
	if (username === undefined) return 'P T';
	else {
		var x1 = username.charAt(0);
		var x2 = username.charAt(1);
		return x1 + ' ' + x2;
	}
}
