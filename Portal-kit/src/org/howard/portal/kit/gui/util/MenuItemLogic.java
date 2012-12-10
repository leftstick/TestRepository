package org.howard.portal.kit.gui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The purpose of this class is to provide a simple logic API
 * interface of MenuItem
 */
public class MenuItemLogic {
    private List<MenuItemElement> list;

    /**
     * Creates a new instance of <code>MenuItemLogic</code>.
     */
    public MenuItemLogic() {
        list = new ArrayList<MenuItemElement>();
    }

    /**
     * @param text
     * @return MenuItemElement of a top MenuItem with specified text
     */
    public MenuItemElement getMenuItemByText(String text) {
        return this.getMenuItemByText(text, null);
    }

    /**
     * @param text
     * @param parentText
     * @return MenuItemElement of a MenuItem with specified text and
     *         parent
     */
    public MenuItemElement getMenuItemByText(String text, String parentText) {
        for (MenuItemElement element : list) {
            if (parentText == null) {
                if (element.getParentText() == null && element.getMenuItemText().equals(text))
                    return element;
            } else if (parentText.equals(element.getParentText()) && element.getMenuItemText().equals(text))
                return element;
        }
        return null;
    }

    /**
     * @param parentText
     * @return an array of MenuItemElement, with a specified parent
     */
    public List<MenuItemElement> getMenuItemsByParentText(String parentText) {
        List<MenuItemElement> result = new ArrayList<MenuItemElement>();
        for (MenuItemElement element : list) {
            if (element.getParentText().equals(parentText))
                result.add(element);
        }
        return result;
    }

    /**
     * Add an MenuItem into logic controller
     * 
     * @param text
     * @param parentText
     * @param menuItem
     * @param dropDwon
     */
    public void addMenuItem(String text, String parentText, MenuItem menuItem, Menu dropDwon) {
        MenuItemElement element = new MenuItemElement();
        element.setMenuItem(menuItem);
        element.setMenuItemText(text);
        element.setParentText(parentText);
        element.setDropDown(dropDwon);
        list.add(element);
    }

    /**
     * Add a MenuItem into logic controller to specified parent
     * 
     * @param text
     * @param parentText
     * @param menuItem
     */
    public void addMenuItem(String text, String parentText, MenuItem menuItem) {
        this.addMenuItem(text, parentText, menuItem, null);
    }

    /**
     * Add a top level MenuItem into logic controller
     * 
     * @param text
     * @param menuItem
     * @param dropDwon
     */
    public void addMenuItem(String text, MenuItem menuItem, Menu dropDwon) {
        this.addMenuItem(text, null, menuItem, dropDwon);
    }
}
