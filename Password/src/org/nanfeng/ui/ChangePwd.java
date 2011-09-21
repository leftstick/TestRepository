package org.nanfeng.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;

public class ChangePwd extends BaseDialog {

	private Text text_oldpwd;
	private Text text_newPwd;
	private Text text_newPwd2;
	private UserInfoDao userinfodao;

	public ChangePwd(Shell parent) {
		super(parent);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText("Password->Option->ChangePwd");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		main.setLayout(gridLayout);

		Label label_oldpwd = new Label(main, SWT.LEFT);
		label_oldpwd.setText("Old password:");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 150;
		data.minimumWidth = 150;
		label_oldpwd.setLayoutData(data);
		text_oldpwd = new Text(main, SWT.BORDER | SWT.PASSWORD);
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 200;
		data2.minimumWidth = 200;
		text_oldpwd.setLayoutData(data2);
		text_oldpwd.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					change();
				}
			}
		});

		Label label_newPwd = new Label(main, SWT.LEFT);
		label_newPwd.setText("New password:");
		label_newPwd.setLayoutData(data);
		text_newPwd = new Text(main, SWT.BORDER | SWT.PASSWORD);
		text_newPwd.setLayoutData(data2);
		text_newPwd.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					change();
				}
			}
		});

		Label label_newPwd2 = new Label(main, SWT.LEFT);
		label_newPwd2.setText("Repeat new password:");
		label_newPwd2.setLayoutData(data);
		text_newPwd2 = new Text(main, SWT.BORDER | SWT.PASSWORD);
		text_newPwd2.setLayoutData(data2);
		text_newPwd2.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					change();
				}
			}
		});

		Composite composite_buttons1 = new Composite(main,
				GridData.HORIZONTAL_ALIGN_CENTER);
		RowLayout rl1 = new RowLayout(SWT.HORIZONTAL);
		GridData data3 = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		data3.horizontalSpan = 2;
		composite_buttons1.setLayout(rl1);
		composite_buttons1.setLayoutData(data3);

		Button button_change = new Button(composite_buttons1, SWT.PUSH);
		button_change.setText("Submit");
		button_change.setLayoutData(new RowData(50, 25));
		button_change.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				change();
			}
		});

		Button button_cancel = new Button(composite_buttons1, SWT.PUSH);
		button_cancel.setText("Cancel");
		button_cancel.setLayoutData(new RowData(50, 25));
		button_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
	}

	private void change() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText("Error");
		if (text_oldpwd.getText().trim().length() == 0) {
			mb.setMessage("old password must not be empty");
			mb.open();
			return;
		}
		if (text_newPwd.getText().length() == 0) {
			mb.setMessage("new password must not be empty");
			mb.open();
			return;
		}
		if (text_newPwd2.getText().length() == 0) {
			mb.setMessage("repeat password must not be empty");
			mb.open();
			return;
		}
		if (!text_newPwd2.getText().equals(text_newPwd.getText())) {
			mb.setMessage("repeat password is not the same as new password");
			mb.open();
			return;
		}
		UserInfo user = getData("userinfo", UserInfo.class);
		if (!text_oldpwd.getText().equals(user.getUser_pwd())) {
			mb.setMessage("old password is not correct");
			mb.open();
			return;
		}

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();
		user.setUser_pwd(text_newPwd2.getText());
		try {
			userinfodao.update(user);
		} catch (Exception e) {
			mb.setMessage(e.getMessage());
			mb.open();
			return;
		}
		mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
		mb.setText("Information");
		mb.setMessage("change successful");
		mb.open();
		close();
	}
}
