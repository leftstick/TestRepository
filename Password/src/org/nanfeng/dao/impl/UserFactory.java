package org.nanfeng.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.nanfeng.bean.impl.UserInfo;

public class UserFactory {
	private static List<UserInfo> list = new ArrayList<UserInfo>();
	static {
		UserInfo user1 = new UserInfo();
		user1.setUser_name("nanfeng");
		user1.setUser_pwd("123");
		user1.setQuestion("Where were you born?");
		user1.setAnswer("É½Î÷Ì«Ô­");
		list.add(user1);
	}

	public static void adduser(UserInfo user) {
		list.add(user);
	}

	public static void updateuser(UserInfo user) {
		for (UserInfo u : list) {
			if (u.getUser_name().equals(user.getUser_name())) {
				list.remove(u);
				list.add(user);
				return;
			}
		}
	}

	public static void deleteuser(UserInfo user) {
		list.remove(user);
	}

	public static UserInfo getuser(String name) {
		for (UserInfo u : list) {
			if (u.getUser_name().equals(name)) {
				return u;
			}
		}
		return null;
	}
}
