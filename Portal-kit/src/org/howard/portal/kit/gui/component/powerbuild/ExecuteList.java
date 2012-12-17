package org.howard.portal.kit.gui.component.powerbuild;

import java.util.ArrayList;
import java.util.List;

import org.howard.portal.kit.gui.factory.BuildStatus;
import org.howard.portal.kit.gui.listener.ExecutionCallback;
import org.howard.portal.kit.util.ShellRunner;

/**
 * The purpose of this class is provide a controlled list of executed
 * items.
 */
public class ExecuteList {
    private List<ExecuteItem> list;

    private ExecutionCallback callback;

    /**
     * Creates a new instance of <code>ExecuteList</code>.
     */
    public ExecuteList() {
        list = new ArrayList<ExecuteItem>();
    }

    /**
     * @param text
     * @param absPath
     * @param status
     * @return Added execute Item
     */
    public ExecuteItem addItem(String text, String absPath, BuildStatus status) {
        ExecuteItem o = new ExecuteItem(text, absPath, status);
        int index = list.indexOf(o);
        if (index < 0)
            list.add(o);
        else
            throw new RuntimeException("Item has already exists.");
        return o;
    }

    /**
     * Returns the index of the first occurrence of the specified
     * element in this list, or -1 if this list does not contain the
     * element. More formally, returns the lowest index i such that
     * (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is
     * no such index.
     * 
     * @param text
     * @return index
     */
    public int indexOf(String text) {
        ExecuteItem o = new ExecuteItem(text, null, null);
        return list.indexOf(o);
    }

    /**
     * @param text
     * @param absPath
     * @param status
     */
    public void setItem(String text, String absPath, BuildStatus status) {
        ExecuteItem o = new ExecuteItem(text, absPath, status);
        int index = list.indexOf(o);
        if (index > -1)
            list.get(index).status = status;
        else
            throw new RuntimeException("Item has already exists.");
    }

    /**
     * @param text
     * @param absPath
     * @param status
     */
    public void removeItem(String text, String absPath, BuildStatus status) {
        ExecuteItem o = new ExecuteItem(text, absPath, status);
        int index = list.indexOf(o);
        if (index > -1)
            list.remove(o);
        else
            throw new RuntimeException("No such item can be removed.");
    }

    /**
     * @param callback
     */
    public void setCallback(ExecutionCallback callback) {
        this.callback = callback;
    }

    /**
     * 
     */
    public void startExec() {
        for (ExecuteItem item : list) {
            item.status = BuildStatus.EXECUTING;
            if (callback != null)
                callback.onChanged(item);
            ShellRunner shell = new ShellRunner();
            shell.setCmdline("mvn " + item.absolutePath);
            int run = shell.run();
            if (run == 0)
                item.status = BuildStatus.COMPLETED;
            else
                item.status = BuildStatus.ERROR;
        }
    }

    /**
     * @return list of execute item.
     */
    public List<ExecuteItem> getList() {
        return list;
    }

    /**
     * The purpose of this class is to provide a controlled executed
     * item
     */
    public class ExecuteItem {

        /**
         * <code>itemText</code>
         */
        String itemText;
        /**
         * <code>absolutePath</code>
         */
        String absolutePath;
        /**
         * <code>status</code>
         */
        BuildStatus status;

        /**
         * Creates a new instance of <code>ExecuteItem</code>.
         * 
         * @param t itemText
         * @param absPath absolutePath
         * @param s status
         */
        ExecuteItem(String t, String absPath, BuildStatus s) {
            itemText = t;
            absolutePath = absPath;
            status = s;
        }

        @Override
        public boolean equals(Object obj) {
            ExecuteItem i = (ExecuteItem) obj;
            if (!i.itemText.equals(itemText))
                return false;
            return true;
        }
    }
}
