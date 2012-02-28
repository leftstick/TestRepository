package org.nanfeng.common.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogFactory {
	public static int openError(Shell parent, String title,
			String buttonOkText, String message) {
		MessageDialog md = new MessageDialog(parent, title, null, message,
				MessageDialog.ERROR, new String[] { buttonOkText }, 0);
		if (md.getShell() == null)
			md.create();
		md.getShell().setSize(300, 150);
		md.getShell().setLocation(
				(parent.getBounds().width - md.getShell().getBounds().width)
						/ 2 + parent.getBounds().x,
				(parent.getBounds().height - md.getShell().getBounds().height)
						/ 2 + parent.getBounds().y);
		return md.open();
	}

	public static int openInformation(Shell parent, String title,
			String buttonOkText, String message) {
		MessageDialog md = new MessageDialog(parent, title, null, message,
				MessageDialog.INFORMATION, new String[] { buttonOkText }, 0);
		if (md.getShell() == null)
			md.create();
		md.getShell().setSize(300, 150);
		md.getShell().setLocation(
				(parent.getBounds().width - md.getShell().getBounds().width)
						/ 2 + parent.getBounds().x,
				(parent.getBounds().height - md.getShell().getBounds().height)
						/ 2 + parent.getBounds().y);
		return md.open();
	}

	public static int openConfirm(Shell parent, String title,
			String buttonOkText, String buttonCancelText, String message) {
		MessageDialog md = new MessageDialog(parent, title, null, message,
				MessageDialog.CONFIRM, new String[] { buttonOkText,
						buttonCancelText }, 0);
		if (md.getShell() == null)
			md.create();
		md.getShell().setSize(300, 150);
		md.getShell().setLocation(
				(parent.getBounds().width - md.getShell().getBounds().width)
						/ 2 + parent.getBounds().x,
				(parent.getBounds().height - md.getShell().getBounds().height)
						/ 2 + parent.getBounds().y);
		return convert(md.open());
	}

	public static String openFileDialog(Shell parent) {
		FileDialog fd = new FileDialog(parent, SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.war" });
		return fd.open();
	}

	public static int convert(int src) {
		switch (src) {
		case 0:
			return SWT.OK;
		case 1:
			return SWT.CANCEL;
		}
		return SWT.OK;
	}
}
