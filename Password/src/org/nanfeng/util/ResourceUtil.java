package org.nanfeng.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.nanfeng.bean.impl.Configuration;
import org.nanfeng.dao.ConfigurationDao;
import org.nanfeng.dao.impl.ConfigurationDaoImpl;

public class ResourceUtil {
	private static ResourceUtil instance;
	private ResourceBundle resource;
	private String bundleFile = "org.nanfeng.ui.resource.Language";

	private ConfigurationDao configdao;
	private Locale currentLanguage;

	private ResourceUtil() {
		configdao = new ConfigurationDaoImpl();
		Configuration config = configdao.get(new Configuration("language"));
		if (config == null) {
			config = new Configuration("language", Locale.ENGLISH);
			configdao.save(config);
		}
		resource = ResourceBundle.getBundle(bundleFile,
				(Locale) config.getConfig_object());
		currentLanguage = (Locale) config.getConfig_object();
		Locale.setDefault(currentLanguage);
	}

	public static ResourceUtil instance() {
		synchronized (ResourceUtil.class) {
			if (instance == null)
				instance = new ResourceUtil();
		}
		return instance;
	}

	public Locale getCurrentLanguage() {
		return currentLanguage;
	}

	public String getString(String key) {
		return resource.getString(key);
	}

	public Locale getDBLanguage() {
		return (Locale) configdao.get(new Configuration("language"))
				.getConfig_object();
	}

	public void modifyLanguageConfig(Locale locale) {
		Configuration config = configdao.get(new Configuration("language"));
		config.setConfig_object(locale);
		configdao.update(config);
	}

	public void refreshCache() {
		Configuration config = configdao.get(new Configuration("language"));
		resource = ResourceBundle.getBundle(bundleFile,
				(Locale) config.getConfig_object());
		currentLanguage = (Locale) config.getConfig_object();
		Locale.setDefault(currentLanguage);
	}
}
