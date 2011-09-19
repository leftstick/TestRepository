package org.nanfeng.exception;

public class BeanErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeanErrorException() {
		super();
	}

	public BeanErrorException(String msg) {
		super(msg);
	}

	public BeanErrorException(String msg, Throwable e) {
		super(msg, e);
	}
}
