package org.nanfeng.upgrade.impl;

import java.util.ArrayList;
import java.util.List;

import org.nanfeng.bean.impl.Configuration;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.ConfigurationDao;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.ConfigurationDaoImpl;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.upgrade.Upgrade;

public class Upgrade1 implements Upgrade {
	private UserInfoDao userinfodao;
	private ConfigurationDao configdao;

	private List<String> chinese;
	private List<String> english;

	public Upgrade1() {
		userinfodao = new UserInfoDaoImpl();
		configdao = new ConfigurationDaoImpl();
		chinese = new ArrayList<String>();
		english = new ArrayList<String>();
		init();
	}

	private void init() {
		chinese.add("您的出生地？");
		chinese.add("您的生日？");
		chinese.add("您父亲的名字？");
		chinese.add("您母亲的名字？");

		english.add("Where were you born?");
		english.add("When were you born?");
		english.add("What is your father's name?");
		english.add("What is your mother's name?");
	}

	public void upgrade() {
		List<UserInfo> list = userinfodao.get();
		for (UserInfo userInfo : list) {
			if (chinese.contains(userInfo.getQuestion())
					|| english.contains(userInfo.getQuestion())) {

				userInfo.setQuestion((chinese.indexOf(userInfo.getQuestion()) >= 0 ? chinese
						.indexOf(userInfo.getQuestion()) : english
						.indexOf(userInfo.getQuestion()))
						+ "");
				userinfodao.update(userInfo);
			}
		}
		setConfig();
	}

	public boolean needUpgrade() {
		Configuration configuration = configdao.get(new Configuration(
				"upgrade_time"));
		List<UserInfo> list = userinfodao.get();
		if (list.isEmpty() && configuration == null) {
			setConfig();
			return false;
		}
		if (list.isEmpty() && configuration != null) {
			return false;
		}
		if (!list.isEmpty() && configuration == null) {
			for (UserInfo userInfo : list) {
				if (chinese.contains(userInfo.getQuestion())
						|| english.contains(userInfo.getQuestion()))
					return true;
			}
		}
		return false;
	}

	private void setConfig() {
		configdao.save(new Configuration("upgrade_time", 1));
	}
}
