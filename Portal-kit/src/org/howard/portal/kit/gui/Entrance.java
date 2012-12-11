package org.howard.portal.kit.gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.howard.portal.kit.gui.component.powerbuild.StackBuild;

/**
 * Entrance to portal kit
 */
public class Entrance {
    private PortalKit kit;
    private StackBuild build;

    /**
     * Creates a new instance of <code>Entrance</code>.
     */
    public Entrance() {
        kit = new PortalKit();
        build = new StackBuild();
        kit.addMenuItem("File");
        kit.addMenuItem("Exit", "File", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        final Composite cbuild = kit.addPanelToStack(build);
        kit.addMenuItem("Build");
        kit.addMenuItem("Power Build", "Build", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                kit.setToTop(cbuild);
            }
        });
        kit.setTitle("PortalKit");
        kit.setTextToStatusbar("kitddddddddddddddddddd");
    }

    public void show() {
        kit.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Entrance entrance = new Entrance();
        entrance.show();
    }
}
