package org.howard.portal.kit.gui.factory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.howard.portal.kit.gui.listener.SelectCallback;
import org.howard.portal.kit.util.ObjectUtil;

/**
 * The purpose of this class is to provide simple API interface to
 * Tree
 */
public class TreeLogic {
    private Tree tree;
    private TreeItem rootItem;
    private Listener listener;
    private FileFilter filter;
    private SelectCallback callback;

    private List<TreeItem> selection;

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
        selection = new ArrayList<TreeItem>();
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
        if (tree == null)
            tree = new Tree(parent, SWT.BORDER | SWT.CHECK);
        // Add root file to the tree.
        if (rootFile != null)
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
                    if (item.getData() == null)
                        item.dispose();
                }
            }
        });

        tree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    TreeItem item = (TreeItem) event.item;
                    boolean state = item.getChecked();
                    selection.clear();
                    checkChildren(item, state);
                    if (callback != null) {
                        if (state)
                            callback.onSelected(selection);
                        else
                            callback.onCanceled(selection);
                    }

                    checkParents(item, state);
                }
            }
        });
    }

    private String getAbsolutePath(TreeItem item) {
        return ((File) item.getData()).getAbsolutePath();
    }

    private void checkChildren(TreeItem item, boolean state) {
        item.setChecked(state);
        TreeItem[] items = item.getItems();
        if (items.length == 0 && callback != null) {
            if (!"".equals(item.getText())) {
                item.setData("path", getAbsolutePath(item));
                if (state) {
                    selection.add(item);
                } else {
                    selection.add(item);
                }
            }
        }
        for (TreeItem treeItem : items) {
            checkChildren(treeItem, state);
        }
    }

    private void checkParents(TreeItem item, boolean state) {
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
     * specified callback will be executed when treeItem is selected.
     * 
     * @param callback
     */
    public void setOnSelectCallback(SelectCallback callback) {
        this.callback = callback;
    }

    private void initAllTreeItems(TreeItem treeItem) {
        // Get the event-source  
        TreeItem parent = treeItem;

        File parentFile = (File) parent.getData();
        File[] files = parentFile.listFiles(filter);

        for (File file : files) {
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText(file.getName());
            item.setData(file);

            if (hasSubElement(file)) {
                new TreeItem(item, SWT.NONE);
                initAllTreeItems(item);
            }
        }

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
        rootItem.removeAll();
        rootItem.setText(rootFile.getName());
        rootItem.setData(rootFile);
        if (listener != null)
            tree.removeListener(SWT.Expand, listener);
        if (hasSubElement(rootFile)) {
            // Add a empty sub-node to make the node can be expanded.  
            new TreeItem(rootItem, SWT.NONE);
            setListener();
            initAllTreeItems(rootItem);
        }
    }

}
