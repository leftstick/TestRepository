package com.dreamworks.portal.deploy.tool.thread.util;

import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.gui.MainFrame;

import com.dreamworks.portal.deploy.tool.container.Tomcat;
import com.dreamworks.portal.deploy.tool.thread.ServiceRunnable;

public class TomcatStateThread implements ServiceRunnable {
	private MainFrame frame;
	private boolean isRunning = true;
	private Tomcat tomcat;
	private static TomcatStateThread instance;

	private TomcatStateThread(MainFrame mf, Tomcat tom) {
		frame = mf;
		tomcat = tom;
	}

	public static TomcatStateThread getInstance(MainFrame mf, Tomcat tom) {
		if (instance == null) {
			synchronized (TomcatStateThread.class) {
				if (instance == null) {
					instance = new TomcatStateThread(mf, tom);
				}
			}
		}
		return instance;
	}

	private void checkState() {
		Context cxt = new ContextImpl();
		cxt.setData("tomcat.state", Boolean.valueOf(tomcat.isTomcatStarted()));
		frame.notifyActions(cxt);
		synchronized (tomcat) {
			try {
				tomcat.wait(5000);
			} catch (InterruptedException e) {
			}
		}

	}

	public void run() {
		while (isRunning) {
			checkState();
		}
	}

	public void stop() {
		synchronized (tomcat) {
			isRunning = false;
			try {
				tomcat.stopTomcat();
			} catch (Exception e) {
			}
		}
	}
}
