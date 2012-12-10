package org.howard.portal.kit.gui.util;

import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

/**
 * The purpose of this class is to provide a selected item callback
 */
public interface SelectCallback {
    /**
     * @param treeItem
     */
    public void onSelected(List<TreeItem> treeItem);

    /**
     * @param treeItem
     */
    public void onCanceled(List<TreeItem> treeItem);
}
