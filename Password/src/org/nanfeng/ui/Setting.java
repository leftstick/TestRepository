package org.nanfeng.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
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
import org.nanfeng.util.ResourceUtil;

public class Setting extends BaseDialog {
	private String userPwd;
	private Combo combo_question;
	private Text text_answer;
	private UserInfoDao userinfodao;

	public Setting() {
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
		Label text_userName = new Label(main, SWT.LEFT);
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 200;
		data2.minimumWidth = 200;
		data2.heightHint = 20;
		data2.minimumHeight = 20;
		text_userName.setLayoutData(data2);
		text_userName.setText(getData("userinfo", UserInfo.class)
				.getUser_name());

		Label label_userPwd = new Label(main, SWT.LEFT);
		label_userPwd.setText(ResourceUtil.instance().getString(
				simpleClassName + ".user_pwd"));
		label_userPwd.setLayoutData(data);
		Label text_userPwd = new Label(main, SWT.LEFT);
		text_userPwd.setLayoutData(data2);
		text_userPwd.setBackground(new Color(null, 255, 255, 255));
		userPwd = getData("userinfo", UserInfo.class).getUser_pwd();
		text_userPwd.setText(char2Pwd(userPwd));

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
		text_answer = new Text(main, SWT.LEFT);
		text_answer.setLayoutData(data2);
		text_answer.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					set();
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

		Button button_set = new Button(composite_buttons1, SWT.PUSH);
		button_set.setText(ResourceUtil.instance().getString(
				simpleClassName + ".set"));
		button_set.setLayoutData(new RowData(60, 25));
		button_set.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				set();
			}
		});

		Button button_exit = new Button(composite_buttons1, SWT.PUSH);
		button_exit.setText(ResourceUtil.instance().getString(
				simpleClassName + ".exit"));
		button_exit.setLayoutData(new RowData(60, 25));
		button_exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
	}

	private String char2Pwd(String chars) {
		char[] cs = chars.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			sb.append("*");
		}
		return sb.toString();
	}

	private void set() {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText(ResourceUtil.instance().getString("common.error"));
		if (combo_question.getSelectionIndex() == -1
				|| combo_question.getItem(combo_question.getSelectionIndex())
						.length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.question.empty"));
			mb.open();
			return;
		}
		if (text_answer.getText().length() == 0) {
			mb.setMessage(ResourceUtil.instance().getString(
					"common.error.answer.empty"));
			mb.open();
			return;
		}

		UserInfo user = getData("userinfo", UserInfo.class);
		user.setQuestion(combo_question.getSelectionIndex()+"");
		user.setAnswer(text_answer.getText());

		if (userinfodao == null)
			userinfodao = new UserInfoDaoImpl();

		try {
			userinfodao.update(user);
		} catch (Exception e) {
			mb.setMessage(e.getMessage());
			mb.open();
			return;
		}

		Keeper keeper = new Keeper();
		keeper.setData("userinfo", user);
		close();
		keeper.show(true);
	}
}
