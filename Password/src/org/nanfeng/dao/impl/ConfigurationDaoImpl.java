package org.nanfeng.dao.impl;

import org.nanfeng.bean.impl.Configuration;
import org.nanfeng.dao.ConfigurationDao;
import org.nanfeng.db.Connection;
import org.nanfeng.db.Statement;
import org.nanfeng.exception.RecordNotExistsException;
import org.nanfeng.util.DataBaseConstants;

public class ConfigurationDaoImpl implements ConfigurationDao {

	public void save(Configuration config) {
		Connection<Configuration> conn = Connection.createConnection(
				DataBaseConstants.envHome, Configuration.class);
		Statement<Configuration> stmt = conn.createStatement();
		stmt.insert(config);
		conn.close();
	}

	public void update(Configuration config) {
		Connection<Configuration> conn = Connection.createConnection(
				DataBaseConstants.envHome, Configuration.class);
		Statement<Configuration> stmt = conn.createStatement();
		stmt.update(config);
		conn.close();
	}

	public void delete(Configuration config) {
		Connection<Configuration> conn = Connection.createConnection(
				DataBaseConstants.envHome, Configuration.class);
		Statement<Configuration> stmt = conn.createStatement();
		stmt.delete(config.getConfig_key());
		conn.close();
	}

	public Configuration get(Configuration config) {
		Configuration info = null;
		Connection<Configuration> conn = Connection.createConnection(
				DataBaseConstants.envHome, Configuration.class);
		Statement<Configuration> stmt = conn.createStatement();
		try {
			info = stmt.getByPrimaryKey(config.getConfig_key());
		} catch (RecordNotExistsException e) {
		}
		conn.close();
		return info;
	}

}
