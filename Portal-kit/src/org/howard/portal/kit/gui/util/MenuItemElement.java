package org.howard.portal.kit.gui.util;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemElement {
	private String MenuItemText;
	private MenuItem menuItem;
	private Menu dropDown;

	public Menu getDropDown() {
		return dropDown;
	}

	public void setDropDown(Menu dropDown) {
		this.dropDown = dropDown;
	}

	private String parentText;

	public String getMenuItemText() {
		return MenuItemText;
	}

	public void setMenuItemText(String menuItemText) {
		MenuItemText = menuItemText;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public String getParentText() {
		return parentText;
	}

	public void setParentText(String parentText) {
		this.parentText = parentText;
	}
}
