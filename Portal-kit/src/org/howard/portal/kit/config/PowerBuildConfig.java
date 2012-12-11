package org.howard.portal.kit.config;

import java.io.File;
import java.util.List;
import java.util.Observable;

/**
 * The purpose of this class is to provide configuration to power
 * build
 */
public class PowerBuildConfig extends Observable {
    private String designPath;
    private String deployPath;
    private static PowerBuildConfig config;

    /**
     * @return instance of PowerBuildConfig
     */
    public static PowerBuildConfig getConfig() {
        if (config == null) {
            synchronized (PowerBuildConfig.class) {
                if (config == null) {
                    config = new PowerBuildConfig();
                }
            }
        }
        return config;
    }

    /**
     * @return Returns the designPath.
     */
    public String getDesignPath() {
        return designPath;
    }

    /**
     * @param designPath The designPath to set.
     */
    public void setDesignPath(String designPath) {
        this.designPath = designPath;
    }

    /**
     * @return Returns the deployPath.
     */
    public String getDeployPath() {
        return deployPath;
    }

    /**
     * @param deployPath The deployPath to set.
     */
    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    /**
     * @param result
     * @return is configuration reseted
     */
    public boolean resetConfig(List<List<String>> result) {
        if (result == null || result.isEmpty() || result.size() != 2 || result.get(0).size() != 2
                || result.get(1).size() != 2)
            throw new RuntimeException("Wrong result setted");
        boolean isReset = false;
        if (config.getDesignPath() == null || !config.getDesignPath().equals(result.get(0).get(1))) {
            isReset = true;
            if (!new File(result.get(0).get(1)).isDirectory())
                throw new RuntimeException("Design Path should be a correct directory");
            config.setDesignPath(result.get(0).get(1));
        }
        if (config.getDeployPath() == null || !config.getDeployPath().equals(result.get(1).get(1))) {
            isReset = true;
            if (!new File(result.get(1).get(1)).isDirectory())
                throw new RuntimeException("Deploy Path should be a correct directory");
            config.setDeployPath(result.get(1).get(1));
        }
        if (isReset) {
            super.setChanged();
            super.notifyObservers();
        }
        return isReset;
    }
}
