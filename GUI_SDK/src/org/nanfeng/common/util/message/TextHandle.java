package org.nanfeng.common.util.message;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class TextHandle implements ConsoleHandle {
	private Text text;

	public TextHandle(Composite parent) {
		text = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY
				| SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	public Text getText() {
		return text;
	}

	public void onOutRead(final String message, boolean isErrorOut) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				text.append(message + "\n");
			}
		});

	}

}
