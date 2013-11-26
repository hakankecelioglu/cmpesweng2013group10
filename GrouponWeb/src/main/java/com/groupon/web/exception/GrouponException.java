package com.groupon.web.exception;

public class GrouponException extends RuntimeException {

	private static final long serialVersionUID = -4412950336471523266L;

	public GrouponException() {
	}

	public GrouponException(String message) {
		super(message);
	}

	public GrouponException(Throwable cause) {
		super(cause);
	}

	public GrouponException(String message, Throwable cause) {
		super(message, cause);
	}

	public GrouponException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
