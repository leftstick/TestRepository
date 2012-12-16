package org.howard.portal.kit.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.howard.portal.kit.gui.component.StackComponent;
import org.howard.portal.kit.gui.factory.MenuItemLogic;
import org.howard.portal.kit.gui.factory.StatusBarLogic;

/**
 * The purpose of this class is main entrance of this toolkit.
 */
public class PortalKit {

    private Display mainDisplay;
    private Shell mainShell;
    private Composite composite;
    private StackLayout centralLayout;

    private String title;

    private MenuItemLogic menuItemLogic;
    private StatusBarLogic statusBarLogic;

    /**
     * Creates a new instance of <code>PortalKit</code>.
     */
    public PortalKit() {
        mainDisplay = new Display();
        mainShell = new Shell(mainDisplay);
        menuItemLogic = new MenuItemLogic();
        mainShell.setLayout(new GridLayout(1, true));
        mainShell.setLayoutData(new GridData(GridData.FILL_BOTH));
        statusBarLogic = new StatusBarLogic();
        composite = new Composite(mainShell, SWT.BORDER);
        centralLayout = new StackLayout();
        composite.setLayout(centralLayout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /**
     * set specified composite to the top of panel
     * 
     * @param composite
     */
    public void setToTop(Composite composite) {
        centralLayout.topControl = composite;
        composite.getParent().layout();
    }

    /**
     * Add a new component into stack
     * 
     * @param component
     * @return added composite
     */
    public Composite addPanelToStack(StackComponent component) {
        return component.ceatePanel(composite);
    }

    /**
     * @return created MenuBar
     */
    public Menu createMenuBar() {
        return menuItemLogic.createMenuBar(mainShell, mainDisplay);
    }

    /**
     * Add a menuItem to the top of MenuBar
     * 
     * @param text
     * @return added top MenuItem
     */
    public MenuItem addMenuItem(String text) {
        if (!menuItemLogic.isMenuBarCreated())
            this.createMenuBar();
        return this.addMenuItem(text, null, null);
    }

    /**
     * Add a MenuItem to specified parent
     * 
     * @param text text of MenuItem
     * @param parentText text of the MenuItem's parent
     * @param listener listener to the MenuItem
     * @return added MenuItem
     */
    public MenuItem addMenuItem(String text, String parentText, SelectionListener listener) {
        if (!menuItemLogic.isMenuBarCreated())
            throw new RuntimeException("Menu bar has not created, you cannot add Menuitem.");
        if (parentText == null || parentText.trim().equals(""))
            return menuItemLogic.createMenuItem(text);
        return menuItemLogic.createMenuItem(text, parentText, listener);
    }

    /**
     * create StatusBar
     */
    public void createStatusbar() {
        createStatusbar(null);
    }

    /**
     * create StatusBar with specified text
     * 
     * @param text
     */
    public void createStatusbar(String text) {
        statusBarLogic.createStatusBar(mainShell);
        if (text != null)
            statusBarLogic.setText(text);
    }

    /**
     * set text to status bar
     * 
     * @param text
     */
    public void setTextToStatusbar(String text) {
        if (!statusBarLogic.isStatusBarCreated())
            createStatusbar(text);
        statusBarLogic.setText(text);
    }

    /**
     * @return title of kit
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title title of kit
     */
    public void setTitle(String title) {
        this.title = title;
        mainShell.setText(title);
    }

    /**
     * display Kit
     */
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
