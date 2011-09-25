package org.nanfeng.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.nanfeng.bean.impl.ObjectInfo;
import org.nanfeng.bean.impl.ObjectInfo.ObjectProperty;
import org.nanfeng.dao.ObjectInfoDao;
import org.nanfeng.dao.impl.HiloDao;
import org.nanfeng.dao.impl.ObjectInfoDaoImpl;

public class DataOperator {
	private String userName;
	private ObjectInfoDao objectinfodao;

	private String OBJ_NAME = "object_name";
	private String OBJ_DESC = "object_description";
	private String OBJ_PROP = "object_properties";

	public DataOperator(String user_name) {
		userName = user_name;
		objectinfodao = new ObjectInfoDaoImpl();
	}

	public void exportData(String exportPath) throws IOException {
		File export = new File(exportPath);
		List<String> list = new ArrayList<String>();
		List<ObjectInfo> datas = objectinfodao.get(userName);
		for (ObjectInfo obj : datas) {
			Map<String, Object> objectinfo = new LinkedHashMap<String, Object>();
			objectinfo.put(OBJ_NAME, obj.getObject_name());
			objectinfo.put(OBJ_DESC, obj.getObject_description());
			List<ObjectProperty> pro = obj.getObjectProperties();
			Map<String, String> pros = new LinkedHashMap<String, String>();
			for (ObjectProperty op : pro) {
				pros.put(op.key, op.value);
			}
			objectinfo.put(OBJ_PROP, pros);
			list.add(JSONObject.fromObject(objectinfo).toString());
		}

		FileUtils.writeLines(export, "GBK", list);
	}

	public int[] importData(String user, String importPath) throws IOException {
		int[] result = new int[2];
		File importf = new File(importPath);
		List<String> list = FileUtils.readLines(importf, "GBK");
		ObjectInfo object = null;
		JSONObject json = null;
		int total = 0;
		int success = 0;
		for (String line : list) {
			total++;
			ObjectInfo obj = new ObjectInfo();
			try {
				json = JSONObject.fromObject(line);
				obj.setUser_name(user);
				obj.setObject_name(json.getString(OBJ_NAME));
				obj.setObject_description(json.getString(OBJ_DESC));
				@SuppressWarnings("unchecked")
				Map<String, String> pros = (Map<String, String>) json
						.get(OBJ_PROP);
				for (Iterator<Entry<String, String>> iterator = pros.entrySet()
						.iterator(); iterator.hasNext();) {
					Entry<String, String> entry = iterator.next();
					obj.addProperty(entry.getKey(), entry.getValue());

				}
			} catch (Exception e) {
				continue;
			}
			object = objectinfodao.get(user, obj.getObject_name());
			if (object == null) {
				obj.setObject_id(HiloDao.getInstance().getValue());
				objectinfodao.save(obj);
			} else {
				object.setObject_name(obj.getObject_name());
				object.setObject_description(obj.getObject_description());
				object.setUser_name(obj.getUser_name());
				object.setObjectProperties(obj.getObjectProperties());
				objectinfodao.update(object);
			}
			success++;
		}
		result[0] = total;
		result[1] = success;
		return result;
	}
}
