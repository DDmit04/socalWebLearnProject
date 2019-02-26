function validateRegistration() {
	var username = document.getElementById("username");
	var password = document.getElementById("password");
	var retypePassword = document.getElementById("retypePassword");
	if (!username.value || !password.value || password.value != retypePassword.value) {
		if(password != retypePassword) {
			retypePassword.classList.add('is-invalid');
			var retypePasswordError = document.getElementById("retypePasswordError");
			retypePasswordError.innerHTML = 'passwords are different!';
		}
		if (!username.value) {
			username.classList.add('is-invalid');
			var usernameError = document.getElementById("usernameError");
			usernameError.innerHTML = 'username can not be empty!';
		}
		if (!password.value) {
			password.classList.add('is-invalid');
			var passwordError = document.getElementById("passwordError");
			passwordError.innerHTML = 'password can not be empty!';
		}
		return false;
	}
	return true;
}

function validateLogin() {
	if (window.location.href.substring(window.location.href.length - 5, window.location.href.length) == 'error') {
		var password = document.getElementById("password");
		password.classList.add('is-invalid');
		var username = document.getElementById("username");
		username.classList.add('is-invalid');
		var accesError = document.getElementById("usernameError");
		accesError.innerHTML = 'wrong username or password!';
		return false;
	}
	return true;
}

function disposeAlert(elementName) {
	var element = document.getElementById(elementName);
	var elementError = element.nextSibling;
	elementError.innerHTML = '';
	element.classList.remove('is-invalid');
	event.preventDefault();
}