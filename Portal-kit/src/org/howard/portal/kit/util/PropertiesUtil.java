package org.howard.portal.kit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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
     * Calls the Hashtable method put. Provided for parallelism with
     * the getProperty method. Enforces use of strings for property
     * keys and values. The value returned is the result of the
     * Hashtable call to put.
     * 
     * @param key the key to be placed into this property list.
     * @param value the key to be placed into this property list.
     */
    public void setProperty(String key, String value) {
        prop.setProperty(key, value);
    }

    /**
     * Searches for the property with the specified key in this
     * property list. If the key is not found in this property list,
     * the default property list, and its defaults, recursively, are
     * then checked. The method returns null if the property is not
     * found.
     * 
     * @param key
     * @return the value in this property list with the specified key
     *         value.
     */
    public String getProperty(String key) {
        return prop.getProperty(key);
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
        cfile = new File("profile", "config.dat");
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
            try {
                cfile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * Sync properties to configuration file
     */
    public void sync() {
        try {
            prop.store(new FileOutputStream(cfile), DateUtil.simpleDate(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
