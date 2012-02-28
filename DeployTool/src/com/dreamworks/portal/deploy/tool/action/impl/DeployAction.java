package com.dreamworks.portal.deploy.tool.action.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Composite;
import org.nanfeng.common.action.SwitchPanelBaseAction;
import org.nanfeng.common.gui.DialogFactory;
import org.nanfeng.common.gui.LogPage;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.state.Submit;
import org.nanfeng.common.util.resource.Resource;

import com.dreamworks.portal.deploy.tool.configuration.Config;

public class DeployAction extends SwitchPanelBaseAction {
	private Config config;

	public DeployAction(Resource resource, String text) {
		super(resource, text);
		config = Config.getConfig();
	}

	public int getStyle() {
		return AS_PUSH_BUTTON;
	}

	public void operation() {
		getContext()
				.getData(MainFrame.PARENT, MainFrame.class)
				.notifyLineState(getResourceStr("menu.deploy.deploynewpackage"));
	}

	public Composite makeChild(final MainFrame mainFrame) {
		final Composite centralComposite = mainFrame.getCentralComposite();
		final LogPage logPage = new LogPage(centralComposite, 1);
		logPage.setButtonText(0, getResourceStr("menu.deploy.open"));
		logPage.addSubmit(0, new Submit() {
			public void run(Object args) {
				logPage.clear();
				String path = DialogFactory.openFileDialog(logPage.getShell());
				if (path == null) {
					logPage.append("选择程序包路径为空，请重新选择", true);
					return;
				}
				File parentDir = new File(config.getCatalinaHomePath(),
						"webapps");
				File oldDir = new File(parentDir, "DreamGO");
				if (!oldDir.exists()) {
					logPage.append(
							getResourceStr(
									"message.directory.nonexists.delete.non",
									new Object[] { oldDir.getAbsolutePath() }),
							false);
				} else {
					try {
						FileUtils.deleteDirectory(oldDir);
						logPage.append(
								getResourceStr(
										"message.directory.exists.delete.suc",
										new Object[] { oldDir.getAbsolutePath() }),
								false);
					} catch (IOException e) {
						logPage.append(
								getResourceStr(
										"message.directory.exists.delete.fai",
										new Object[] { oldDir.getAbsolutePath() }),
								true);
					}
				}
				File oldWar = new File(parentDir, "DreamGO.war");
				if (!oldWar.exists()) {
					logPage.append(
							getResourceStr("message.file.nonexists.delete.non",
									new Object[] { oldWar.getAbsolutePath() }),
							false);
				} else {
					boolean f = FileUtils.deleteQuietly(oldWar);
					if (f)
						logPage.append(
								getResourceStr(
										"message.file.exists.delete.suc",
										new Object[] { oldWar.getAbsolutePath() }),
								false);
					else
						logPage.append(
								getResourceStr(
										"message.file.exists.delete.fai",
										new Object[] { oldWar.getAbsolutePath() }),
								true);
				}
				File srcFile = null;
				try {
					srcFile = new File(path);
					FileUtils.copyFileToDirectory(srcFile, parentDir);
					logPage.append(
							getResourceStr("message.file.copy.suc",
									new Object[] { srcFile.getAbsoluteFile(),
											parentDir.getAbsolutePath() }),
							false);
				} catch (IOException e) {
					logPage.append(
							getResourceStr("message.file.copy.fai",
									new Object[] { srcFile.getAbsoluteFile(),
											parentDir.getAbsolutePath() }),
							true);
				}

			}
		});
		return logPage;
	}
}
