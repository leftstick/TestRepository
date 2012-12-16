package org.howard.portal.kit.gui.listener;

import org.howard.portal.kit.gui.component.powerbuild.ExecuteList.ExecuteItem;

/**
 * The purpose of this class is to provide a state changed callback.
 */
public interface ExecutionCallback {
    /**
     * @param item
     */
    public void onChanged(ExecuteItem item);
}
