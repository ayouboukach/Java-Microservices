package com.learning.exception.exceptions;

public class EmailExistException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1514887432375678247L;
	public EmailExistException(String message) {
        super(message);
    }
}
