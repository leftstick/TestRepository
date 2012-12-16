package org.howard.portal.kit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The purpose of this class is to provide a Date tool
 */
public class DateUtil {
    private static SimpleDateFormat simple = new SimpleDateFormat();
    private static String PATTERN_SIMPLE = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date string with format yyyy-MM-dd HH:mm:ss.
     * 
     * @param date
     * @return date string
     */
    public static String simpleDate(Date date) {
        simple.applyPattern(PATTERN_SIMPLE);
        return simple.format(date);
    }
}
