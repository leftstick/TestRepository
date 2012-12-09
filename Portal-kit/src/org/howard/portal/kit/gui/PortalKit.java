package org.howard.portal.kit.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.howard.portal.kit.gui.component.StackComponent;
import org.howard.portal.kit.gui.util.MenuItemLogic;
import org.howard.portal.kit.gui.util.MenuType;

/**
 * The purpose of this class is main entrance of this toolkit.
 */
public class PortalKit {

	private Display mainDisplay;
	private Shell mainShell;
	private Menu menuBar;
	private Composite composite;
	private StackLayout centralLayout;

	private String title;

	private MenuItemLogic menuItemLogic;

	public PortalKit() {
		mainDisplay = new Display();
		mainShell = new Shell(mainDisplay);
		menuItemLogic = new MenuItemLogic();
		mainShell.setLayout(new GridLayout(1, true));
		mainShell.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite = new Composite(mainShell, SWT.BORDER);
		centralLayout = new StackLayout();
		composite.setLayout(centralLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		SashForm form = new SashForm(composite, SWT.HORIZONTAL);
		form.setLayout(new GridLayout(2, false));
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setSashWidth(10);
		form.setBackground(new Color(null, new RGB(0, 0, 0)));

		Composite child1 = new Composite(form, SWT.NONE);
		child1.setLayout(new FillLayout());
		new Label(child1, SWT.NONE).setText("Label in pane 1");

		Composite child3 = new Composite(form, SWT.NONE);
		child3.setLayout(new FillLayout());
		new Label(child3, SWT.PUSH).setText("Label in pane3");

		centralLayout.topControl = form;

	}

	public void addPanel(StackComponent component) {
		component.ceatePanel(composite);
	}

	public Menu createMenuBar() {
		if (menuBar == null) {
			menuBar = new Menu(mainShell, MenuType.BAR.getType());
			mainShell.setMenuBar(menuBar);
		}
		return menuBar;
	}

	public MenuItem addMenuItem(String text) {
		return this.addMenuItem(text, null, null);
	}

	public MenuItem addMenuItem(String text, String parentText,
			SelectionListener listener) {
		MenuItem menuItem = null;
		if (parentText == null || parentText.trim().equals("")) {
			if (menuItemLogic.getMenuItemByText(text) == null) {
				menuItem = new MenuItem(menuBar, MenuType.CASCADE.getType());
				menuItem.setText(text);
				Menu dropdown = new Menu(menuBar);
				menuItem.setMenu(dropdown);
				menuItemLogic.addMenuItem(text, null, menuItem, dropdown);
			} else
				throw new RuntimeException(
						"It's an exists menuitem there on menuBar");
			return menuItem;
		}
		if (menuItemLogic.getMenuItemByText(text, parentText) == null) {
			menuItem = new MenuItem(menuItemLogic.getMenuItemByText(parentText)
					.getDropDown(), MenuType.PUSH.getType());
			menuItem.setText(text);
			menuItem.addSelectionListener(listener);
			menuItemLogic.addMenuItem(text, parentText, menuItem, null);
		} else
			throw new RuntimeException(
					"It's an exists menuitem there under menuItem "
							+ parentText);
		return menuItem;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		mainShell.setText(title);
	}

	public void show() {
		mainShell.pack();
		mainShell.open();
		while (!mainShell.isDisposed()) {
			if (!mainDisplay.readAndDispatch())
				mainDisplay.sleep();
		}
		mainDisplay.dispose();
	}
}
