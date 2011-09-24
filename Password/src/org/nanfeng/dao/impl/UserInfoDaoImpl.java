package org.nanfeng.dao.impl;

import java.util.List;

import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.db.Connection;
import org.nanfeng.db.Statement;
import org.nanfeng.exception.RecordNotExistsException;
import org.nanfeng.util.DataBaseConstants;

public class UserInfoDaoImpl implements UserInfoDao {

	public void save(UserInfo user) {
		Connection<UserInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, UserInfo.class);
		Statement<UserInfo> stmt = conn.createStatement();
		stmt.insert(user);
		conn.close();
	}

	public void update(UserInfo user) {
		Connection<UserInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, UserInfo.class);
		Statement<UserInfo> stmt = conn.createStatement();
		stmt.update(user);
		conn.close();
	}

	public void delete(UserInfo user) {
		Connection<UserInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, UserInfo.class);
		Statement<UserInfo> stmt = conn.createStatement();
		stmt.delete(user.getUser_name());
		conn.close();
	}

	public UserInfo get(String userName) {
		UserInfo info = null;
		Connection<UserInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, UserInfo.class);
		Statement<UserInfo> stmt = conn.createStatement();
		try {
			info = stmt.getByPrimaryKey(userName);
		} catch (RecordNotExistsException e) {
		}
		conn.close();
		return info;
	}

	public List<UserInfo> get() {
		List<UserInfo> list = null;
		Connection<UserInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, UserInfo.class);
		Statement<UserInfo> stmt = conn.createStatement();
		try {
			list = stmt.get();
		} catch (RecordNotExistsException e) {
		}
		conn.close();
		return list;
	}

}
