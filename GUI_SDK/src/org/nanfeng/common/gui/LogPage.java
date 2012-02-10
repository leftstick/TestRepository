package org.nanfeng.common.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.common.state.Submit;

public class LogPage extends Composite {
	private Text text;
	private List<Button> buttons;

	private List<Submit> submits;

	public LogPage(Composite parent, int buttonNum) {
		super(parent, SWT.NONE);
		init(buttonNum);
	}

	public void addSubmit(int index, Submit sub) {
		submits.add(index, sub);
	}

	public void setButtonText(int index, String value) {
		Button b = buttons.get(index);
		if (b != null)
			b.setText(value);
		else
			throw new IndexOutOfBoundsException("you set a wrong index");
	}

	public void append(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				text.append(message);
			}
		});
	}

	public void clear() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				text.setText("");
			}
		});
	}

	public void setButtonsEnable(final boolean[] boos) {
		if (boos.length == buttons.size()) {
			for (int i = 0; i < boos.length; i++) {
				final Button b = buttons.get(i);
				final boolean boo = boos[i];
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						b.setEnabled(boo);
					}
				});
			}
		}
	}

	private void init(int buttonNum) {
		submits = new ArrayList<Submit>(buttonNum);
		buttons = new ArrayList<Button>(buttonNum);
		setLayout(new GridLayout(1, true));
		setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text = new Text(this, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY
				| SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite bottom = new Composite(this, SWT.RIGHT_TO_LEFT);
		bottom.setLayout(new RowLayout(SWT.HORIZONTAL));
		bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		for (int i = 0; i < buttonNum; i++) {
			Button button = new Button(bottom, SWT.PUSH);
			button.setText("BUTTON_0");
			button.addSelectionListener(new ButtonClicked(i));
			buttons.add(button);
		}
	}

	private void doSubmit(int index) {
		if (submits.size() > index) {
			submits.get(index).run(buttons.get(index));
		}
	}

	class ButtonClicked extends SelectionAdapter {
		int index;

		ButtonClicked(int i) {
			index = i;
		}

		public void widgetSelected(SelectionEvent e) {
			doSubmit(index);
		}
	}
}
