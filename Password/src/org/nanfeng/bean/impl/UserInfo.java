package org.nanfeng.bean.impl;

import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.bean.Pojo;

public class UserInfo extends Pojo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2839881118317471028L;
	@PrimaryKey
	private String user_name = "";
	private String user_pwd = "";
	private String question = "";
	private String answer = "";

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
