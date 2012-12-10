package org.howard.portal.kit.resource.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.howard.portal.kit.resource.Resource;

/**
 * The purpose of this class is to provide a default resource controller implementation
 */
public class DefaultResource implements Resource {
	private static Resource instance;
	private ResourceBundle resource;
	private String bundleFile = "org.howard.portal.kit.resource.impl.Language";
	private Locale currentLanguage;

	/**
	 * Creates a new instance of <code>DefaultResource</code>.
	 */
	public DefaultResource() {
		resource = ResourceBundle.getBundle(bundleFile, Locale.ENGLISH);
		currentLanguage = resource.getLocale();
		Locale.setDefault(currentLanguage);
	}

	/**
	 * @return Singleton instance of DefaultResource
	 */
	public static Resource instance() {
		synchronized (DefaultResource.class) {
			if (instance == null)
				instance = new DefaultResource();
		}
		return instance;
	}

	@Override
    public Locale getCurrentLanguage() {
		return currentLanguage;
	}

	@Override
    public String getValue(String key) {
		return resource.getString(key);
	}

	@Override
    public void modifyLocale(Locale locale) {
		resource = ResourceBundle.getBundle(bundleFile, locale);
		currentLanguage = resource.getLocale();
	}

	@Override
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
