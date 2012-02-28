package com.dreamworks.portal.deploy.tool.thread.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import com.dreamworks.portal.deploy.tool.configuration.Config;
import com.dreamworks.portal.deploy.tool.thread.ServiceRunnable;

public class ConfigStateThread extends Observable implements ServiceRunnable {
	private static File configFile = new File("profile/config.properties");
	private static boolean isRunning = true;
	private static Config config;
	private static ConfigStateThread instance;

	private ConfigStateThread(Config con) {
		config = con;
	}

	public static ConfigStateThread getInstance() {
		if (instance == null) {
			synchronized (ConfigStateThread.class) {
				if (instance == null) {
					instance = new ConfigStateThread(Config.getConfig());
				}
			}
		}
		return instance;
	}

	public void run() {
		while (isRunning) {
			if (config.isDirty()) {
				if (load()) {
					setChanged();
					notifyObservers();
				}
			} else {
				try {
					synchronized (config) {
						config.wait();
					}
				} catch (InterruptedException e) {
				}
			}

		}
	}

	public static Properties loadProperties()
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		Properties p = new Properties();
		p.load(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
		return p;
	}

	public static void storeProperties(Properties p)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		if (!configFile.getParentFile().exists())
			configFile.getParentFile().mkdir();
		p.store(new OutputStreamWriter(new FileOutputStream(configFile),
				"UTF-8"), "it is dirty");
	}

	private boolean load() {
		System.out.println("loading...");
		Properties p = null;
		try {
			p = loadProperties();
		} catch (Exception e) {
			config.clean();
			return false;
		}
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(Config.class);
		String temp;
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			temp = null;
			if (propertyDescriptor.getPropertyType() != java.lang.Class.class) {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				try {
					if (writeMethod != null) {
						temp = p.getProperty(propertyDescriptor.getName());
						temp = temp == null ? "" : temp;
						writeMethod.invoke(config, new Object[] { temp });
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
		config.clean();
		System.out.println(config);
		return true;
	}

	public void stop() {
		synchronized (config) {
			isRunning = false;
			config.notifyAll();
		}
	}
}