package org.howard.portal.kit.gui.util;

import org.eclipse.swt.SWT;

/**
 * The purpose of this class is to provide menu types
 */
public enum MenuType {
	/**
	 * Style constant for line separator behavior (value is 1&lt;&lt;1).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Label</code></li>
	 * <li><code>MenuItem</code></li>
	 * <li><code>ToolItem</code></li>
	 * </ul>
	 * </p>
	 */
	SEPARATOR(SWT.SEPARATOR), /**
	 * /** Style constant for push button behavior
	 * (value is 1&lt;&lt;3).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Button</code></li>
	 * <li><code>MenuItem</code></li>
	 * <li><code>ToolItem</code></li>
	 * </ul>
	 * </p>
	 */
	PUSH(SWT.PUSH), /**
	 * /** Style constant for radio button behavior (value is
	 * 1&lt;&lt;4).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Button</code></li>
	 * <li><code>MenuItem</code></li>
	 * <li><code>ToolItem</code></li>
	 * </ul>
	 * </p>
	 */
	RADIO(SWT.RADIO), /**
	 * /** Style constant for check box behavior (value is
	 * 1&lt;&lt;5).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Button</code></li>
	 * <li><code>MenuItem</code></li>
	 * <li><code>ToolItem</code></li>
	 * <li><code>Table</code></li>
	 * <li><code>Tree</code></li>
	 * </ul>
	 * </p>
	 */
	CHECK(SWT.CHECK), /**
	 * /** Style constant for cascade behavior (value is
	 * 1&lt;&lt;6).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>MenuItem</code></li>
	 * </ul>
	 * </p>
	 */
	CASCADE(SWT.CASCADE),
	/**
	 * Style constant for menu bar behavior (value is 1&lt;&lt;1).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Menu</code></li>
	 * </ul>
	 * </p>
	 */
	BAR(SWT.BAR);
	private int type;

	private MenuType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}
}
