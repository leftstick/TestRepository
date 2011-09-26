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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;
import org.nanfeng.util.DialogFactory;
import org.nanfeng.util.ResourceUtil;

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
		parent.getShell().setText(
				ResourceUtil.instance().getString(simpleClassName + ".title"));
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		main.setLayout(gridLayout);

		Label label_oldpwd = new Label(main, SWT.LEFT);
		label_oldpwd.setText(ResourceUtil.instance().getString(
				simpleClassName + ".oldpwd"));
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
		label_newPwd.setText(ResourceUtil.instance().getString(
				simpleClassName + ".newpwd"));
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
		label_newPwd2.setText(ResourceUtil.instance().getString(
				simpleClassName + ".reppwd"));
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
		button_change.setText(ResourceUtil.instance().getString(
				simpleClassName + ".submit"));
		button_change.setLayoutData(new RowData(50, 25));
		button_change.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				change();
			}
		});

		Button button_cancel = new Button(composite_buttons1, SWT.PUSH);
		button_cancel.setText(ResourceUtil.instance().getString(
				simpleClassName + ".cancel"));
		button_cancel.setLayoutData(new RowData(50, 25));
		button_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
	}

	private void change() {
		if (text_oldpwd.getText().trim().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString(simpleClassName + ".oldpwd.empty"));
			return;
		}
		if (text_newPwd.getText().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString(simpleClassName + ".newpwd.empty"));
			return;
		}
		if (text_newPwd2.getText().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString(simpleClassName + ".repnewpwd.empty"));
			return;
		}
		if (!text_newPwd2.getText().equals(text_newPwd.getText())) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString(simpleClassName + ".oldnew.not.same"));
			return;
		}
		UserInfo user = getData("userinfo", UserInfo.class);
		if (!text_oldpwd.getText().equals(user.getUser_pwd())) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString(simpleClassName + ".oldpwd.not.correct"));
			return;
		}

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();
		user.setUser_pwd(text_newPwd2.getText());
		try {
			userinfodao.update(user);
		} catch (Exception e) {
			DialogFactory.openError(getShell(), e.getMessage());
			return;
		}
		DialogFactory.openInformation(getShell(), ResourceUtil.instance()
				.getString("common.update.successful"));
		close();
	}
}
