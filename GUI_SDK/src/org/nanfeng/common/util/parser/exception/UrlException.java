package org.nanfeng.common.util.parser.exception;

public class UrlException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1401457282526527531L;

	public UrlException(String msg) {
		super(msg);
	}

	public UrlException(Throwable t) {
		super(t);
	}

	public UrlException(String msg, Throwable t) {
		super(msg, t);
	}
}
