package org.howard.portal.kit.gui.component;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.howard.portal.kit.gui.util.TreeLogic;

/**
 * The purpose of this class is to provide composite of build page
 */
public class StackBuild implements StackComponent {
    private SashForm form;
    private StackLayout stackLayout;

    private TreeLogic treeLogic;
    /**
     * Creates a new instance of <code>StackBuild</code>.
     */
    public StackBuild(){
        treeLogic = new TreeLogic();
    }

    @Override
    public Composite ceatePanel(Composite composite) {
        if (form != null)
            return form;
        form = new SashForm(composite, SWT.HORIZONTAL);
        form.setLayout(new GridLayout(2, false));
        form.setLayoutData(new GridData(GridData.FILL_BOTH));
        form.setSashWidth(10);
        form.setBackground(new Color(null, new RGB(0, 0, 0)));
        
        treeLogic.createTree(form, new File("C:\\Users\\ehaozuo\\workenv\\msdk\\portal-team"));

        Composite child3 = new Composite(form, SWT.NONE);
        child3.setLayout(new FillLayout());
        new Label(child3, SWT.PUSH).setText("Label in pane3");
        stackLayout = ((StackLayout) composite.getLayout());
        stackLayout.topControl = form;
        form.layout();
        return form;
    }

    @Override
    public void close() {
        if (form != null) {
            form.setVisible(false);
        }
    }
}
