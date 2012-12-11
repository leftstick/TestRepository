package org.howard.portal.kit.gui.listener;

import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

/**
 * The purpose of this class is to provide a selected item callback to
 * Tree
 */
public interface SelectCallback {
    /**
     * Selected TreeItems
     * 
     * @param treeItem
     */
    public void onSelected(List<TreeItem> treeItem);

    /**
     * Removed TreeItems
     * 
     * @param treeItem
     */
    public void onCanceled(List<TreeItem> treeItem);
}
