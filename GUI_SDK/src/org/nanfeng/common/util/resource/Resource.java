package org.nanfeng.common.util.resource;

import java.util.List;
import java.util.Locale;

public interface Resource {

	public Locale getCurrentLanguage();

	public void modifyLocale(Locale locale);

	public String getString(String key);

	public List<String> getKeys(String startWith);
}
