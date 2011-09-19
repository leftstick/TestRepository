package org.nanfeng.bean.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.bean.Pojo;

public class ObjectInfo extends Pojo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3146258145342360117L;
	@PrimaryKey
	private String object_id;
	private String user_name = "";
	private String object_name = "";
	private String object_description = "";
	private List<ObjectProperty> objectProperties;

	public List<ObjectProperty> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(List<ObjectProperty> objectProperties) {
		this.objectProperties = objectProperties;
	}

	public ObjectInfo() {
		objectProperties = new ArrayList<ObjectProperty>();
	}

	public String getObject_id() {
		return object_id;
	}

	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getObject_name() {
		return object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getObject_description() {
		return object_description;
	}

	public void setObject_description(String object_description) {
		this.object_description = object_description;
	}

	public void addProperty(String key, String value) {
		objectProperties.add(new ObjectProperty(key, value));
	}

	public void addProperty(ObjectProperty op) {
		objectProperties.add(op);
	}

	public void removeProperty(int index) {
		objectProperties.remove(index);
	}

	public void clearProperties() {
		objectProperties.clear();
	}

	static public class ObjectProperty implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2447812285760065930L;
		public String key;
		public String value;

		public ObjectProperty(String k, String v) {
			key = k;
			value = v;
		}
	}

}
