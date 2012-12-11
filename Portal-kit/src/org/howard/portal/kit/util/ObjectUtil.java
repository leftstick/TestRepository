package org.howard.portal.kit.util;

/**
 * The purpose of this class is to provide several useful APIs to
 * object
 */
public class ObjectUtil {
    /**
     * Check whether the specified obj is null or not.
     * @param obj
     * @param msg
     */
    public static void checkNull(Object obj, String msg) {
        if (obj == null)
            throw new NullPointerException(msg);
    }
}
