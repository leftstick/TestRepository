package org.howard.portal.kit.gui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

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
        fillLayout.marginWidth=0;
        composite.setLayout(fillLayout);
        return composite;
    }

    /**
     * create a composite with StackLayout.
     * 
     * @param parent
     * @return created composite with StackLayout
     */
    public static Composite createStackComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new StackLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return composite;
    }

    /**
     * create a button in PUSH type with specified text
     * 
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
     * 
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
    
    /**
     * @param parent
     * @param text
     * @param listener
     * @return created check button
     */
    public static Button createCheckButton(Composite parent, String text, SelectionListener listener) {
        Button button = new Button(parent, SWT.CHECK);
        button.setText(text);
        if (listener != null)
            button.addSelectionListener(listener);
        return button;
    }

    /** 
     * create a read-only text with white background.
     * @param parent
     * @return created Text
     */
    public static Text createReadOnlyText(Composite parent) {
        Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
        text.setEnabled(false);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setBackground(new Color(null, 255, 255, 255));
        return text;
    }
}
