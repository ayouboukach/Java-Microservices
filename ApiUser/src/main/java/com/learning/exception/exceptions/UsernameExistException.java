package com.learning.exception.exceptions;

public class UsernameExistException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3002195068991207831L;

	public UsernameExistException(String message) {
        super(message);
    }
}
