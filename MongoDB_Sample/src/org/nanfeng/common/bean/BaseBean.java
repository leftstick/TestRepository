package org.nanfeng.common.bean;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public abstract class BaseBean {
	public String toString() {
		StringBuffer sb = new StringBuffer();
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(this);
		String value = null;
		for (PropertyDescriptor propertyDescriptor : pd) {
			if (propertyDescriptor.getPropertyType().getName()
					.equals("java.lang.Class")
					&& propertyDescriptor.getName().equals("class"))
				continue;
			try {
				value = BeanUtils.getProperty(this,
						propertyDescriptor.getName());
			} catch (Exception e) {
				value = "Convert Error";
			}
			sb.append(propertyDescriptor.getName() + "=" + value + "|");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
