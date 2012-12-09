package org.howard.portal.kit.gui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemLogic {
	private List<MenuItemElement> list;

	public MenuItemLogic() {
		list = new ArrayList<MenuItemElement>();
	}

	public MenuItemElement getMenuItemByText(String text) {
		return this.getMenuItemByText(text, null);
	}

	public MenuItemElement getMenuItemByText(String text, String parentText) {
		for (MenuItemElement element : list) {
			if (parentText == null) {
				if (element.getParentText() == null
						&& element.getMenuItemText().equals(text))
					return element;
			} else if (parentText.equals(element.getParentText())
					&& element.getMenuItemText().equals(text))
				return element;
		}
		return null;
	}

	public List<MenuItemElement> getMenuItemsByParentText(String parentText) {
		List<MenuItemElement> result = new ArrayList<MenuItemElement>();
		for (MenuItemElement element : list) {
			if (element.getParentText().equals(parentText))
				result.add(element);
		}
		return result;
	}

	public void addMenuItem(String text, String parentText, MenuItem menuItem,
			Menu dropDwon) {
		MenuItemElement element = new MenuItemElement();
		element.setMenuItem(menuItem);
		element.setMenuItemText(text);
		element.setParentText(parentText);
		element.setDropDown(dropDwon);
		list.add(element);
	}

	public void addMenuItem(String text, String parentText, MenuItem menuItem) {
		this.addMenuItem(text, parentText, menuItem, null);
	}
}
