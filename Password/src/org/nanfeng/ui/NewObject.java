package org.nanfeng.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.ActionGroup;
import org.nanfeng.bean.impl.ObjectInfo;
import org.nanfeng.bean.impl.ObjectInfo.ObjectProperty;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.ObjectInfoDao;
import org.nanfeng.dao.impl.HiloDao;
import org.nanfeng.dao.impl.ObjectInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;
import org.nanfeng.util.ResourceUtil;

public class NewObject extends BaseDialog {
	private TableViewer view;

	private Text text_objectName;
	private Text text_description;

	private ObjectInfoDao objectinfodao;

	private Shell p;

	public NewObject(Shell parent) {
		super(parent);
		p = parent;
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText(
				ResourceUtil.instance().getString(simpleClassName + ".title"));
		parent.setSize(350, 370);
		parent.setLocation(
				(Display.getCurrent().getClientArea().width - p.getBounds().width)
						/ 4 + p.getBounds().x,
				(Display.getCurrent().getClientArea().height - p.getBounds().height)
						/ 4 + p.getBounds().y);
		Composite main = new Composite(parent, SWT.CENTER);
		GridLayout gl1 = new GridLayout(2, false);
		gl1.marginTop = 5;
		gl1.marginBottom = 5;
		gl1.marginLeft = 5;
		gl1.marginRight = 5;
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 200;
		data2.minimumWidth = 200;
		data2.heightHint = 20;
		data2.minimumHeight = 20;
		main.setLayout(gl1);

		Label label_objectName = new Label(main, SWT.LEFT);
		label_objectName.setText(ResourceUtil.instance().getString(
				simpleClassName + ".objname"));
		label_objectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		text_objectName = new Text(main, SWT.LEFT | SWT.BORDER);
		text_objectName.setLayoutData(data2);

		Label label_description = new Label(main, SWT.LEFT);
		label_description.setText(ResourceUtil.instance().getString(
				simpleClassName + ".description"));
		label_description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		text_description = new Text(main, SWT.LEFT | SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
		data3.widthHint = 200;
		data3.minimumWidth = 200;
		data3.heightHint = 60;
		data3.minimumHeight = 60;
		text_description.setLayoutData(data3);

		GridData data4 = new GridData(GridData.FILL_HORIZONTAL);
		data4.horizontalSpan = 2;
		data4.widthHint = 200;
		data4.minimumWidth = 200;
		data4.minimumHeight = 150;
		data4.heightHint = 150;
		view = new TableViewer(main, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles1 = {
				ResourceUtil.instance().getString(simpleClassName + ".key"),
				ResourceUtil.instance().getString(simpleClassName + ".value") };
		view.getTable().setHeaderVisible(true);
		view.getTable().setLinesVisible(true);
		view.getControl().setLayoutData(data4);
		view.setContentProvider(new ObjectsContentProvider());
		view.setLabelProvider(new ObjectsLabelProvider());
		createColumns(titles1, view, new int[] { 175, 175 });
		view.setCellModifier(new CellModifier());
		view.getTable().getColumns()[0]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view.getTable().getSize().x;
						view.getTable().getColumns()[1].setWidth(width
								- tc.getWidth());
					}
				});
		view.getTable().getColumns()[1]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view.getTable().getSize().x;
						view.getTable().getColumns()[0].setWidth(width
								- tc.getWidth());
					}
				});
		view.getTable().addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TableColumn[] columns = view.getTable().getColumns();
				for (TableColumn cl : columns) {
					cl.setWidth(view.getTable().getSize().x / 2);
				}
			}
		});
		Composite bottom = new Composite(main, SWT.RIGHT_TO_LEFT);
		bottom.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button button_cancel = new Button(bottom, SWT.PUSH);
		button_cancel.setText(ResourceUtil.instance().getString(
				simpleClassName + ".cancel"));
		button_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setData("newObject", null);
				close();
			}
		});

		Button button_ok = new Button(bottom, SWT.PUSH);
		button_ok.setText(ResourceUtil.instance().getString(
				simpleClassName + ".submit"));
		button_ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});

		bottom.setTabList(new Control[] { button_ok, button_cancel });
		main.setTabList(new Control[] { text_objectName, text_description,
				view.getControl(), bottom });
		parent.setTabList(new Control[] { main });

		GridData data5 = new GridData(GridData.FILL_HORIZONTAL);
		data5.horizontalSpan = 2;
		data5.widthHint = 200;
		data5.minimumWidth = 200;
		bottom.setLayoutData(data5);

		PopMenu pm = new PopMenu();
		pm.fillContextMenu(new MenuManager());
	}

	class PopMenu extends ActionGroup {
		public void fillContextMenu(IMenuManager menu) {
			MenuManager menuManager = (MenuManager) menu;
			menuManager
					.add(new Action("&"
							+ ResourceUtil.instance().getString(
									simpleClassName + ".delete"),
							Action.AS_PUSH_BUTTON) {
						public ImageDescriptor getImageDescriptor() {
							return ImageDescriptor.createFromURL(this
									.getClass().getResource("icon/delete.jpg"));
						}

						public void run() {
							delete();
						}
					});
			Menu m = menuManager.createContextMenu(view.getTable());
			view.getTable().setMenu(m);
		}
	}

	@SuppressWarnings("unchecked")
	private void delete() {
		TableItem[] items = view.getTable().getSelection();
		if (items != null && items.length > 0) {
			MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION
					| SWT.OK | SWT.CANCEL);
			mb.setText(ResourceUtil.instance().getString("common.information"));
			mb.setMessage(ResourceUtil.instance().getString(
					simpleClassName + ".delete.notify"));
			int res = mb.open();
			if (res == SWT.CANCEL)
				return;
			if (view.getTable().getItems().length == 1)
				return;
			ObjectProperty op = (ObjectProperty) items[0].getData();
			if ((op.key == null || op.key.trim().length() == 0)
					&& (op.value == null || op.value.trim().length() == 0))
				return;
			((List<ObjectProperty>) view.getInput()).remove(items[0].getData());
			mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
			mb.setText(ResourceUtil.instance().getString("common.information"));
			mb.setMessage(ResourceUtil.instance().getString(
					"common.delete.successful"));
			mb.open();
			view.refresh();
		}
	}

	@SuppressWarnings("unchecked")
	private void createColumns(final String[] titles, final TableViewer viewer,
			int[] columnswidth) {
		for (int i = 0; i < columnswidth.length; i++) {
			TableColumn column = new TableColumn(viewer.getTable(), SWT.CENTER);
			column.setText(titles[i]);
			column.setWidth(columnswidth[i]);
		}
		viewer.setColumnProperties(titles);
		CellEditor[] editors = new TextCellEditor[titles.length];
		for (int i = 0; i < editors.length; i++) {
			editors[i] = new TextCellEditor(viewer.getTable());
		}
		viewer.setCellEditors(editors);

		List<ObjectProperty> list = (List<ObjectProperty>) view.getInput();
		if (list == null) {
			list = new LinkedList<ObjectProperty>();
			view.setInput(list);
		}
		((List<ObjectProperty>) view.getInput())
				.add(new ObjectProperty("", ""));
		view.refresh();
	}

	private void save() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText(ResourceUtil.instance().getString("common.error"));
		if (text_objectName.getText().trim().length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					simpleClassName + ".objname.empty"));
			mb.open();
			return;
		}
		if (text_description.getText().trim().length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					simpleClassName + ".objdesc.empty"));
			mb.open();
			return;
		}
		int searchWide = 0;
		TableItem[] items = view.getTable().getItems();
		ObjectProperty o = (ObjectProperty) items[items.length - 1].getData();
		if ((o.key == null || o.key.trim().length() == 0)
				&& (o.value == null || o.value.trim().length() == 0))
			searchWide = items.length - 1;
		else
			searchWide = items.length;

		for (int i = 0; i < searchWide; i++) {
			ObjectProperty op = (ObjectProperty) items[i].getData();
			if (op.key == null || op.key.trim().length() == 0
					|| op.value == null || op.value.trim().length() == 0) {
				mb.setMessage(ResourceUtil.instance().getString(
						simpleClassName + ".objpro.empty"));
				mb.open();
				return;
			}
		}
		ObjectInfo obj = new ObjectInfo();
		obj.setObject_id(HiloDao.getInstance().getValue());
		obj.setObject_name(text_objectName.getText());
		obj.setObject_description(text_description.getText());
		obj.setUser_name(getData("userinfo", UserInfo.class).getUser_name());
		for (int i = 0; i < searchWide; i++) {
			ObjectProperty op = (ObjectProperty) items[i].getData();
			obj.addProperty(op);
		}
		if (obj.getObjectProperties().isEmpty()) {
			mb.setMessage(ResourceUtil.instance().getString(
					simpleClassName + ".objpro.empty"));
			mb.open();
			return;
		}
		if (objectinfodao == null)
			objectinfodao = new ObjectInfoDaoImpl();
		try {
			objectinfodao.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
			mb.setMessage(e.getMessage());
			mb.open();
			return;
		}
		setData("newObject", obj);
		mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
		mb.setText(ResourceUtil.instance().getString("common.information"));
		mb.setMessage(ResourceUtil.instance().getString(
				"common.save.successful"));
		mb.open();
		close();
	}

	class ObjectsContentProvider implements IStructuredContentProvider {
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

	class ObjectsLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ObjectProperty) {
				ObjectProperty obj = (ObjectProperty) element;
				if (columnIndex == 0) {
					return obj.key;
				} else if (columnIndex == 1) {
					return obj.value;
				}
			}
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class CellModifier implements ICellModifier {
		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			ObjectProperty entry = (ObjectProperty) element;
			if (property.equals(ResourceUtil.instance().getString(
					simpleClassName + ".key")))
				return entry.key;
			else if (property.equals(ResourceUtil.instance().getString(
					simpleClassName + ".value")))
				return entry.value;
			return null;
		}

		@SuppressWarnings("unchecked")
		public void modify(Object element, String property, Object value) {

			TableItem item = (TableItem) element;
			ObjectProperty entry = (ObjectProperty) item.getData();
			if (property.equals(ResourceUtil.instance().getString(
					simpleClassName + ".key"))) {
				entry.key = value.toString();
			} else if (property.equals(ResourceUtil.instance().getString(
					simpleClassName + ".value"))) {
				entry.value = value.toString();
			}
			view.update(entry, null);
			TableItem[] items = view.getTable().getItems();
			ObjectProperty op = (ObjectProperty) items[items.length - 1]
					.getData();
			if (op.key.trim().length() > 0 && op.value.trim().length() > 0) {
				List<ObjectProperty> list = (List<ObjectProperty>) view
						.getInput();
				if (list == null) {
					list = new LinkedList<ObjectProperty>();
					view.setInput(list);
				}
				((List<ObjectProperty>) view.getInput())
						.add(new ObjectProperty("", ""));
				view.refresh();
			}
		}
	}
}
