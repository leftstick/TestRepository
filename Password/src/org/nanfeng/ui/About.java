package org.nanfeng.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.nanfeng.ui.face.BaseDialog;

public class About extends BaseDialog {

	public About(Shell parent) {
		super(parent);
		setShellStyle(SWT.CLOSE|SWT.APPLICATION_MODAL);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText("Password->Help->About");
		Composite main = new Composite(parent, SWT.LEFT_TO_RIGHT);
		GridLayout gl = new GridLayout(1, true);
		gl.marginTop = 5;
		gl.marginLeft = 20;
		gl.marginRight = 20;
		gl.verticalSpacing = 15;
		main.setLayout(gl);
		Label text = new Label(main, SWT.CENTER);
		StringBuffer sb = new StringBuffer();
		sb.append("Software: Password Keeper\n");
		sb.append("Author: ZuoHao\n");
		sb.append("Version: 1.0\n");
		sb.append("Update time: 2011-09-22 21:15\n");
		text.setText(sb.toString());
		// text.setLayoutData();

		Button button_close = new Button(main, SWT.PUSH);
		button_close.setText("close");
		button_close.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		button_close.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
	}
}
