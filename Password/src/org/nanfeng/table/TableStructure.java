package org.nanfeng.table;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.exception.BeanErrorException;
import org.nanfeng.exception.PrimaryKeyNotFoundException;

public class TableStructure<E> {
	private Class<E> clazz;
	private Field[] declaredFields;

	private TableStructure(Class<E> po) {
		clazz = po;
		declaredFields = clazz.getDeclaredFields();
	}

	public static <E> TableStructure<E> wrapTableStructure(Class<E> cls) {
		return new TableStructure<E>(cls);
	}

	public String tableName() {
		return clazz.getSimpleName().toUpperCase();
	}

	public String primaryKey() {
		Field field = null;
		for (Field f : declaredFields) {
			if (f.getAnnotation(PrimaryKey.class) != null) {
				field = f;
				break;
			}
		}
		if (field == null)
			throw new PrimaryKeyNotFoundException(clazz.getSimpleName()
					+ " has not a correct primary key");
		if (field.getType() != String.class)
			throw new BeanErrorException("primary key '" + field.getName()
					+ "' must be String");
		return field.getName();
	}

	public String primaryValue(E obj) {
		try {
			return BeanUtils.getProperty(obj, primaryKey());
		} catch (Exception e) {
			return null;
		}

	}

	public List<String> secondaryKeys() {
		List<String> list = new LinkedList<String>();
		for (Field f : declaredFields) {
			if (f.getAnnotation(PrimaryKey.class) == null
					&& String.class == f.getType()) {
				list.add(f.getName());
			}
		}
		return list;
	}
}
