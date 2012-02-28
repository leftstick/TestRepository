package com.dreamworks.portal.deploy.tool.state;

import org.nanfeng.common.stateManager.AbstractStatusLine;
import org.nanfeng.common.util.resource.Resource;

public class StateLine extends AbstractStatusLine {

	public StateLine(Resource resource) {
		super(resource);
	}

	public void stateChanged(Object state) {
		if (state != null)
			setMessage(null, (String)state);
	}
}
