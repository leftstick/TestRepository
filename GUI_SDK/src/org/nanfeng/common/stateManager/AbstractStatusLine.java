package org.nanfeng.common.stateManager;

import org.eclipse.jface.action.StatusLineManager;
import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.state.State;
import org.nanfeng.common.util.resource.Resource;

public abstract class AbstractStatusLine extends StatusLineManager implements
		State {
	private Context context;
	private Resource resource;

	public AbstractStatusLine(Resource resource) {
		context = new ContextImpl();
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public Context getContext() {
		return context;
	}

	public void stateChanged(Object state) {

	}

}
