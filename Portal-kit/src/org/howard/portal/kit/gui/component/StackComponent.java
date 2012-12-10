package org.howard.portal.kit.gui.component;

import org.eclipse.swt.widgets.Composite;

/**
 * The purpose of this class is to provide a interface to create a
 * certral panel into stack
 */
public interface StackComponent {
    /**
     * @param composite
     * @return created composite
     */
    public Composite ceatePanel(Composite composite);

    /**
     * close the composite
     */
    public void close();

}
