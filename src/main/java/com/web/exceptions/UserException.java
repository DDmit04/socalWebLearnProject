package com.web.exceptions;

import com.web.data.User;

public class UserException extends Exception {
	
	private User user;
	
	public UserException(String message, User user) {
		super(message);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
