package org.nanfeng.common.action;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.util.resource.Resource;

public abstract class SwitchPanelBaseAction extends AbstractAction {
	private Composite child;

	public SwitchPanelBaseAction(Resource resource, String text) {
		super(resource, text);
	}

	public void run() {
		MainFrame mainFrame = getContext().getData(MainFrame.PARENT,
				MainFrame.class);
		StackLayout layout = (StackLayout) mainFrame.getCentralComposite()
				.getLayout();
		if (child == null) {
			Composite child = makeChild(mainFrame);
			setChild(child);
		}
		if (child != null) {
			layout.topControl = getChild();
			child.getParent().layout();
		}
		operation();
	}

	public void setStateStr(String str) {
		getContext().getData(MainFrame.PARENT, MainFrame.class)
				.notifyLineState(str);
	}

	public abstract void operation();

	public abstract Composite makeChild(MainFrame mainFrame);

	public void stateChanged(Object state) {
		//
	}

	public Composite getChild() {
		return child;
	}

	public void setChild(Composite child) {
		this.child = child;
	}

}
