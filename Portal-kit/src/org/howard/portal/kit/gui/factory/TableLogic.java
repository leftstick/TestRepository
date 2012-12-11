package org.howard.portal.kit.gui.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.howard.portal.kit.gui.listener.TextChanedCallback;

/**
 * The purpose of this class is to provide a simple logic module to
 * control Table
 */
public class TableLogic {
    private Table table;
    private boolean isHeadSet;
    private List<TableColumn> columns;
    private List<Boolean> editables;

    private TextChanedCallback callback;
    private Map<Integer, String> originalText;

    /**
     * Creates a new instance of <code>TableLogic</code>.
     * 
     * @param parent
     */
    public TableLogic(Composite parent) {
        table = new Table(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        columns = new ArrayList<TableColumn>();
        editables = new ArrayList<Boolean>();
        originalText = new HashMap<Integer, String>();
    }

    /**
     * @param text header text of current column
     * @param isEditable
     */
    public void addTableHeader(String text, boolean isEditable) {
        TableColumn column = new TableColumn(table, SWT.CENTER);
        columns.add(column);
        final int index = columns.size() - 1;
        column.setText(text);
        column.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                TableColumn tc = (TableColumn) e.getSource();
                if (columns.size() == 1) {
                    columns.get(0).setWidth(table.getSize().x);
                    return;
                }
                for (int i = 0; i < columns.size(); i++) {
                    if (i != index) {
                        columns.get(i).setWidth((table.getSize().x - tc.getWidth()) / (columns.size() - 1));
                    }
                }
            }
        });
        isHeadSet = true;
        editables.add(isEditable);
    }

    /**
     * @param texts content text of current column
     */
    public void addTableColumn(String... texts) {
        if (!isHeadSet)
            throw new NullPointerException("Header hasn't set yet.");
        TableItem item = new TableItem(table, SWT.LEFT);
        item.setText(texts);
    }

    /**
     * 
     */
    public void prepareTable() {
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent event) {
                Control old = editor.getEditor();
                if (old != null)
                    old.dispose();

                Point pt = new Point(event.x, event.y);

                final TableItem item = table.getItem(pt);
                if (item == null) {
                    return;
                }
                int column = -1;
                for (int i = 0, n = table.getColumnCount(); i < n; i++) {
                    Rectangle rect = item.getBounds(i);
                    if (rect.contains(pt)) {
                        column = i;
                        break;
                    }
                }

                if (!editables.get(column)) {
                    return;
                }
                if (!originalText.containsKey(column))
                    originalText.put(column, item.getText(column));
                final Text text = new Text(table, SWT.NONE);
                text.setText(item.getText(column));
                text.selectAll();
                text.setFocus();

                editor.minimumWidth = text.getBounds().width;

                editor.setEditor(text, item, column);

                final int col = column;
                text.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent me) {
                        item.setText(col, text.getText());
                        if (callback != null) {
                            if (!originalText.get(col).equals(text.getText()))
                                callback.onChanged(true);
                            else
                                callback.onChanged(false);
                        }
                    }
                });
            }
        });
        table.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                final int width = table.getSize().x;
                final int width_1 = width / 2;
                for (TableColumn col : columns) {
                    col.setWidth(width_1);
                }
            }
        });
        table.pack();
    }

    /**
     * @return result of this table
     */
    public List<List<String>> getResult() {
        List<List<String>> list = new ArrayList<List<String>>();
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            TableItem item = items[i];
            List<String> ilist = new ArrayList<String>();
            for (int j = 0; j < editables.size(); j++) {
                ilist.add(item.getText(j));
            }
            if (!ilist.isEmpty())
                list.add(ilist);
        }
        return list;
    }

    /**
     * Something
     */
    public void onShow() {
        originalText.clear();
    }

    /**
     * @param callback The callback to set.
     */
    public void setCallback(TextChanedCallback callback) {
        this.callback = callback;
    }

    /**
     * reset all the data that inclueded in the table
     * 
     * @param list
     */
    public void resetData(List<List<String>> list) {
        for (int i = 0; i < list.size(); i++) {
            List<String> ilist = list.get(i);
            TableItem item = table.getItem(i);
            for (int j = 0; j < ilist.size(); j++) {
                item.setText(j, ilist.get(j));
            }
        }
    }
}
