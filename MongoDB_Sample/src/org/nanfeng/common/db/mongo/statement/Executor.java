package org.nanfeng.common.db.mongo.statement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nanfeng.common.bean.BaseBean;
import org.nanfeng.common.bean.BeansUtil;
import org.nanfeng.common.db.mongo.connection.DBConnection;
import org.nanfeng.common.db.mongo.query.JQLFilter;
import org.nanfeng.common.exception.ConvertException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

public class Executor {
	private DBConnection conn;

	public Executor(DBConnection connection) {
		conn = connection;
	}

	public void save(Serializable bean) {
		DBCollection coll = null;
		String tablename = BeansUtil.simpleClassName(bean.getClass());
		coll = conn.getDb().getCollection(tablename);

		try {
			coll.save(BeansUtil.bean2Object(bean),WriteConcern.SAFE);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public <T> List<T> find(Class<T> bean, JQLFilter filter) {
		DBCollection coll = null;
		String tablename = BeansUtil.simpleClassName(bean);
		coll = conn.getDb().getCollection(tablename);

		List<T> list = new ArrayList<T>();
		DBCursor cursor = coll.find(filter.getQuery());
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			try {
				list.add(BeansUtil.object2Bean(obj, bean));
			} catch (ConvertException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return list;
	}

	public int count(Class<? extends BaseBean> bean, JQLFilter filter) {
		DBCollection coll = null;
		String tablename = BeansUtil.simpleClassName(bean);
		coll = conn.getDb().getCollection(tablename);

		return coll.find(filter.getQuery()).count();
	}

	public <T> List<T> find(Class<T> bean, JQLFilter filter, int pageSize,
			int page) {
		DBCollection coll = null;
		String tablename = BeansUtil.simpleClassName(bean);
		coll = conn.getDb().getCollection(tablename);

		List<T> list = new ArrayList<T>();
		int temp = (page - 1) * pageSize;
		DBCursor cursor = coll.find(filter.getQuery()).limit(pageSize).skip(temp);
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			try {
				list.add(BeansUtil.object2Bean(obj, bean));
			} catch (ConvertException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return list;
	}
}
