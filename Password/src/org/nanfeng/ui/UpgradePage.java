package org.nanfeng.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.nanfeng.ui.face.BaseDialog;
import org.nanfeng.upgrade.Upgrade;
import org.nanfeng.upgrade.UpgradeFactory;
import org.nanfeng.util.ResourceUtil;

public class UpgradePage extends BaseDialog {

	public UpgradePage() {
		super(null);
		setShellStyle(SWT.NONE);
	}

	protected void initContents(Composite parent) {
		parent.getShell().setText(
				ResourceUtil.instance().getString(simpleClassName + ".title"));
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, true);
		main.setLayout(gridLayout);
		final Label label_text = new Label(main, SWT.CENTER);
		label_text.setText(ResourceUtil.instance().getString(
				simpleClassName + ".checkupgrade"));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 300;
		data.minimumWidth = 300;
		label_text.setLayoutData(data);
		final Button button_ok = new Button(main, SWT.PUSH);
		button_ok.setText(ResourceUtil.instance().getString(
				simpleClassName + ".button.ok"));
		button_ok.setEnabled(false);
		button_ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_ok.setLayoutData(data);
		final List<Upgrade> list = UpgradeFactory.getUpgradeList();
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				int upgradeNum = 0;
				for (final Upgrade upgrade : list) {
					if (upgrade.needUpgrade()) {
						label_text.setText(ResourceUtil.instance().getString(
								simpleClassName + ".upgrade"));
						upgrade.upgrade();
						upgradeNum++;
					}
				}
				if (upgradeNum > 0)
					label_text.setText(ResourceUtil.instance().getString(
							simpleClassName + ".upgrade.finish"));
				else
					label_text.setText(ResourceUtil.instance().getString(
							simpleClassName + ".upgrade.nothing"));
				button_ok.setEnabled(true);
			}
		});
	}

	public static void main(String[] args) {
		UpgradePage up = new UpgradePage();
		up.show(true);
	}
}
