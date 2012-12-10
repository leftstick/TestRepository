package org.howard.portal.kit.gui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * The purpose of this class is to provide easy way to create a
 * composite
 */
public class CompositeFactory {
    /**
     * create a SashForm with specified separator number
     * 
     * @param parent
     * @param type
     * @param sepNum
     * @return created SashForm
     */
    public static SashForm createSashForm(Composite parent, int type, int sepNum) {
        SashForm form = new SashForm(parent, type);
        form.setLayout(new GridLayout(sepNum, false));
        form.setLayoutData(new GridData(GridData.FILL_BOTH));
        form.setSashWidth(5);
        return form;
    }

    /**
     * create a composite with GridLayout in specified columns
     * 
     * @param parent
     * @param colNum
     * @return created composite with GridLayout
     */
    public static Composite createGridComposite(Composite parent, int colNum) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(colNum, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return composite;
    }

    /**
     * create a composite with FillLayout
     * 
     * @param parent
     * @return created composite with FillLayout
     */
    public static Composite createHorFillComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
        fillLayout.spacing = 5;
        composite.setLayout(fillLayout);
        return composite;
    }

    /**
     * create a button in PUSH type with specified text
     * @param parent
     * @param text
     * @param listener
     * @return created button
     */
    public static Button createPushButton(Composite parent, String text, SelectionListener listener) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(text);
        if (listener != null)
            button.addSelectionListener(listener);
        return button;
    }

    /**
     * create a button in RADIO type with specified text
     * @param parent
     * @param text
     * @param listener
     * @return created button
     */
    public static Button createRadioButton(Composite parent, String text, SelectionListener listener) {
        Button button = new Button(parent, SWT.RADIO);
        button.setText(text);
        if (listener != null)
            button.addSelectionListener(listener);
        return button;
    }
}
