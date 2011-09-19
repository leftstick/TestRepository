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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.bean.impl.UserInfo;
import org.nanfeng.dao.UserInfoDao;
import org.nanfeng.dao.impl.UserInfoDaoImpl;
import org.nanfeng.ui.face.BaseDialog;

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
		parent.getShell().setText("Password->Forget");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		main.setLayout(gridLayout);

		Label label_note = new Label(main, SWT.LEFT);
		label_note
				.setText("Note:You have to write the correct answer to get your password back");
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.horizontalSpan = 2;
		label_note.setLayoutData(data1);

		Label label_useName = new Label(main, SWT.LEFT);
		label_useName.setText("User Name:");
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
		label_question.setText("Question:");
		label_question.setLayoutData(data);
		combo_question = new Combo(main, SWT.FLAT | SWT.BORDER | SWT.READ_ONLY);
		combo_question.setLayoutData(data2);
		combo_question.setItems(new String[] { "Where were you born?",
				"When were you born?", "What is your father's name?",
				"What is your mother's name?" });

		Label label_answer = new Label(main, SWT.LEFT);
		label_answer.setText("Answer:");
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
		button_get.setText("Get");
		button_get.setLayoutData(new RowData(50, 25));
		button_get.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				get();
			}
		});

		Button button_exit = new Button(composite_buttons1, SWT.PUSH);
		button_exit.setText("Cancel");
		button_exit.setLayoutData(new RowData(50, 25));
		button_exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
				Forget.this.getData("login", Login.class).show(true);
			}
		});
	}

	private void get() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText("Error");
		if (text_userName.getText().length() == 0) {
			mb.setMessage("user name must not be empty");
			mb.open();
			return;
		}
		if (combo_question.getSelectionIndex() == -1
				|| combo_question.getItem(combo_question.getSelectionIndex())
						.length() == 0) {
			mb.setMessage("user question must not be empty");
			mb.open();
			return;
		}
		if (text_answer.getText().length() == 0) {
			mb.setMessage("user answer must not be empty");
			mb.open();
			return;
		}

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();
		UserInfo user = null;
		try {
			user = userinfodao.get(text_userName.getText());
		} catch (Exception e) {
			mb.setMessage(e.getMessage());
			mb.open();
			return;
		}
		if (!combo_question.getItem(combo_question.getSelectionIndex()).equals(
				user.getQuestion())) {
			mb.setMessage("wrong question");
			mb.open();
			return;
		}
		if (!text_answer.getText().equals(user.getAnswer())) {
			mb.setMessage("wrong answer");
			mb.open();
			return;
		}
		mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
		mb.setText("Information");
		mb.setMessage("Password: " + user.getUser_pwd());
		mb.open();

	}
}
