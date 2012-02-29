package org.nanfeng.common.db.mongo.query;

import java.util.List;
import java.util.regex.Pattern;

import org.nanfeng.common.exception.SymbolException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class JQLFilters {

	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int BOTH = 3;

	public static JQLFilter eq(final String key, final Object value) {
		return new JQLFilter() {
			protected void initQuery() {
				query = new BasicDBObject(key, value);
			}
		};
	}

	public static JQLFilter gt(final String key, final Object value) {
		return new JQLFilter() {
			protected void initQuery() {
				query = new BasicDBObject(key,
						new BasicDBObject("$gt", "value"));
			}
		};
	}

	public static JQLFilter lt(final String key, final Object value) {
		return new JQLFilter() {
			protected void initQuery() {
				query = new BasicDBObject(key,
						new BasicDBObject("$lt", "value"));
			}
		};
	}

	public static JQLFilter in(final String key, final List<Object> values) {
		return new JQLFilter() {
			protected void initQuery() {
				BasicDBList list = new BasicDBList();
				for (Object object : values) {
					list.add(object);
				}
				query = new BasicDBObject(key, new BasicDBObject("$in", list));
			}
		};
	}

	public static JQLFilter or(final JQLFilter exp1, final JQLFilter exp2) {
		return new JQLFilter() {
			protected void initQuery() {
				BasicDBList list = new BasicDBList();
				list.add(exp1.query);
				list.add(exp2.query);
				query = new BasicDBObject("$or", list);
			}
		};
	}

	public static JQLFilter like(final String key, final String value,
			final int pos) {
		return new JQLFilter() {
			protected void initQuery() {
				Pattern john = null;
				// right
				if (pos == RIGHT)
					john = Pattern.compile("^" + value,
							Pattern.CASE_INSENSITIVE);
				else if (pos == LEFT)
					john = Pattern.compile(value + "$",
							Pattern.CASE_INSENSITIVE);
				else if (pos == BOTH)
					john = Pattern.compile(value, Pattern.CASE_INSENSITIVE);
				else
					throw new SymbolException("error option pos=" + pos);
				query = new BasicDBObject(key, john);
			}
		};
	}
}
