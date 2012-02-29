package org.nanfeng.common.db.mongo.query;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;

public abstract class JQLFilter {

	protected BasicDBObject query = null;

	protected JQLFilter() {
		initQuery();
	}

	protected abstract void initQuery();

	public JQLFilter addFilter(JQLFilter filter) {
		if (query == null)
			query = filter.query;
		else
			query.putAll((BSONObject) filter.query);
		return this;
	}

	public BasicDBObject getQuery() {
		return query;
	}
}