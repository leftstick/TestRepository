import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

public class LoadConfig implements ServiceRunnable {
	private static File configFile = new File("profile/config.properties");
	private static boolean isRunning = true;
	private Config config;

	public LoadConfig(Config con) {
		config = con;
		load();
	}

	public void run() {
		while (isRunning) {
			if (config.isDirty()) {
				load();
			}

		}
	}

	private void load() {
		System.out.println("loading...");
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(
					new FileInputStream(configFile), "UTF-8"));
		} catch (Exception e) {
			stop();
			throw new RuntimeException("read config file error", e);
		}
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(Config.class);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getPropertyType() != java.lang.Class.class) {
				Method writeMethod = propertyDescriptor
						.getWriteMethod();
				try {
					if (writeMethod != null)
						writeMethod.invoke(config, new Object[] { p
								.getProperty(propertyDescriptor
										.getName()) });
				} catch (Exception e) {
					stop();
					throw new RuntimeException(
							"load config file to cache error", e);
				}
			}
		}
		config.clean();
		System.out.println(config);
	}

	public void stop() {
		isRunning = false;

	}
}