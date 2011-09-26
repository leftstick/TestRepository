package org.nanfeng.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class DialogFactory {
	public static int openError(Shell parent, String message) {
		MessageDialog md = new MessageDialog(parent, ResourceUtil.instance()
				.getString("common.error"), null, message, MessageDialog.ERROR,
				new String[] { ResourceUtil.instance().getString(
						"common.button.ok") }, 0);
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

	public static int openInformation(Shell parent, String message) {
		MessageDialog md = new MessageDialog(parent, ResourceUtil.instance()
				.getString("common.information"), null, message,
				MessageDialog.INFORMATION, new String[] { ResourceUtil
						.instance().getString("common.button.ok") }, 0);
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

	public static int openConfirm(Shell parent, String message) {
		MessageDialog md = new MessageDialog(parent, ResourceUtil.instance()
				.getString("common.confirm"), null, message,
				MessageDialog.CONFIRM, new String[] {
						ResourceUtil.instance().getString("common.button.ok"),
						ResourceUtil.instance().getString(
								"common.button.cancel") }, 0);
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
