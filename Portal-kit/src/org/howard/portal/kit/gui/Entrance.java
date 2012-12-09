package org.howard.portal.kit.gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Entrance {
	public static void main(String[] args) {

		PortalKit kit = new PortalKit();
		kit.createMenuBar();
		kit.addMenuItem("File");
		kit.addMenuItem("Exit", "File", new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("hello exit");
			}
		});
		kit.setTitle("PortalKit");
		kit.show();
	}
}
