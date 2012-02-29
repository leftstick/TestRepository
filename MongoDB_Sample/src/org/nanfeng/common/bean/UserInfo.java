package org.nanfeng.common.bean;

import java.io.Serializable;

public class UserInfo extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7253564098761184566L;
	private int user_id;
	private String user_name;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
