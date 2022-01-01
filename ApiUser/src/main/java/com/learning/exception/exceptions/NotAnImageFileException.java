package com.learning.exception.exceptions;

public class NotAnImageFileException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3141868918293314230L;

	public NotAnImageFileException(String message) {
        super(message);
    }
}
