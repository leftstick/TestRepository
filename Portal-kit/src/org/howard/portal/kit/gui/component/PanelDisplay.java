package org.howard.portal.kit.gui.component;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.howard.portal.kit.gui.util.MenuType;

/**
 * The purpose of this class is to provide a basic display Object
 */
public class PanelDisplay extends Display {
    /**
     * @param parent
     * @param type 
     * @return Menu
     */
    public Menu createMenuBar(Shell parent, MenuType type) {
        Menu menuBar = super.getMenuBar();
        if (menuBar == null) {
            menuBar = new Menu(parent, type.getType());
            parent.setMenuBar(menuBar);
        }
        return menuBar;
    }

}
