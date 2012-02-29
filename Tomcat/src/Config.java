import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class Config {

	private String catalinaHomePath;
	private String catalinaPort;
	private String hostName;
	public static Config config;
	private boolean dirty = true;

	public boolean isDirty() {
		return dirty;
	}

	public void dirty() {
		dirty = true;
	}

	public void clean() {
		dirty = false;
	}

	public static Config getConfig() {
		if (config == null) {
			synchronized (Config.class) {
				if (config == null) {
					config = new Config();
					Thread t = new Thread(new LoadConfig(config));
					t.start();
				}
			}
		}
		return config;
	}

	private Config() {
		//
	}

	public String getCatalinaHomePath() {
		return catalinaHomePath;
	}

	public void setCatalinaHomePath(String catalinaHomePath) {
		this.catalinaHomePath = catalinaHomePath;
	}

	public String getCatalinaPort() {
		return catalinaPort;
	}

	public void setCatalinaPort(String catalinaPort) {
		this.catalinaPort = catalinaPort;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(Config.class);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getPropertyType() != java.lang.Class.class) {
				Method readMethod = propertyDescriptor.getReadMethod();
				Object o = null;
				try {
					o = readMethod.invoke(this, new Object[] {});
				} catch (Exception e) {
					e.printStackTrace();
					o = "";
				}
				sb.append(propertyDescriptor.getName() + "=" + o + " | ");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException,
			FileNotFoundException, IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		getConfig();
		getConfig();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("set");
		config.dirty = true;
	}

}
