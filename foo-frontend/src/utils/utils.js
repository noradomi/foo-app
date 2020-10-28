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
	if (username === undefined || username === null) return 'P T';
	else {
		let words = username.split(' ');
		const length = words.length;
		if (length >= 2) return words[length - 2][0] + ' ' + words[length - 1][0];
		return words[1][0];
	}
}
