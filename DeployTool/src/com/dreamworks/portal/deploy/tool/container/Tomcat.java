package com.dreamworks.portal.deploy.tool.container;

public interface Tomcat {
	public void startTomcat() throws Exception;

	public void stopTomcat() throws Exception;

	public boolean isTomcatStarted();
}
