package org.nanfeng.common.context.impl;

import java.util.HashMap;
import java.util.Map;

import org.nanfeng.common.context.Context;

public class ContextImpl implements Context {
	private Map<String, Object> context;

	public ContextImpl() {
		context = new HashMap<String, Object>();
	}

	public void setData(String key, Object obj) {
		if (key != null && obj != null)
			context.put(key, obj);
		else if (key != null && obj == null)
			context.remove(key);
		else if (key == null)
			throw new RuntimeException(
					"set data error:the key must not be null");
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(String key, Class<T> cls) {
		if (key == null)
			throw new RuntimeException(
					"set data error:the key must not be null");
		Object obj = null;
		obj = context.get(key);
		if (obj != null)
			return (T) obj;
		return null;
	}

}
