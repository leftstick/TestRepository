package org.nanfeng.ui;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.ActionGroup;
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
	private Login login;

	private TableViewer view_left;
	private TableViewer view_right;
	private ViewerFilter filter;
	private Action action_modify;
	private Action action_delete;
	private ObjectInfoDao objectinfodao;
	private final int index1 = 1;
	private final int index2 = 2;
	private final Sorter OBJECT_NAME_ASC = new Sorter(index1);
	private final Sorter OBJECT_NAME_DESC = new Sorter(-index1);
	private final Sorter DESCRIPTION_ASC = new Sorter(index2);
	private final Sorter DESCRIPTION_DESC = new Sorter(-index2);

	private final Sorter KEY_ASC = new Sorter(index1);
	private final Sorter KEY_DESC = new Sorter(-index1);
	private final Sorter VALUE_ASC = new Sorter(index2);
	private final Sorter VALUE_DESC = new Sorter(-index2);

	private UserInfo user;

	public Keeper() {
		super(null);
		addMenuBar();
		objectinfodao = new ObjectInfoDaoImpl();
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText("Password");
		parent.setSize(700, 500);
		parent.setLocation(Display.getCurrent().getClientArea().width / 2
				- parent.getShell().getSize().x / 2, Display.getCurrent()
				.getClientArea().height / 2 - parent.getSize().y / 2);
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
		Group group = new Group(main, SWT.NONE);
		group.setText("Information");
		group.setLayout(new GridLayout(1, true));

		SashForm sash = new SashForm(group, SWT.HORIZONTAL);
		GridLayout gl1 = new GridLayout(2, false);
		gl1.marginHeight = 0;
		gl1.marginWidth = 0;
		gl1.horizontalSpacing = 0;
		gl1.verticalSpacing = 0;
		gl1.marginLeft = 0;
		gl1.marginRight = 0;
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 350;
		gd.minimumWidth = 350;
		sash.setLayout(gl1);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		view_left = new TableViewer(sash, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles1 = { "Object name", "Description" };
		createColumns(titles1, view_left, new int[] { 130, 200 });
		view_left.getTable().getColumns()[0]
				.addSelectionListener(new SelectionAdapter() {
					boolean asc = true;

					public void widgetSelected(SelectionEvent e) {
						view_left.setSorter(asc ? OBJECT_NAME_ASC
								: OBJECT_NAME_DESC);
						asc = !asc;
					}
				});
		view_left.getTable().getColumns()[1]
				.addSelectionListener(new SelectionAdapter() {
					boolean asc = true;

					public void widgetSelected(SelectionEvent e) {
						view_left.setSorter(asc ? DESCRIPTION_ASC
								: DESCRIPTION_DESC);
						asc = !asc;
					}
				});
		view_left.getTable().setHeaderVisible(true);
		view_left.getTable().setLinesVisible(true);
		view_left.setContentProvider(new ObjectsLeftContentProvider());
		view_left.setLabelProvider(new ObjectsLeftLabelProvider());
		view_left.getControl().setLayoutData(gd);
		view_left.getTable().addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				final int width = view_left.getTable().getSize().x;
				final int width_1 = width / 2;
				view_left.getTable().getColumns()[0].setWidth(width_1);
				view_left.getTable().getColumns()[1].setWidth(width_1);
			}
		});
		view_left.getTable().addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
			}
		});
		PopMenu pm = new PopMenu();
		pm.fillContextMenu(new MenuManager());

		view_right = new TableViewer(sash, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles2 = { "Key", "Value" };
		createColumns(titles2, view_right, new int[] { 130, 200 });
		view_right.getTable().setHeaderVisible(true);
		view_right.getTable().setLinesVisible(true);
		view_right.setContentProvider(new ObjectsRightContentProvider());
		view_right.setLabelProvider(new ObjectsRightLabelProvider());
		view_right.getControl().setLayoutData(gd);
		view_right.getTable().addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				final int width = view_right.getTable().getSize().x;
				final int width_1 = width / 2;
				view_right.getTable().getColumns()[0].setWidth(width_1);
				view_right.getTable().getColumns()[1].setWidth(width_1);
			}
		});

		view_left.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				TableItem[] items = view_left.getTable().getSelection();
				if (items != null && items.length > 0) {
					TableItem item = items[0];
					view_right.setInput(((ObjectInfo) item.getData())
							.getObjectProperties());
					view_right.refresh();
					action_modify.setEnabled(true);
					action_delete.setEnabled(true);
				} else {
					action_modify.setEnabled(false);
					action_delete.setEnabled(false);
				}
			}
		});
		view_right.getTable().getColumns()[0]
				.addSelectionListener(new SelectionAdapter() {
					boolean asc = true;

					public void widgetSelected(SelectionEvent e) {
						view_right.setSorter(asc ? KEY_ASC : KEY_DESC);
						asc = !asc;
					}
				});
		view_right.getTable().getColumns()[1]
				.addSelectionListener(new SelectionAdapter() {
					boolean asc = true;

					public void widgetSelected(SelectionEvent e) {
						view_right.setSorter(asc ? VALUE_ASC : VALUE_DESC);
						asc = !asc;
					}
				});
		action_modify.setEnabled(false);
		action_delete.setEnabled(false);
		user = getData("userinfo", UserInfo.class);
		view_left.setInput(objectinfodao.get(user.getUser_name()));
	}

	private void createColumns(final String[] titles, final TableViewer viewer,
			int[] columnswidth) {
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(viewer.getTable(), SWT.CENTER);
			column.setText(titles[i]);
			column.setWidth(columnswidth[i]);
		}
	}

	protected MenuManager createMenuManager() {
		MenuManager main_menu = new MenuManager(null);
		MenuManager menu_file = new MenuManager("&File");
		MenuManager menu_option = new MenuManager("&Option");
		MenuManager menu_help = new MenuManager("&Help");
		main_menu.add(menu_file);
		main_menu.add(menu_option);
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
				ObjectInfo data = newinfo
						.getData("newObject", ObjectInfo.class);
				if (data != null) {
					((List<ObjectInfo>) view_left.getInput()).add(data);
					view_left.refresh();
				}
			}
		});
		menu_file.add(action_modify = new Action("&Modify@Ctrl+M",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/modify.jpg"));
			}

			public void run() {
				modify();
			}
		});
		menu_file.add(action_delete = new Action("&Delete@Ctrl+D",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/delete.jpg"));
			}

			public void run() {
				delete();
			}
		});
		menu_option.add(new Action("&ChangePwd@Ctrl+U", Action.AS_PUSH_BUTTON) {
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
		menu_file.add(new Action("&Logout@Ctrl+L", Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/logout.jpg"));
			}

			public void run() {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION
						| SWT.OK | SWT.CANCEL);
				mb.setText("Information");
				mb.setMessage("Are you sure to logout?");
				int res = mb.open();
				if (res == SWT.CANCEL)
					return;
				if (login == null) {
					login = new Login();
				}
				close();
				login.show(true);
			}
		});
		menu_file.add(new Separator());
		menu_file.add(new Action("&Exit", Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/exit.jpg"));
			}

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

	class PopMenu extends ActionGroup {
		public void fillContextMenu(IMenuManager menu) {
			MenuManager menuManager = (MenuManager) menu;
			menuManager.add(action_delete);
			menuManager.add(action_modify);
			Menu m = menuManager.createContextMenu(view_left.getTable());
			view_left.getTable().setMenu(m);
		}
	}

	@SuppressWarnings("unchecked")
	private void delete() {
		TableItem[] items = view_left.getTable().getSelection();
		ObjectInfo obj = null;
		if (items != null && items.length > 0) {
			MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION
					| SWT.OK | SWT.CANCEL);
			mb.setText("Information");
			mb.setMessage("Are you sure to delete?");
			int res = mb.open();
			if (res == SWT.CANCEL)
				return;
			try {
				obj = (ObjectInfo) items[0].getData();
				objectinfodao.delete(obj);
			} catch (Exception e) {
				mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
				mb.setText("Error");
				mb.setMessage(e.getMessage());
				mb.open();
				return;
			}
			mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
			mb.setText("Information");
			mb.setMessage("delete successful");
			mb.open();
			((List<ObjectInfo>) view_left.getInput()).remove(obj);
			view_left.refresh();
			view_right.setInput(null);
		}
	}

	private void modify() {
		if (modifyinfo == null) {
			modifyinfo = new ModifyObject(Keeper.this.getShell());
		}
		modifyinfo.setData("userinfo", user);
		modifyinfo.setData("object",
				view_left.getTable().getSelection()[0].getData());
		modifyinfo.show(true);
		view_left.refresh();
		view_right.refresh();
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

	class Sorter extends ViewerSorter {
		Collator comp = Collator.getInstance(Locale.CHINA);
		private int sortType;

		Sorter(int sortType) {
			this.sortType = sortType;

		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof ObjectInfo) {
				ObjectInfo o1 = (ObjectInfo) e1;
				ObjectInfo o2 = (ObjectInfo) e2;

				switch (sortType) {
				case index1: {
					String l1 = o1.getObject_name();
					String l2 = o2.getObject_name();
					return comp.compare(l1, l2);
				}
				case -index1: {
					String l1 = o1.getObject_name();
					String l2 = o2.getObject_name();
					return comp.compare(l2, l1);
				}
				case index2: {
					String s1 = o1.getObject_description();
					String s2 = o2.getObject_description();
					return comp.compare(s1, s2);
				}
				case -index2: {
					String s1 = o1.getObject_description();
					String s2 = o2.getObject_description();
					return comp.compare(s2, s1);
				}
				}
			} else if (e1 instanceof ObjectProperty) {

				ObjectProperty o1 = (ObjectProperty) e1;
				ObjectProperty o2 = (ObjectProperty) e2;

				switch (sortType) {
				case index1: {
					String l1 = o1.key;
					String l2 = o2.key;
					return comp.compare(l1, l2);
				}
				case -index1: {
					String l1 = o1.key;
					String l2 = o2.key;
					return comp.compare(l2, l1);
				}
				case index2: {
					String s1 = o1.value;
					String s2 = o2.value;
					return comp.compare(s1, s2);
				}
				case -index2: {
					String s1 = o1.value;
					String s2 = o2.value;
					return comp.compare(s2, s1);
				}
				}

			}
			return 0;
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
}
