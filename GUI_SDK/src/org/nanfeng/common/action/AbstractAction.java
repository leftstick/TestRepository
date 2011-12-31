package org.nanfeng.common.action;

import org.eclipse.jface.action.Action;
import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.state.State;
import org.nanfeng.common.util.resource.Resource;

public abstract class AbstractAction extends Action implements State{
	private Context context;
	private Resource resource;

	public AbstractAction(Resource resource,String text) {
		super(text);
		this.resource = resource;
		context = new ContextImpl();
	}

	public abstract int getStyle();
	
	public Resource getResource() {
		return resource;
	}
	public Context getContext() {
		return context;
	}

	public void stateChanged(Object state) {
		
	}

}
