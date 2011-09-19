package org.nanfeng.dao;

import java.util.List;

import org.nanfeng.bean.impl.ObjectInfo;

public interface ObjectInfoDao {
	public void save(ObjectInfo obj);

	public void update(ObjectInfo obj);

	public void delete(ObjectInfo obj);

	public List<ObjectInfo> get(String userName);
}
