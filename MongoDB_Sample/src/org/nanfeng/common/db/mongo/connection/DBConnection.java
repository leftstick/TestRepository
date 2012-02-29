package org.nanfeng.common.db.mongo.connection;

import org.nanfeng.common.exception.ConnectionException;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class DBConnection {
	private Mongo mongo = null;
	private DB db = null;

	public DBConnection(String host, int port, String user, String pwd,
			String database) throws ConnectionException {

		boolean isAllow = true;
		try {
			if (host == null)
				mongo = new Mongo();
			else
				mongo = new Mongo(host, port);
			db = mongo.getDB(database);
			if (user != null)
				isAllow = db.authenticate(user, pwd.toCharArray());
			if (!isAllow)
				throw new ConnectionException("fail user or password");
		} catch (Exception e) {
			throw new ConnectionException(e.getMessage());
		}
	}

	public DBConnection(String host, int port, String database)
			throws ConnectionException {
		this(host, port, null, null, database);
	}

	public DBConnection(String database) throws ConnectionException {
		this(null, 0, null, null, database);
	}

	public DB getDb() {
		return db;
	}

	public void close() {
		mongo.close();
	}
}
