package org.nanfeng.dao;

import org.nanfeng.bean.impl.UserInfo;

public interface UserInfoDao {
	public void save(UserInfo user);

	public void update(UserInfo user);

	public void delete(UserInfo user);

	public UserInfo get(String userName);
}
