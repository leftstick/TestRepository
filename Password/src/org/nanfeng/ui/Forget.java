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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;
import org.nanfeng.util.DialogFactory;
import org.nanfeng.util.ResourceUtil;

public class Forget extends BaseDialog {
	private Text text_userName;
	private Combo combo_question;
	private Text text_answer;
	private UserInfoDao userinfodao;

	public Forget() {
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

		Label label_note = new Label(main, SWT.LEFT);
		label_note.setText(ResourceUtil.instance().getString(
				simpleClassName + ".note"));
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.horizontalSpan = 2;
		label_note.setLayoutData(data1);

		Label label_useName = new Label(main, SWT.LEFT);
		label_useName.setText(ResourceUtil.instance().getString(
				simpleClassName + ".user_name"));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 100;
		data.minimumWidth = 100;
		label_useName.setLayoutData(data);
		text_userName = new Text(main, SWT.LEFT | SWT.BORDER);
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 200;
		data2.minimumWidth = 200;
		data2.heightHint = 20;
		data2.minimumHeight = 20;
		text_userName.setLayoutData(data2);
		text_userName.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					get();
				}
			}
		});

		Label label_question = new Label(main, SWT.LEFT);
		label_question.setText(ResourceUtil.instance().getString(
				simpleClassName + ".question"));
		label_question.setLayoutData(data);
		combo_question = new Combo(main, SWT.FLAT | SWT.BORDER | SWT.READ_ONLY);
		combo_question.setLayoutData(data2);
		String[] questions = new String[4];
		for (int i = 0; i < questions.length; i++) {
			questions[i] = ResourceUtil.instance().getString(
					simpleClassName + ".question_" + (i + 1));
		}
		combo_question.setItems(questions);

		Label label_answer = new Label(main, SWT.LEFT);
		label_answer.setText(ResourceUtil.instance().getString(
				simpleClassName + ".answer"));
		label_answer.setLayoutData(data);
		text_answer = new Text(main, SWT.LEFT | SWT.BORDER);
		text_answer.setLayoutData(data2);
		text_answer.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					get();
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

		Button button_get = new Button(composite_buttons1, SWT.PUSH);
		button_get.setText(ResourceUtil.instance().getString(
				simpleClassName + ".get"));
		button_get.setLayoutData(new RowData(60, 25));
		button_get.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				get();
			}
		});

		Button button_exit = new Button(composite_buttons1, SWT.PUSH);
		button_exit.setText(ResourceUtil.instance().getString(
				simpleClassName + ".cancel"));
		button_exit.setLayoutData(new RowData(50, 25));
		button_exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
				Forget.this.getData("login", Login.class).show(true);
			}
		});
	}

	private void get() {
		if (text_userName.getText().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.username.empty"));
			return;
		}
		if (combo_question.getSelectionIndex() == -1) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.question.empty"));
			return;
		}
		if (text_answer.getText().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.answer.empty"));
			return;
		}

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();
		UserInfo user = null;
		try {
			user = userinfodao.get(text_userName.getText());
		} catch (Exception e) {
			DialogFactory.openError(getShell(), e.getMessage());
			return;
		}
		if (user == null) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.user.notfound"));
			return;
		}
		if (user.getQuestion().trim().length() == 0) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.user.question.notfound"));
			return;

		}
		int index = -1;
		try {
			index = Integer.parseInt(user.getQuestion());
		} catch (NumberFormatException e) {
		}
		if (combo_question.getSelectionIndex() != index) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.question.error"));
			return;
		}
		if (!text_answer.getText().equals(user.getAnswer())) {
			DialogFactory.openError(getShell(), ResourceUtil.instance()
					.getString("common.error.answer.error"));
			return;
		}
		DialogFactory.openInformation(getShell(), ResourceUtil.instance()
				.getString("common.pwd") + user.getUser_pwd());
	}
}
