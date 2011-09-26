package org.nanfeng.dao.impl;

import java.util.List;

import org.nanfeng.bean.impl.ObjectInfo;
import org.nanfeng.dao.ObjectInfoDao;
import org.nanfeng.db.Connection;
import org.nanfeng.db.Statement;
import org.nanfeng.util.Context;
import org.nanfeng.util.DataBaseConstants;

public class ObjectInfoDaoImpl implements ObjectInfoDao {

	public void save(ObjectInfo obj) {
		Connection<ObjectInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, ObjectInfo.class);
		Statement<ObjectInfo> stmt = conn.createStatement();
		stmt.insert(obj);
		conn.close();
	}

	public void update(ObjectInfo obj) {
		Connection<ObjectInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, ObjectInfo.class);
		Statement<ObjectInfo> stmt = conn.createStatement();
		stmt.update(obj);
		conn.close();
	}

	public void delete(ObjectInfo obj) {
		Connection<ObjectInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, ObjectInfo.class);
		Statement<ObjectInfo> stmt = conn.createStatement();
		stmt.delete(obj.getObject_id());
		conn.close();
	}

	public List<ObjectInfo> get(String userName) {
		List<ObjectInfo> list;
		Connection<ObjectInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, ObjectInfo.class);
		Statement<ObjectInfo> stmt = conn.createStatement();
		Context context = new Context();
		context.setValue("user_name", userName);
		list = stmt.getBySecondaryKey(context);
		conn.close();
		return list;
	}

	public ObjectInfo get(String userName, String objectName) {
		List<ObjectInfo> list;
		Connection<ObjectInfo> conn = Connection.createConnection(
				DataBaseConstants.envHome, ObjectInfo.class);
		Statement<ObjectInfo> stmt = conn.createStatement();
		Context context = new Context();
		context.setValue("user_name", userName);
		context.setValue("object_name", objectName);
		list = stmt.getBySecondaryKey(context);
		conn.close();
		return list.isEmpty() ? null : list.get(0);
	}

}
