package org.howard.portal.kit.gui.util;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The purpose of this class is to provide simple API interface to
 * Tree
 */
public class TreeLogic {
    private Tree tree;
    private TreeItem rootItem;
    private Listener listener;
    private FileFilter filter;

    /**
     * Creates a new instance of <code>TreeLogic</code>.
     */
    public TreeLogic() {
        filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile())
                    return false;
                File[] subFiles = pathname.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File subname) {
                        return subname.isFile() && "pom.xml".equals(subname.getName());
                    }
                });
                return subFiles.length == 1;
            }
        };
    }

    /**
     * create a tree
     * 
     * @param parent
     */
    public void createTree(Composite parent) {
        this.createTree(parent, null);
    }

    /**
     * create a tree with specified root
     * 
     * @param parent
     * @param rootFile
     */
    public void createTree(Composite parent, File rootFile) {
        ObjectUtil.checkNull(rootFile, "rootFile cannot be null when creating Tree");
        if (tree == null)
            tree = new Tree(parent, SWT.BORDER | SWT.CHECK);
        // Add root file to the tree.
        this.setRoot(rootFile);
    }

    private void setListener() {
        tree.addListener(SWT.Expand, listener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                // Get the event-source  
                TreeItem parent = (TreeItem) event.item;

                // Dispose the empty node  
                TreeItem[] items = parent.getItems();
                for (TreeItem item : items) {
                    item.dispose();
                }

                File parentFile = (File) parent.getData();
                File[] files = parentFile.listFiles(filter);

                for (File file : files) {
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    item.setText(file.getName());
                    item.setData(file);

                    if (hasSubElement(file)) {
                        new TreeItem(item, SWT.NONE);
                    }
                }
            }
        });

        tree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    TreeItem item = (TreeItem) event.item;
                    boolean state = item.getChecked();
                    checkChildren(item, state);
                    checkParents(item, state);
                }
            }
        });
    }

    private static void checkChildren(TreeItem item, boolean state) {
        item.setChecked(state);
        TreeItem[] items = item.getItems();
        for (TreeItem treeItem : items) {
            checkChildren(treeItem, state);
        }
    }

    private static void checkParents(TreeItem item, boolean state) {
        item.setChecked(state);
        TreeItem parent = item.getParentItem();
        if (parent != null) {
            checkParents(parent, state);
        }
    }

    private boolean hasSubElement(File file) {
        if (file.isFile())
            return false;
        if (file.listFiles(filter).length > 0)
            return true;
        return false;
    }

    /**
     * set root to tree
     * 
     * @param rootFile
     */
    public void setRoot(File rootFile) {
        ObjectUtil.checkNull(rootFile, "rootFile cannot be null when setting rootFile");
        if (rootItem == null) {
            rootItem = new TreeItem(tree, SWT.NONE);
        }
        rootItem.setText(rootFile.getName());
        rootItem.setData(rootFile);
        if (listener != null)
            tree.removeListener(SWT.Expand, listener);
        if (hasSubElement(rootFile)) {
            // Add a empty sub-node to make the node can be expanded.  
            new TreeItem(rootItem, SWT.NONE);
            setListener();
        }
    }

}
