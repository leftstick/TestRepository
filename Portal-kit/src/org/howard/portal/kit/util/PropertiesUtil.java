package org.howard.portal.kit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The purpose of this class is to provide a util to read properties
 */
public class PropertiesUtil {
    private File cfile;
    private Properties prop;

    private static PropertiesUtil reader;

    /**
     * @return instance of PropertiesReader
     */
    public static PropertiesUtil getInstance() {
        if (reader == null) {
            synchronized (PropertiesUtil.class) {
                if (reader == null) {
                    reader = new PropertiesUtil();
                }
            }
        }
        return reader;
    }

    /**
     * @return properties of configuration file.
     */
    public Properties getProperties() {
        return prop;
    }

    /**
     * Creates a new instance of <code>PropertiesReader</code>.
     */
    private PropertiesUtil() {
        cfile = new File("profile");
        prop = new Properties();
        read();
    }

    /**
     * @return properties from configuration file.
     */
    public Properties read() {
        try {
            prop.load(new FileInputStream(cfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public void sync() {

    }
}
