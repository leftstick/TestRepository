package org.nanfeng.ui;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.ObjectInfo;
import org.nanfeng.bean.impl.ObjectInfo.ObjectProperty;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.ObjectInfoDao;
import org.nanfeng.dao.impl.ObjectInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;

public class Keeper extends BaseDialog {
	private About about;
	private NewObject newinfo;
	private ModifyObject modifyinfo;
	private ChangePwd changePwd;

	private TableViewer view_left;
	private TableViewer view_right;
	private ViewerFilter filter;
	private Action action_modify;
	private ObjectInfoDao objectinfodao;

	private UserInfo user;

	public Keeper() {
		super(null);
		addMenuBar();
		objectinfodao = new ObjectInfoDaoImpl();
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText("Password");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(1, true);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		main.setLayout(gl);

		filter = null;
		final Text text_search = new Text(main, SWT.LEFT);
		text_search.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text_search.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (filter != null)
					view_left.removeFilter(filter);
				filter = new ObjectsFilter(text_search.getText());
				view_left.addFilter(filter);
			}
		});

		Composite bottom = new Composite(main, SWT.NONE);
		GridLayout gl1 = new GridLayout(2, false);
		gl1.marginHeight = 0;
		gl1.marginWidth = 0;
		gl1.horizontalSpacing = 0;
		gl1.verticalSpacing = 0;
		gl1.marginLeft = 0;
		gl1.marginRight = 0;
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		bottom.setLayout(gl1);
		bottom.setLayoutData(new GridData(GridData.FILL_BOTH));

		view_left = new TableViewer(bottom, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles1 = { "Object name", "Description" };
		createColumns(titles1, view_left);
		view_left.getTable().setHeaderVisible(true);
		view_left.getTable().setLinesVisible(true);
		view_left.setContentProvider(new ObjectsLeftContentProvider());
		view_left.setLabelProvider(new ObjectsLeftLabelProvider());
		view_left.setInput(objectinfodao.get());
		view_left.getControl().setLayoutData(gd);

		view_right = new TableViewer(bottom, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles2 = { "Key", "Value" };
		createColumns(titles2, view_right);
		view_right.getTable().setHeaderVisible(true);
		view_right.getTable().setLinesVisible(true);
		view_right.setContentProvider(new ObjectsRightContentProvider());
		view_right.setLabelProvider(new ObjectsRightLabelProvider());
		view_right.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		view_left.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				TableItem[] items = view_left.getTable().getSelection();
				if (items != null && items.length > 0) {
					TableItem item = items[0];
					view_right.setInput(((ObjectInfo) item.getData())
							.getObjectProperties());
					view_right.refresh();
					action_modify.setEnabled(true);
				} else
					action_modify.setEnabled(false);
			}
		});
		action_modify.setEnabled(false);
		user = getData("userinfo", UserInfo.class);
	}

	private void createColumns(final String[] titles, final TableViewer viewer) {
		for (String title : titles) {
			TableColumn column = new TableColumn(viewer.getTable(), SWT.CENTER);
			column.setText(title);
			column.pack();
		}

	}

	protected MenuManager createMenuManager() {
		MenuManager main_menu = new MenuManager(null);
		MenuManager menu_file = new MenuManager("&File");
		MenuManager menu_help = new MenuManager("&Help");
		main_menu.add(menu_file);
		main_menu.add(menu_help);
		menu_file.add(new Action("&New@Ctrl+N", Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/add.jpg"));
			}

			@SuppressWarnings("unchecked")
			public void run() {
				if (newinfo == null) {
					newinfo = new NewObject(Keeper.this.getShell());
				}
				newinfo.setData("userinfo", user);
				newinfo.show(true);
				ObjectInfo data = newinfo.getData("newObject", ObjectInfo.class);
				((List<ObjectInfo>)view_left.getInput()).add(data);
				view_left.refresh();
			}
		});
		menu_file.add(action_modify = new Action("&Modify@Ctrl+M",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/modify.jpg"));
			}

			public void run() {
				if (modifyinfo == null) {
					modifyinfo = new ModifyObject(Keeper.this.getShell());
				}
				modifyinfo.setData("userinfo", user);
				modifyinfo.setData("object", view_left.getTable()
						.getSelection()[0].getData());
				modifyinfo.show(true);
				view_left.refresh();
				view_right.refresh();
			}
		});
		menu_file.add(new Action("&ChangePwd@Ctrl+U", Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/change.jpg"));
			}

			public void run() {
				if (changePwd == null) {
					changePwd = new ChangePwd(Keeper.this.getShell());
				}
				changePwd.setData("userinfo", user);
				changePwd.show(true);
			}
		});
		menu_file.add(new Separator());
		menu_file.add(new Action("&Exit", Action.AS_PUSH_BUTTON) {
			public void run() {
				close();
			}
		});

		menu_help.add(new Action("&About", Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/about.jpg"));
			}

			public void run() {
				if (about == null) {
					about = new About(Keeper.this.getShell());
					about.setData("keeper", Keeper.this);
				}
				about.show(true);
			}
		});
		return main_menu;

	}

	class ObjectsFilter extends ViewerFilter {
		String text;

		ObjectsFilter(String t) {
			text = t;
		}

		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			ObjectInfo info = (ObjectInfo) element;
			if (info.getObject_name().indexOf(text) >= 0
					|| info.getObject_description().indexOf(text) >= 0)
				return true;
			return false;
		}

	}

	class ObjectsLeftContentProvider implements IStructuredContentProvider {
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			List<ObjectInfo> list = null;
			try {
				list = (List<ObjectInfo>) inputElement;
			} catch (Exception e) {
				return new ObjectInfo[0];
			}
			return list.toArray(new ObjectInfo[list.size()]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class ObjectsRightContentProvider implements IStructuredContentProvider {
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

	class ObjectsLeftLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ObjectInfo) {
				ObjectInfo obj = (ObjectInfo) element;
				if (columnIndex == 0) {
					return obj.getObject_name();
				} else if (columnIndex == 1) {
					return obj.getObject_description();
				}
			}
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class ObjectsRightLabelProvider extends LabelProvider implements
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

	// protected CoolBarManager createCoolBarManager(int style) {
	// CoolBarManager coolBar = new CoolBarManager(style);
	// createCoolBars(style);
	// coolBar.add(fileBar);
	// coolBar.add(editBar);
	// coolBar.add(formatBar);
	// return coolBar;
	// }
	//
	// private void createCoolBars(int style) {
	// ToolBarManager tbm = new ToolBarManager(style);
	// fileBar = new ToolBarContributionItem(tbm, "file");
	// tbm = new ToolBarManager(style);
	// editBar = new ToolBarContributionItem(tbm, "edit");
	// tbm = new ToolBarManager(style);
	// formatBar = new ToolBarContributionItem(tbm, "format");
	// }

	public static void main(String[] args) {
		Keeper login = new Keeper();
		login.show(true);
	}
}
