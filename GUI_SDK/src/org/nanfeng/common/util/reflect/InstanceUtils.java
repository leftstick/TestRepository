package org.nanfeng.common.util.reflect;

import java.lang.reflect.Constructor;

public class InstanceUtils {

	public static <T> Object getInstance(Class<T> cls, @SuppressWarnings("rawtypes") Class[] types,
			Object[] args) {
		Constructor<T> constructor = null;
		try {
			constructor = cls.getConstructor(types);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		T newAction = null;
		try {
			newAction = constructor.newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return newAction;
	}
}
