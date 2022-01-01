package com.learning.exception.exceptions;

public class UserNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7312741677246882821L;

	public UserNotFoundException(String message) {
        super(message);
    }
}
