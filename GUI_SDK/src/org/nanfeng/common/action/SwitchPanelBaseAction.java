package org.nanfeng.common.action;

import org.eclipse.swt.widgets.Composite;
import org.nanfeng.common.action.AbstractAction;
import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.util.resource.Resource;

public abstract class SwitchPanelBaseAction extends AbstractAction {
	private Composite child;

	public SwitchPanelBaseAction(Resource resource,String text) {
		super(resource, text);
	}

	public int getStyle() {
		return 0;
	}
	
	public void run() {
		if (getChild() != null)
			return;
		MainFrame mainFrame = getContext().getData(MainFrame.PARENT,
				MainFrame.class);
		Context context = new ContextImpl();
		context.setData("dispose.except", this.getClass().getName());
		mainFrame.changeActionState(context);
		Composite child = makeChild(mainFrame);
		setChild(child);
		child.getParent().layout();
		child.getShell().pack();
	}
	public abstract Composite makeChild(MainFrame mainFrame);

	public void stateChanged(Object state) {
		if (state instanceof Context) {
			Context con = (Context) state;
			String value = con.getData("dispose.except", String.class);
			if (value != null && !value.equals(this.getClass().getName())) {
				if (child != null) {
					child.dispose();
					child = null;
				}
			}
		}
	}

	public Composite getChild() {
		return child;
	}

	public void setChild(Composite child) {
		this.child = child;
	}

}
