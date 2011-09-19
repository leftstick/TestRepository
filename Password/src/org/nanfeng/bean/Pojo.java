package org.nanfeng.bean;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.nanfeng.exception.BeanErrorException;

public abstract class Pojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8719017418747266029L;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {

			PropertyDescriptor[] propertyDescriptors = PropertyUtils
					.getPropertyDescriptors(this.getClass());
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				if ("class".equals(propertyDescriptor.getName()))
					continue;
				sb.append(propertyDescriptor.getName()
						+ "="
						+ propertyDescriptor.getReadMethod().invoke(this,
								new Object[0]) + "|");
			}
		} catch (IllegalArgumentException e) {
			throw new BeanErrorException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new BeanErrorException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new BeanErrorException(e.getMessage(), e);
		}
		return sb.toString();
	}
}
