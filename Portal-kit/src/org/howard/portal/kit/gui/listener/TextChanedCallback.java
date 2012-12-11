package org.howard.portal.kit.gui.listener;

/**
 * The purpose of this class is to provide a textchanged callback to
 * table
 */
public interface TextChanedCallback {
    /**
     * callback when something changed or not
     * 
     * @param isChanged tell something you want to listen if it has
     *            been changed or not
     */
    public void onChanged(boolean isChanged);
}
