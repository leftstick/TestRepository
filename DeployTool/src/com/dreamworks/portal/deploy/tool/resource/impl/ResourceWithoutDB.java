package com.dreamworks.portal.deploy.tool.resource.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.nanfeng.common.util.resource.Resource;

public class ResourceWithoutDB implements Resource {
	private static Resource instance;
	private ResourceBundle resource;
	private String bundleFile = "com.dreamworks.portal.deploy.tool.resource.Language";
	private Locale currentLanguage;

	public ResourceWithoutDB() {
		resource = ResourceBundle.getBundle(bundleFile, Locale.CHINESE);
		currentLanguage = resource.getLocale();
		Locale.setDefault(currentLanguage);
	}

	public static Resource instance() {
		synchronized (ResourceWithoutDB.class) {
			if (instance == null)
				instance = new ResourceWithoutDB();
		}
		return instance;
	}

	public Locale getCurrentLanguage() {
		return currentLanguage;
	}

	public String getString(String key) {
		return resource.getString(key);
	}

	public void modifyLocale(Locale locale) {
		resource = ResourceBundle.getBundle(bundleFile, locale);
	}

	public List<String> getKeys(String startWith) {
		Enumeration<String> keys = resource.getKeys();
		List<String> list = new ArrayList<String>();
		String temp;
		String start = startWith == null || startWith.trim().length() == 0 ? ""
				: startWith;
		while (keys.hasMoreElements()) {
			temp = keys.nextElement();
			if (temp.startsWith(start))
				list.add(temp);
		}
		Collections.sort(list, Collator.getInstance(resource.getLocale()));
		return list;
	}
}
