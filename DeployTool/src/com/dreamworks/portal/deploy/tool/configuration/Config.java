package com.dreamworks.portal.deploy.tool.configuration;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class Config {

	private String catalinaHomePath = "";
	private static Config config;
	private boolean dirty = true;

	public boolean isDirty() {
		return dirty;
	}

	public void makeDirty() {
		dirty = true;
		synchronized (config) {
			config.notify();
		}
	}

	public String getCatalinaPort() {
		return "8080";
	}

	public boolean isTomcatFilled() {
		if (catalinaHomePath.trim().length() == 0)
			return false;
		return true;
	}

	public void clean() {
		dirty = false;
	}

	public static Config getConfig() {
		if (config == null) {
			synchronized (Config.class) {
				if (config == null) {
					config = new Config();
				}
			}
		}
		return config;
	}

	private Config() {
		//
	}

	public String getCatalinaHomePath() {
		return catalinaHomePath;
	}

	public void setCatalinaHomePath(String catalinaHomePath) {
		this.catalinaHomePath = catalinaHomePath;
	}

	public String getHostName() {
		return "localhost";
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(Config.class);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getPropertyType() != java.lang.Class.class) {
				Method readMethod = propertyDescriptor.getReadMethod();
				Object o = null;
				try {
					if (readMethod != null)
						o = readMethod.invoke(this, new Object[] {});
				} catch (Exception e) {
					e.printStackTrace();
					o = "";
				}
				sb.append(propertyDescriptor.getName() + "=" + o + " | ");
			}
		}
		return sb.toString();
	}
}
