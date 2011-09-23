package org.nanfeng.bean.impl;

import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.bean.Pojo;

public class Configuration extends Pojo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3961885353909061684L;
	@PrimaryKey
	private String config_key;
	private Object config_object;

	public Configuration(String key) {
		config_key = key;
	}

	public Configuration(String key, Object value) {
		config_key = key;
		config_object = value;
	}

	public String getConfig_key() {
		return config_key;
	}

	public void setConfig_key(String config_key) {
		this.config_key = config_key;
	}

	public Object getConfig_object() {
		return config_object;
	}

	public void setConfig_object(Object config_object) {
		this.config_object = config_object;
	}
}
