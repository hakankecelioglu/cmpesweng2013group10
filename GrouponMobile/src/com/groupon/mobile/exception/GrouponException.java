package com.groupon.mobile.exception;

public class GrouponException extends Exception {
	private static final long serialVersionUID = -167617430635582333L;

	public GrouponException() {
		super();
	}

	public GrouponException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public GrouponException(String detailMessage) {
		super(detailMessage);
	}

	public GrouponException(Throwable throwable) {
		super(throwable);
	}

}
