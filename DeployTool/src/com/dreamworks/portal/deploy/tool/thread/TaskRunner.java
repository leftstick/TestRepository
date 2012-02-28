package com.dreamworks.portal.deploy.tool.thread;

import java.util.LinkedList;
import java.util.List;

public class TaskRunner {

	private List<ServiceRunnable> services;

	public static TaskRunner instance = new TaskRunner();

	private TaskRunner() {
		services = new LinkedList<ServiceRunnable>();
	}

	public void addService(ServiceRunnable e) {
		services.add(e);
	}

	public void startAll() {
		for (int i = 0; i < services.size(); i++) {
			Thread t = new Thread(services.get(i));
			t.start();
		}
	}

	public void stopAll() {
		for (int i = 0; i < services.size(); i++) {
			ServiceRunnable e = services.get(i);
			e.stop();
		}
	}
}
