package org.nanfeng.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageUtil {
	public static int openError(Shell parent, String title, String message) {
		MessageBox mb = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
		mb.setText(title);
		mb.setMessage(message);
		return mb.open();
	}

	public static int openInformation(Shell parent, String title, String message) {
		MessageBox mb = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
		mb.setText(title);
		mb.setMessage(message);
		return mb.open();
	}
}
