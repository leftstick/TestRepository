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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;
import org.nanfeng.util.ResourceUtil;

public class Login extends BaseDialog {
	private Register register;
	private Forget forget;
	private Text text_userName;
	private Text text_userPwd;
	private UserInfoDao userdao;

	public Login() {
		super(null);
		setShellStyle(SWT.CLOSE);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText(
				ResourceUtil.instance().getString(simpleClassName + ".title"));
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		main.setLayout(gridLayout);
		Label label_useName = new Label(main, SWT.LEFT);
		label_useName.setText(ResourceUtil.instance().getString(
				simpleClassName + ".user_name"));
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
					login();
				}
			}
		});

		Label label_usePwd = new Label(main, SWT.LEFT);
		label_usePwd.setText(ResourceUtil.instance().getString(
				simpleClassName + ".user_pwd"));
		label_usePwd.setLayoutData(data);
		text_userPwd = new Text(main, SWT.BORDER | SWT.PASSWORD);
		text_userPwd.setLayoutData(data2);
		text_userPwd.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					login();
				}
			}
		});

		Composite composite_buttons1 = new Composite(main, SWT.RIGHT_TO_LEFT);
		RowLayout rl1 = new RowLayout(SWT.HORIZONTAL);
		GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
		data3.widthHint = 200;
		data3.minimumWidth = 200;
		composite_buttons1.setLayout(rl1);
		composite_buttons1.setLayoutData(data3);

		Button button_exit = new Button(composite_buttons1, SWT.PUSH);
		button_exit.setText(ResourceUtil.instance().getString(
				simpleClassName + ".exit"));
		button_exit.setLayoutData(new RowData(60, 25));
		button_exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});

		Button button_login = new Button(composite_buttons1, SWT.PUSH);
		button_login.setText(ResourceUtil.instance().getString(
				simpleClassName + ".login"));
		button_login.setLayoutData(new RowData(60, 25));
		button_login.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});

		composite_buttons1
				.setTabList(new Control[] { button_login, button_exit });

		Composite composite_buttons2 = new Composite(main, SWT.RIGHT_TO_LEFT);
		composite_buttons2.setLayout(rl1);
		composite_buttons2.setLayoutData(data3);

		Button button_forget = new Button(composite_buttons2, SWT.PUSH);
		button_forget.setText(ResourceUtil.instance().getString(
				simpleClassName + ".forget"));
		button_forget.setLayoutData(new RowData(60, 25));
		button_forget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (forget == null) {
					forget = new Forget();
					forget.setData("login", Login.this);
				}
				close();
				forget.show(true);
			}
		});

		Button button_regist = new Button(composite_buttons2, SWT.PUSH);
		button_regist.setText(ResourceUtil.instance().getString(
				simpleClassName + ".register"));
		button_regist.setLayoutData(new RowData(60, 25));
		button_regist.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (register == null) {
					register = new Register();
					register.setData("login", Login.this);
				}
				close();
				register.show(true);

			}
		});

		composite_buttons2.setTabList(new Control[] { button_regist,
				button_forget });

	}

	private void login() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText(ResourceUtil.instance().getString("common.error"));
		if (text_userName.getText().trim().length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.username.empty"));
			mb.open();
			return;
		}
		if (text_userPwd.getText().length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.userpwd.empty"));
			mb.open();
			return;
		}
		if (userdao == null)
			userdao = new UserInfoDaoImpl();
		UserInfo user = userdao.get(text_userName.getText());
		if (user == null) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.user.notfound"));
			mb.open();
			return;
		}
		if (!user.getUser_pwd().equals(text_userPwd.getText())) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.userpwd.notcorrect"));
			mb.open();
			return;
		}
		Keeper keeper = new Keeper();
		keeper.setData("userinfo", user);
		close();
		keeper.show(true);
	}

	public static void main(String[] args) {
		Login login = new Login();
		login.show(true);
	}
}
