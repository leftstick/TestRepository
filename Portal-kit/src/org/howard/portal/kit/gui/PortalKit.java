package org.howard.portal.kit.gui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.howard.portal.kit.gui.component.PanelDisplay;
import org.howard.portal.kit.gui.util.MenuType;

/**
 * The purpose of this class is main entrance of this toolkit.
 */
public class PortalKit {
    public static void main(String[] args) {
        PanelDisplay display = new PanelDisplay();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        Menu appMenuBar = display.getMenuBar();
        if (appMenuBar == null) {
            appMenuBar = new Menu(shell, MenuType.BAR.getType());
            shell.setMenuBar(appMenuBar);
        }
        MenuItem file = new MenuItem(appMenuBar, MenuType.CASCADE.getType());
        file.setText("File");
        Menu dropdown = new Menu(appMenuBar);
        file.setMenu(dropdown);
        MenuItem exit = new MenuItem(dropdown, MenuType.PUSH.getType());
        exit.setText("Exit");
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
