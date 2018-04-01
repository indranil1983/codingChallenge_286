package com.n26.exceptions;


public class InvalidTimestampException extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTimestampException(String message) {
        super(message);
    }
}
