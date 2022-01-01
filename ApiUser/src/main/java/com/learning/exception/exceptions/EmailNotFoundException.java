package com.learning.exception.exceptions;

public class EmailNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5164410263537278060L;

	public EmailNotFoundException(String message) {
        super(message);
    }
}
