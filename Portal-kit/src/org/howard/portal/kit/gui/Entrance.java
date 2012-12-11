package org.howard.portal.kit.gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.howard.portal.kit.gui.component.powerbuild.StackBuild;

/**
 * Entrance to portal kit
 */
public class Entrance {
    /**
     * @param args
     */
    public static void main(String[] args) {

        PortalKit kit = new PortalKit();
        kit.createMenuBar();
        kit.addMenuItem("File");
        final StackBuild build = new StackBuild();
        kit.addMenuItem("Exit", "File", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                build.close();
                System.out.println("hello exit");
            }
        });
        kit.addPanelToStack(build);
        kit.setTitle("PortalKit");
        kit.createStatusbar("kitddddddddddddddddddd");
        kit.show();
    }
}
