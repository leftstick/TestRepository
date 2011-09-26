package org.nanfeng.exception;

public class PrimaryKeyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrimaryKeyNotFoundException() {
		super();
	}

	public PrimaryKeyNotFoundException(String msg) {
		super(msg);
	}

	public PrimaryKeyNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}
}
