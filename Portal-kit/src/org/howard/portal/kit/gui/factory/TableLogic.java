package org.howard.portal.kit.gui.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    private OriginalText originalText;

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
        originalText = new OriginalText();
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

    private void resetOriginalText() {
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < editables.size(); j++) {
                if (editables.get(j))
                    originalText.setSource(new Position(i, j), "");
            }
        }
    }

    /**
     * 
     */
    public void prepareTable() {
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        resetOriginalText();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent event) {
                Control old = editor.getEditor();
                if (old != null)
                    old.dispose();
                Point pt = new Point(event.x, event.y);
                final TableItem item = table.getItem(table.getSelectionIndex());
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

                if (column == -1 || !editables.get(column)) {
                    return;
                }
                final Position position = new Position(table.getSelectionIndex(), column);
                final String txt = item.getText(column);
                if (!originalText.containsKey(position)) {
                    System.out.println("run it heere");
                    originalText.setSource(position, txt);
                }
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
                            System.out.println("or[" + originalText.get(position) + "]" + "txt[" + text.getText() + "]");
                            if (!originalText.get(position).equals(text.getText()))
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
     * reset all the data.
     */
    public void onShow() {
        for (Entry<Position, String> entry : originalText.getOriginalText().entrySet()) {
            TableItem item = table.getItem(entry.getKey().row);
            item.setText(entry.getKey().column, entry.getValue());
        }
    }

    /**
     * @param callback The callback to set.
     */
    public void setCallback(TextChanedCallback callback) {
        this.callback = callback;
    }

    /**
     * reset all the data that included in the table
     * 
     * @param list
     */
    public void resetData(List<List<String>> list) {
        for (int i = 0; i < list.size(); i++) {
            List<String> ilist = list.get(i);
            TableItem item = table.getItem(i);
            for (int j = 0; j < ilist.size(); j++) {
                item.setText(j, ilist.get(j));
                Position p = new Position(i, j);
                if (originalText.containsKey(p))
                    originalText.setSource(p, ilist.get(j));
            }
        }
    }

    /**
     * The purpose of this class is to provide originalText source
     * control
     */
    class OriginalText {
        private Map<Position, String> source;

        /**
         * Creates a new instance of <code>OriginalText</code>.
         */
        OriginalText() {
            source = new HashMap<Position, String>();
        }

        /**
         * @param p position
         * @param str text
         */
        void setSource(Position p, String str) {
            for (Entry<Position, String> entry : source.entrySet()) {
                if (entry.getKey().column == p.column && entry.getKey().row == p.row) {
                    entry.setValue(str);
                    return;
                }
            }
            source.put(p, str);
        }

        /**
         * @param p position
         * @return if key contains
         */
        boolean containsKey(Position p) {
            for (Entry<Position, String> entry : source.entrySet()) {
                if (entry.getKey().column == p.column && entry.getKey().row == p.row)
                    return true;
            }
            return false;
        }

        /**
         * @param p position
         * @return text
         */
        String get(Position p) {
            for (Entry<Position, String> entry : source.entrySet()) {
                if (entry.getKey().column == p.column && entry.getKey().row == p.row)
                    return entry.getValue();
            }
            return null;
        }

        /**
         * @return originalText source
         */
        public Map<Position, String> getOriginalText() {
            return source;
        }
    }

    /**
     * The purpose of this class is to provide position information
     */
    class Position {
        /**
         * <code>row</code> of Table
         */
        int row;
        /**
         * <code>column</code> of Table
         */
        int column;

        /**
         * Creates a new instance of <code>Position</code>.
         * 
         * @param row
         * @param col column
         */
        public Position(int row, int col) {
            this.row = row;
            this.column = col;
        }
    }
}
