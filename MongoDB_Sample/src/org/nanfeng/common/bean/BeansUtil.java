package org.nanfeng.common.bean;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.nanfeng.common.exception.ConvertException;

import com.mongodb.BasicDBObject;

public class BeansUtil {
	public static BasicDBObject bean2Object(Serializable bean)
			throws ConvertException {
		BasicDBObject obj = new BasicDBObject();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(bean.getClass());
		try {
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				if (propertyDescriptor.getPropertyType().getName()
						.equals("java.lang.Class")
						&& propertyDescriptor.getName().equals("class"))
					continue;
				obj.put(propertyDescriptor.getName(), propertyDescriptor
						.getReadMethod().invoke(bean, new Object[0]));
			}
		} catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
		return obj;
	}

	public static <T> T object2Bean(BasicDBObject obj, Class<T> beanType)
			throws ConvertException {
		T bean = null;
		try {
			bean = beanType.newInstance();
			PropertyDescriptor[] propertyDescriptors = PropertyUtils
					.getPropertyDescriptors(bean.getClass());
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				if (propertyDescriptor.getPropertyType().getName()
						.equals("java.lang.Class")
						&& propertyDescriptor.getName().equals("class"))
					continue;
				BeanUtils.setProperty(bean, propertyDescriptor.getName(),
						obj.get(propertyDescriptor.getName()));
			}
		} catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
		return bean;
	}

	public static String simpleClassName(Class cls) {
		return cls.getSimpleName();
	}

	public static void main(String[] args) {
		UserInfo user = new UserInfo();
		user.setUser_id(1);
		user.setUser_name("nanfeng");
		BasicDBObject obj = null;
		try {
			obj = bean2Object(user);
		} catch (ConvertException e) {
			e.printStackTrace();
		}
		System.out.println(obj);

		try {
			UserInfo u = object2Bean(obj, UserInfo.class);
			System.out.println(u);
		} catch (ConvertException e) {
			e.printStackTrace();
		}
		System.out.println(user.getClass().getSimpleName());

	}
}
