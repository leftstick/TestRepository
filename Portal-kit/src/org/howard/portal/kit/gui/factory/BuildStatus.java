package org.howard.portal.kit.gui.factory;

/**
 * The purpose of this class is to provide enum value of status
 */
public enum BuildStatus {
    /**
     * <code>NOTEXEC</code>, not executed.
     */
    NOTEXEC("Not executed"), /**
     * <code>EXECUTING</code>, executing.
     */
    EXECUTING("Executing"), /**
     * <code>COMPLETED</code>, executed.
     */
    COMPLETED("Completed"), /**
     * <code>ERROR</code>, executed but
     * failure.
     */
    ERROR("Error");

    private String value;

    private BuildStatus(String v) {
        value = v;
    }

    /**
     * @return value of Status
     */
    public String getValue() {
        return value;
    }
}
