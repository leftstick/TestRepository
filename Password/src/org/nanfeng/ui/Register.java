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
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;

public class Register extends BaseDialog {

	private Setting setting;
	private Text text_userName;
	private Text text_userPwd;
	private UserInfoDao userinfodao;

	public Register() {
		super(null);
		setShellStyle(SWT.CLOSE);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText("Password->Register");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		main.setLayout(gridLayout);
		Label label_useName = new Label(main, SWT.LEFT);
		label_useName.setText("User Name:");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 100;
		data.minimumWidth = 100;
		label_useName.setLayoutData(data);
		text_userName = new Text(main, SWT.BORDER);
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 200;
		data2.minimumWidth = 200;
		text_userName.setLayoutData(data2);
		text_userName.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					register();
				}
			}
		});

		Label label_usePwd = new Label(main, SWT.LEFT);
		label_usePwd.setText("User Password:");
		label_usePwd.setLayoutData(data);
		text_userPwd = new Text(main, SWT.BORDER | SWT.PASSWORD);
		text_userPwd.setLayoutData(data2);
		text_userPwd.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					register();
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

		Button button_register = new Button(composite_buttons1, SWT.PUSH);
		button_register.setText("Register");
		button_register.setLayoutData(new RowData(60, 25));
		button_register.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				register();
			}
		});

		Button button_cancel = new Button(composite_buttons1, SWT.PUSH);
		button_cancel.setText("Cancel");
		button_cancel.setLayoutData(new RowData(60, 25));
		button_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
				Register.this.getData("login", Login.class).show(true);
			}
		});
	}

	private void register() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText("Error");
		if (text_userName.getText().trim().length() == 0) {
			mb.setMessage("User Name must not be empty");
			mb.open();
			return;
		}
		if (text_userPwd.getText().length() == 0) {
			mb.setMessage("User Pwd must not be empty");
			mb.open();
			return;
		}

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();
		UserInfo user = new UserInfo();
		user.setUser_name(text_userName.getText());
		user.setUser_pwd(text_userPwd.getText());
		if (userinfodao.get(user.getUser_name()) != null) {
			mb.setMessage("User " + user.getUser_name() + " has already exists");
			mb.open();
			return;
		}

		try {
			userinfodao.save(user);
		} catch (Exception e) {
			mb.setMessage(e.getMessage());
			mb.open();
			return;
		}

		if (setting == null) {
			setting = new Setting();
			setting.setData("register", Register.this);

		}
		setting.setData("userinfo", user);
		close();
		setting.show(true);
	}
}
