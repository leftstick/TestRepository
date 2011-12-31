package org.nanfeng.common.util.parser.exception;

public class XmlStructureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1401457282526527531L;

	public XmlStructureException(String msg) {
		super(msg);
	}

	public XmlStructureException(Throwable t) {
		super(t);
	}

	public XmlStructureException(String msg, Throwable t) {
		super(msg, t);
	}
}
