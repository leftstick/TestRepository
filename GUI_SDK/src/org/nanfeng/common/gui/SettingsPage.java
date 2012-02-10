package org.nanfeng.common.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.nanfeng.common.state.ParameterChecker;
import org.nanfeng.common.state.Submit;

public class SettingsPage extends Composite {

	public static final int TEXT_COLUMN = 0;
	public static final int RADIO_COLUMN = 1;

	private Button button_ok;
	private TableViewer tableView_parameter;
	private Table table_parameter;
	private String text1;
	private String text2;
	private List<ObjectProperty> parameters;
	private ParameterChecker checker;
	private int style;

	public SettingsPage(Composite parent, String column1Text,
			String column2Text, int style) {
		super(parent, SWT.NONE);
		text1 = column1Text;
		text2 = column2Text;
		this.style = style;
		init();
	}

	private void init() {
		parameters = new ArrayList<ObjectProperty>();
		setLayout(new GridLayout(1, true));
		GridData fill_horizontal = new GridData(GridData.FILL_HORIZONTAL);
		setLayoutData(fill_horizontal);
		tableView_parameter = new TableViewer(this, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		table_parameter = tableView_parameter.getTable();
		table_parameter.setHeaderVisible(true);
		table_parameter.setLinesVisible(true);
		table_parameter.getHorizontalBar().setEnabled(false);
		table_parameter.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumn column1 = new TableColumn(table_parameter, SWT.CENTER);
		column1.setText(text1);
		column1.pack();
		final TableColumn column2 = new TableColumn(table_parameter, SWT.CENTER);
		column2.setText(text2);
		column2.pack();
		column1.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TableColumn tc = (TableColumn) e.getSource();
				final int width = tableView_parameter.getTable().getSize().x;
				column2.setWidth(width - tc.getWidth());
			}
		});

		column2.setResizable(false);
		table_parameter.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TableColumn[] columns = table_parameter.getColumns();
				for (TableColumn cl : columns) {
					cl.setWidth(table_parameter.getSize().x / 2);
				}
			}
		});

		tableView_parameter.setColumnProperties(new String[] { text1, text2 });
		CellEditor editor1 = new TextCellEditor(table_parameter);
		CellEditor editor2 = null;
		switch (style) {
		case TEXT_COLUMN:
			editor2 = new TextCellEditor(table_parameter);
			break;
		case RADIO_COLUMN:
			editor2 = new CheckboxCellEditor(table_parameter);
			break;
		default:
			throw new RuntimeException("error style " + style);
		}
		tableView_parameter
				.setCellEditors(new CellEditor[] { editor1, editor2 });
		tableView_parameter.setContentProvider(new SimpleContentProvider());
		tableView_parameter.setLabelProvider(new SimpleLabelProvider());
		tableView_parameter.setCellModifier(new Column2Modifier());
		tableView_parameter.setInput(parameters);
	}

	public void addLineText(String label, String value) {
		Assert.isTrue(style == TEXT_COLUMN, "text line is not available");
		ObjectProperty op = new ObjectProperty(label, value);
		parameters.add(op);
		tableView_parameter.refresh();
	}

	public void addLineChecked(String label) {
		Assert.isTrue(style == RADIO_COLUMN, "checked line is not available");
		ObjectProperty op = new ObjectProperty(label, false);
		parameters.add(op);
		tableView_parameter.refresh();
	}

	public void resetLines() {
		parameters.clear();
		tableView_parameter.refresh();
	}

	public void setChecker(ParameterChecker checker) {
		this.checker = checker;
	}

	public void setSubmit(String text, final Submit sub) {
		button_ok = new Button(this, SWT.PUSH);
		button_ok.setText(text);
		button_ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				submit(sub);
			}
		});
		GridData data2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data2.horizontalSpan = 2;
		data2.widthHint = 60;
		data2.minimumWidth = 60;
		button_ok.setLayoutData(data2);
	}

	private void submit(final Submit sub) {
		switch (style) {
		case TEXT_COLUMN:
			List<Object> list = new ArrayList<Object>();
			boolean f = false;
			for (int i = 0; i < parameters.size(); i++) {
				if (checker != null) {
					f = checker.check(i, parameters.get(i).value);
					if (!f)
						return;
					list.add(parameters.get(i).value);
				} else
					list.add(parameters.get(i).value);
			}
			sub.run(list);
			break;
		case RADIO_COLUMN:
			for (int i = 0; i < parameters.size(); i++) {
				if ((Boolean) parameters.get(i).value) {
					sub.run(i);
					break;
				}
			}
			break;
		}
	}

	class Column2Modifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			if (property.equals(text1))
				return false;
			return true;
		}

		public Object getValue(Object element, String property) {
			ObjectProperty entry = (ObjectProperty) element;
			if (property.equals(text1))
				return entry.key;
			else if (property.equals(text2))
				return entry.value;
			return null;
		}

		public void modify(Object element, String property, Object value) {
			TableItem item = (TableItem) element;
			ObjectProperty entry = (ObjectProperty) item.getData();
			if (property.equals(text1)) {
				entry.key = value.toString();
			} else if (property.equals(text2)) {
				entry.value = value;
				if (style == RADIO_COLUMN) {
					for (ObjectProperty oo : parameters) {
						oo.value = false;
					}
					tableView_parameter.refresh();
					entry.value = value;
				}
			}
			tableView_parameter.update(entry, null);
		}
	}

	class SimpleContentProvider implements IStructuredContentProvider {
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			List<ObjectProperty> list = null;
			try {
				list = (List<ObjectProperty>) inputElement;
			} catch (Exception e) {
				return new ObjectProperty[0];
			}
			return list.toArray(new ObjectProperty[list.size()]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class SimpleLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ObjectProperty) {
				ObjectProperty obj = (ObjectProperty) element;
				switch (columnIndex) {
				case 0:
					return obj.key;
				case 1:
					if (style == RADIO_COLUMN) {
						return (Boolean) obj.value == true ? "¡Ì" : "";
					}
					return obj.value.toString();
				default:
					return null;
				}
			}
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class ObjectProperty implements Serializable {
		/**
		 */
		private static final long serialVersionUID = 2447812285760065930L;
		public String key;
		public Object value;

		public ObjectProperty(String k, Object v) {
			key = k;
			value = v;
		}
	}
}
