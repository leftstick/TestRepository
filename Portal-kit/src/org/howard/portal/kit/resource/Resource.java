package org.howard.portal.kit.resource;

import java.util.List;
import java.util.Locale;

/**
 * The purpose of this class is to provide Resource API interface
 */
public interface Resource {

    /**
     * @return current using language
     */
    public Locale getCurrentLanguage();

    /**
     * modify current using language to specified locale
     * 
     * @param locale
     */
    public void modifyLocale(Locale locale);

    /**
     * @param key
     * @return value of specified key
     */
    public String getValue(String key);

    /**
     * @param startWith
     * @return array of key, that start with specified characters
     */
    public List<String> getKeys(String startWith);
}
