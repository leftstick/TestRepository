package org.nanfeng.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.nanfeng.bean.impl.ObjectInfo;
import org.nanfeng.bean.impl.ObjectInfo.ObjectProperty;
import org.nanfeng.dao.ObjectInfoDao;
import org.nanfeng.dao.impl.HiloDao;
import org.nanfeng.dao.impl.ObjectInfoDaoImpl;

public class DataOperator {
	private String userName;
	private ObjectInfoDao objectinfodao;
	private String split_0x0f;
	private String split_0x00;
	private String split_0x01;

	public DataOperator(String user_name) {
		userName = user_name;
		objectinfodao = new ObjectInfoDaoImpl();
		split_0x0f = new String(new byte[] { 0x0f });
		split_0x00 = new String(new byte[] { 0x00 });
		split_0x01 = new String(new byte[] { 0x01 });
	}

	public void exportData(String exportPath) throws IOException {
		File export = new File(exportPath);
		List<String> list = new ArrayList<String>();
		List<ObjectInfo> datas = objectinfodao.get(userName);
		StringBuffer sb = new StringBuffer();
		for (ObjectInfo obj : datas) {
			sb.delete(0, sb.length());
			sb.append(obj.getObject_name());
			sb.append(split_0x0f);
			sb.append(obj.getObject_description());
			sb.append(split_0x0f);
			sb.append("{");
			List<ObjectProperty> objectProperties = obj.getObjectProperties();
			for (int i = 0; i < objectProperties.size(); i++) {
				if (i != 0)
					sb.append(split_0x00);
				sb.append("(");
				sb.append(objectProperties.get(i).key + split_0x01
						+ objectProperties.get(i).value);
				sb.append(")");
			}
			sb.append("}");
			list.add(sb.toString());
		}
		FileUtils.writeLines(export, "GBK", list);
	}

	public int[] importData(String user, String importPath) throws IOException {
		int[] result = new int[2];
		File importf = new File(importPath);
		List<String> list = FileUtils.readLines(importf, "GBK");
		ObjectInfo object = null;
		int total = 0;
		int success = 0;
		for (String line : list) {
			total++;
			ObjectInfo obj = new ObjectInfo();
			try {
				obj.setUser_name(user);
				analyze(obj, line);
			} catch (RuntimeException e) {
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

	private void analyze(ObjectInfo obj, String line) {
		String[] element = line.split(split_0x0f);
		if (element.length != 3)
			throw new RuntimeException();
		obj.setObject_name(element[0]);
		obj.setObject_description(element[1]);
		String end = element[2];
		if (end.charAt(0) != '{')
			throw new RuntimeException();
		if (end.charAt(end.toCharArray().length - 1) != '}')
			throw new RuntimeException();
		List<ObjectProperty> list = new ArrayList<ObjectProperty>();
		String content = end.substring(1, end.length() - 1);
		String[] properties = content.split(split_0x00);
		for (String property : properties) {
			if (property.charAt(0) != '(')
				throw new RuntimeException();
			if (property.charAt(property.toCharArray().length - 1) != ')')
				throw new RuntimeException();
			String[] entry = property.substring(1, property.length() - 1)
					.split(split_0x01);
			if (entry.length != 2)
				throw new RuntimeException();
			ObjectProperty op = new ObjectProperty(entry[0], entry[1]);
			list.add(op);
		}
		obj.setObjectProperties(list);
	}
}
