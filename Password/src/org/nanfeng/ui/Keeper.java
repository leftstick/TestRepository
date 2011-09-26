package org.nanfeng.ui;

import java.io.IOException;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
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
import org.nanfeng.util.DataOperator;
import org.nanfeng.util.DialogFactory;
import org.nanfeng.util.MessageUtil;
import org.nanfeng.util.ResourceUtil;

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
	private Action action_chinese;
	private Action action_english;
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
		addStatusLine();
		objectinfodao = new ObjectInfoDaoImpl();
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText(
				ResourceUtil.instance().getString(simpleClassName + ".title"));
		parent.setSize(700, 500);
		parent.setLocation(Display.getCurrent().getClientArea().width / 2
				- parent.getShell().getSize().x / 2, Display.getCurrent()
				.getClientArea().height / 2 - parent.getSize().y / 2);
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, true));

		filter = null;
		final Text text_search = new Text(main, SWT.LEFT | SWT.BORDER);
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
		group.setText(ResourceUtil.instance().getString(
				simpleClassName + ".group"));
		group.setLayout(new GridLayout(1, true));

		SashForm sash = new SashForm(group, SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(2, false));
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		view_left = new TableViewer(sash, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles1 = {
				ResourceUtil.instance().getString(simpleClassName + ".objname"),
				ResourceUtil.instance().getString(
						simpleClassName + ".description") };
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
		view_left.getTable().getColumns()[0]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view_left.getTable().getSize().x;
						view_left.getTable().getColumns()[1].setWidth(width
								- tc.getWidth());
					}
				});
		view_left.getTable().getColumns()[1]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view_left.getTable().getSize().x;
						view_left.getTable().getColumns()[0].setWidth(width
								- tc.getWidth());
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
		view_left.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
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
				TableItem item = view_left.getTable().getItem(
						new Point(e.x, e.y));
				if (item == null) {
					setModifyAndDelete(false);
				} else
					setModifyAndDelete(true);
			}
		});
		PopMenu pm = new PopMenu();
		pm.fillContextMenu(new MenuManager());

		view_right = new TableViewer(sash, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		String[] titles2 = {
				ResourceUtil.instance().getString(simpleClassName + ".key"),
				ResourceUtil.instance().getString(simpleClassName + ".value") };
		createColumns(titles2, view_right, new int[] { 130, 200 });
		view_right.getTable().setHeaderVisible(true);
		view_right.getTable().setLinesVisible(true);
		view_right.setContentProvider(new ObjectsRightContentProvider());
		view_right.setLabelProvider(new ObjectsRightLabelProvider());
		view_right.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
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
					setModifyAndDelete(true);
				} else
					setModifyAndDelete(false);
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
		view_right.getTable().getColumns()[0]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view_right.getTable().getSize().x;
						view_right.getTable().getColumns()[1].setWidth(width
								- tc.getWidth());
					}
				});
		view_right.getTable().getColumns()[1]
				.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						TableColumn tc = (TableColumn) e.getSource();
						final int width = view_right.getTable().getSize().x;
						view_right.getTable().getColumns()[0].setWidth(width
								- tc.getWidth());
					}
				});
		setModifyAndDelete(false);
		user = getData("userinfo", UserInfo.class);
		view_left.setInput(objectinfodao.get(user.getUser_name()));

		getStatusLineManager().setMessage(
				new Image(null, this.getClass().getResourceAsStream(
						"icon/online.jpg")), user.getUser_name());
	}

	private void setModifyAndDelete(boolean boo) {
		action_modify.setEnabled(boo);
		action_delete.setEnabled(boo);
		if (!boo)
			view_left.getTable().deselectAll();
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
		MenuManager menu_file = new MenuManager("&"
				+ ResourceUtil.instance().getString(simpleClassName + ".file"));
		MenuManager menu_option = new MenuManager("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".option"));
		MenuManager menu_language = new MenuManager("&"
				+ ResourceUtil.instance().getString(
						simpleClassName + ".language"));
		MenuManager menu_help = new MenuManager("&"
				+ ResourceUtil.instance().getString(simpleClassName + ".help"));
		main_menu.add(menu_file);
		main_menu.add(menu_option);
		main_menu.add(menu_language);
		main_menu.add(menu_help);
		menu_file.add(new Action("&"
				+ ResourceUtil.instance().getString(simpleClassName + ".new")
				+ "@Ctrl+N", Action.AS_PUSH_BUTTON) {
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
		menu_file.add(action_modify = new Action("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".modify") + "@Ctrl+M",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/modify.jpg"));
			}

			public void run() {
				modify();
			}
		});
		menu_file.add(action_delete = new Action("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".delete") + "@Ctrl+D",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/delete.jpg"));
			}

			public void run() {
				delete();
			}
		});
		menu_option.add(new Action("&"
				+ ResourceUtil.instance().getString(
						simpleClassName + ".changepwd") + "@Ctrl+U",
				Action.AS_PUSH_BUTTON) {
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
		menu_option.add(new Action("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".import") + "@Ctrl+I",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/import.jpg"));
			}

			public void run() {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				String filepath = fd.open();
				if (filepath == null)
					return;
				DataOperator dataoperator = new DataOperator(user
						.getUser_name());
				int[] result;
				try {
					result = dataoperator.importData(user.getUser_name(),
							filepath);
				} catch (IOException e) {
					MessageUtil.openError(getShell(), ResourceUtil.instance()
							.getString("common.error"), e.getMessage());
					return;
				}
				MessageUtil
						.openInformation(
								getShell(),
								ResourceUtil.instance().getString(
										"common.information"),
								ResourceUtil.instance().getString(
										"common.import.successful")
										+ "\n"
										+ ResourceUtil.instance().getString(
												"common.import.total.num")
										+ result[0]
										+ "\n"
										+ ResourceUtil.instance().getString(
												"common.import.success.num")
										+ result[1]);
				view_left.setInput(objectinfodao.get(user.getUser_name()));
			}
		});
		menu_option.add(new Action("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".export") + "@Ctrl+E",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/export.jpg"));
			}

			public void run() {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				String filepath = fd.open();
				if (filepath == null)
					return;
				DataOperator dataoperator = new DataOperator(user
						.getUser_name());
				try {
					dataoperator.exportData(filepath);
				} catch (IOException e) {
					MessageUtil.openError(getShell(), ResourceUtil.instance()
							.getString("common.error"), e.getMessage());
					return;
				}
				MessageUtil.openInformation(getShell(), ResourceUtil.instance()
						.getString("common.information"), ResourceUtil
						.instance().getString("common.export.successful"));
			}
		});
		menu_language.add(action_chinese = new Action("&"
				+ ResourceUtil.instance().getString(
						simpleClassName + ".chinese"), Action.AS_CHECK_BOX) {

			public void run() {
				ResourceUtil.instance().modifyLanguageConfig(Locale.CHINESE);
				action_english.setChecked(false);
				action_chinese.setChecked(true);
				int o = DialogFactory.openConfirm(
						getShell(),
						ResourceUtil.instance().getString(
								simpleClassName + ".note.change.language"));
				if (o == SWT.OK) {
					ResourceUtil.instance().refreshCache();
					close();
					if (login == null) {
						login = new Login();
					}
					login.show(true);
				}
			}
		});
		menu_language.add(action_english = new Action("&"
				+ ResourceUtil.instance().getString(
						simpleClassName + ".english"), Action.AS_CHECK_BOX) {
			public void run() {
				ResourceUtil.instance().modifyLanguageConfig(Locale.ENGLISH);
				action_chinese.setChecked(false);
				action_english.setChecked(true);
				int o = DialogFactory.openConfirm(
						getShell(),
						ResourceUtil.instance().getString(
								simpleClassName + ".note.change.language"));
				if (o == SWT.OK) {
					ResourceUtil.instance().refreshCache();
					close();
					if (login == null) {
						login = new Login();
					}
					login.show(true);
				}
			}
		});
		setLanguageCheck();
		menu_file.add(new Separator());
		menu_file.add(new Action("&"
				+ ResourceUtil.instance()
						.getString(simpleClassName + ".logout") + "@Ctrl+L",
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/logout.jpg"));
			}

			public void run() {
				int res = DialogFactory.openConfirm(
						getShell(),
						ResourceUtil.instance().getString(
								simpleClassName + ".logout.notify"));
				if (res == SWT.CANCEL)
					return;
				if (login == null) {
					login = new Login();
				}
				if (ResourceUtil.instance().getCurrentLanguage() != ResourceUtil
						.instance().getDBLanguage())
					ResourceUtil.instance().refreshCache();
				close();
				login.show(true);
			}
		});
		menu_file.add(new Separator());
		menu_file.add(new Action("&"
				+ ResourceUtil.instance().getString(simpleClassName + ".exit"),
				Action.AS_PUSH_BUTTON) {
			public ImageDescriptor getImageDescriptor() {
				return ImageDescriptor.createFromURL(this.getClass()
						.getResource("icon/exit.jpg"));
			}

			public void run() {
				int o = DialogFactory
						.openConfirm(getShell(), ResourceUtil.instance()
								.getString(simpleClassName + ".exit.notify"));
				if (o == SWT.OK)
					close();
			}
		});

		menu_help.add(new Action(
				"&"
						+ ResourceUtil.instance().getString(
								simpleClassName + ".about"),
				Action.AS_PUSH_BUTTON) {
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

	private void setLanguageCheck() {
		if (ResourceUtil.instance().getCurrentLanguage() == Locale.CHINESE)
			action_chinese.setChecked(true);
		else if (ResourceUtil.instance().getCurrentLanguage() == Locale.ENGLISH)
			action_english.setChecked(true);
	}

	class PopMenu extends ActionGroup {
		public void fillContextMenu(IMenuManager menu) {
			MenuManager menuManager = (MenuManager) menu;
			menuManager.add(action_modify);
			menuManager.add(action_delete);
			Menu m = menuManager.createContextMenu(view_left.getTable());
			view_left.getTable().setMenu(m);
		}
	}

	@SuppressWarnings("unchecked")
	private void delete() {
		TableItem[] items = view_left.getTable().getSelection();
		ObjectInfo obj = null;
		if (items != null && items.length > 0) {
			int res = DialogFactory.openConfirm(getShell(), ResourceUtil
					.instance().getString(simpleClassName + ".delete.notify"));
			if (res == SWT.CANCEL)
				return;
			try {
				obj = (ObjectInfo) items[0].getData();
				objectinfodao.delete(obj);
			} catch (Exception e) {
				DialogFactory.openError(getShell(), e.getMessage());
				return;
			}
			DialogFactory.openInformation(getShell(), ResourceUtil.instance()
					.getString("common.delete.successful"));
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

	protected StatusLineManager createStatusLineManager() {
		StatusLineManager slm = new StatusLineManager();
		return slm;
	}

	protected StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
	}
}
