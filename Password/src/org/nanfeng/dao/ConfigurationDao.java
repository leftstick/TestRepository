package org.nanfeng.dao;

import org.nanfeng.bean.impl.Configuration;

public interface ConfigurationDao {
	public void save(Configuration config);

	public void update(Configuration config);

	public void delete(Configuration config);

	public Configuration get(Configuration key);
}
