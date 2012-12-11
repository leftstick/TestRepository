package org.howard.portal.kit.gui.factory;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * The purpose of this class is to provide simple APIs to generate a
 * dialog
 */
public class DialogFactory {
    /**
     * Open an error dialog with specified title, button, error
     * message base on its parent
     * 
     * @param parent
     * @param message
     * @return 0
     */
    public static int openError(Shell parent, String message) {
        MessageDialog md = new MessageDialog(parent, "ERROR", null, message, MessageDialog.ERROR,
                new String[] { "OK" }, 0);
        if (md.getShell() == null)
            md.create();
        md.getShell().setSize(300, 150);
        md.getShell().setLocation(
                (parent.getBounds().width - md.getShell().getBounds().width) / 2 + parent.getBounds().x,
                (parent.getBounds().height - md.getShell().getBounds().height) / 2 + parent.getBounds().y);
        return md.open();
    }

    /**
     * Open an info dialog with specified title, button, information
     * base on its parent
     * 
     * @param parent
     * @param message
     * @return 0
     */
    public static int openInfo(Shell parent, String message) {
        MessageDialog md = new MessageDialog(parent, "INFORMATION", null, message, MessageDialog.INFORMATION,
                new String[] { "OK" }, 0);
        if (md.getShell() == null)
            md.create();
        md.getShell().setSize(300, 150);
        md.getShell().setLocation(
                (parent.getBounds().width - md.getShell().getBounds().width) / 2 + parent.getBounds().x,
                (parent.getBounds().height - md.getShell().getBounds().height) / 2 + parent.getBounds().y);
        return md.open();
    }

    /**
     * Open a confirm dialog with specified title, text on OK, text on
     * cancel, confirm message, base on its parent
     * 
     * @param parent
     * @param message
     * @return SWT.OK/SWT.CANCEL
     */
    public static int openConfirm(Shell parent, String message) {
        MessageDialog md = new MessageDialog(parent, "CONFIRMATION", null, message, MessageDialog.CONFIRM,
                new String[] { "OK", "CANCEL" }, 0);
        if (md.getShell() == null)
            md.create();
        md.getShell().setSize(300, 150);
        md.getShell().setLocation(
                (parent.getBounds().width - md.getShell().getBounds().width) / 2 + parent.getBounds().x,
                (parent.getBounds().height - md.getShell().getBounds().height) / 2 + parent.getBounds().y);
        return convert(md.open());
    }

    /**
     * Open a file dialog base on its parent
     * 
     * @param parent
     * @return a string describing the absolute path of the first
     *         selected file, or null if the dialog was cancelled or
     *         an error occurred
     */
    public static String openFileDialog(Shell parent) {
        FileDialog fd = new FileDialog(parent, SWT.OPEN);
        return fd.open();
    }

    private static int convert(int src) {
        switch (src) {
        case 0:
            return SWT.OK;
        case 1:
            return SWT.CANCEL;
        }
        return SWT.OK;
    }
}
