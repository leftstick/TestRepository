package com.dreamworks.portal.deploy.tool.container.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.nanfeng.common.gui.log4j.appender.LogPageAppender;
import org.nanfeng.common.util.process.ShellRunner;

import com.dreamworks.portal.deploy.tool.configuration.Config;
import com.dreamworks.portal.deploy.tool.container.Tomcat;

public class ProcessTomcat implements Observer, Tomcat {
	private static File configFile = new File("profile/tomcat.cmd");
	private Config config;

	private static ProcessTomcat instance;

	private Properties sysPro;

	public static ProcessTomcat getInstance(Config config) {
		if (instance == null) {
			synchronized (ProcessTomcat.class) {
				if (instance == null) {
					instance = new ProcessTomcat(config);
				}
			}
		}
		return instance;

	}

	private ProcessTomcat(Config con) {
		config = con;
	}

	public void update(Observable o, Object arg) {
		init();
	}

	private void init() {
		System.out.println("init");
		sysPro = System.getProperties();
		sysPro.setProperty("catalina.home", config.getCatalinaHomePath());
		// sysPro.setProperty("com.sun.management.jmxremote", "");
		// sysPro.setProperty("com.sun.management.jmxremote.port",
		// config.getJmxPort());
		// sysPro.setProperty("com.sun.management.jmxremote.ssl", "false");
		// sysPro.setProperty("com.sun.management.jmxremote.authenticate",
		// "false");
		Map<String, String> getenv = System.getenv();
		for (Iterator<Entry<String, String>> iterator = getenv.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			sysPro.setProperty(entry.getKey(), entry.getValue());
		}
		sysPro.setProperty("CATALINA_HOME", config.getCatalinaHomePath());
	}

	public void startTomcat() throws Exception {
		if (isTomcatStarted() || !config.isTomcatFilled())
			return;
		ShellRunner runner = new ShellRunner();
		runner.setConsoleHandle(LogPageAppender.getAppender());
		runner.setRuningEnv(sysPro);
		String cmd = configFile.getAbsolutePath() + " \""
				+ config.getCatalinaHomePath() + "\" start";
		System.out.println(cmd);
		runner.setCmdline(cmd);
		runner.asyncRun(null);
	}

	public void stopTomcat() throws Exception {
		if (!isTomcatStarted() || !config.isTomcatFilled()) {
			synchronized (ProcessTomcat.this) {
				ProcessTomcat.this.notifyAll();
			}
			return;
		}
		ShellRunner runner = new ShellRunner();
		runner.setConsoleHandle(LogPageAppender.getAppender());
		sysPro.remove("JAVA_OPTS");
		runner.setRuningEnv(sysPro);
		String cmd = configFile.getAbsolutePath() + " \""
				+ config.getCatalinaHomePath() + "\" stop";
		runner.setCmdline(cmd);
		runner.asyncRun(new Runnable() {
			public void run() {
				synchronized (ProcessTomcat.this) {
					ProcessTomcat.this.notifyAll();
				}
			}
		});

	}

	public boolean isTomcatStarted() {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://" + config.getHostName()
				+ ":" + config.getCatalinaPort());
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			method.releaseConnection();
		}
		int statusCode = method.getStatusCode();
		return statusCode == 200;
	}

}
