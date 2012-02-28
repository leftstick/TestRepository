package org.nanfeng.common.context;

public interface Context {
	public void setData(String key, Object obj);

	public <T> T getData(String key, Class<T> cls);
}
