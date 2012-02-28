package com.dreamworks.portal.deploy.tool.action.impl;

import org.eclipse.swt.SWT;
import org.nanfeng.common.action.AbstractAction;
import org.nanfeng.common.gui.DialogFactory;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.util.resource.Resource;

public class ExitAction extends AbstractAction {

	public ExitAction(Resource resource, String text) {
		super(resource, text);
	}

	public void run() {
		MainFrame mainFrame = getContext().getData(MainFrame.PARENT,
				MainFrame.class);
		int i = DialogFactory.openConfirm(mainFrame.getShell(),
				getResourceStr("dialog.confirm.exit.title"),
				getResourceStr("dialog.confirm.exit.button.ok"),
				getResourceStr("dialog.confirm.exit.button.cancel"),
				getResourceStr("dialog.confirm.exit.message"));
		if (i == SWT.OK)
			mainFrame.close();
	}

	public int getStyle() {
		return AS_PUSH_BUTTON;
	}

}
