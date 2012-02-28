package com.dreamworks.portal.deploy.tool.action.impl;

import org.eclipse.swt.widgets.Composite;
import org.nanfeng.common.action.SwitchPanelBaseAction;
import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.gui.LogPage;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.gui.log4j.appender.LogPageAppender;
import org.nanfeng.common.state.Submit;
import org.nanfeng.common.util.resource.Resource;

import com.dreamworks.portal.deploy.tool.configuration.Config;
import com.dreamworks.portal.deploy.tool.container.Tomcat;

public class TomcatAction extends SwitchPanelBaseAction {
	private Tomcat tomcat;

	public TomcatAction(Resource resource, String text) {
		super(resource, text);
	}

	public int getStyle() {
		return AS_PUSH_BUTTON;
	}

	public void operation() {
		setStateStr(getResourceStr("menu.file.tomcat"));
	}

	public Composite makeChild(final MainFrame mainFrame) {
		Composite centralComposite = mainFrame.getCentralComposite();
		final LogPage logPage = new LogPage(centralComposite, 2);
		logPage.setButtonText(0,
				getResourceStr("menu.file.tomcat.button.start"));
		logPage.setButtonText(1, getResourceStr("menu.file.tomcat.button.stop"));
		synchronized (tomcat) {
			tomcat.notify();
		}
		logPage.addSubmit(0, new Submit() {
			public void run(Object args) {
				if (!Config.getConfig().isTomcatFilled()) {
					logPage.append(
							getResourceStr("message.preferences.catalinaHomePath.non"),
							true);
					return;
				}
				Context cxt = new ContextImpl();
				cxt.setData("tomcat.state", new Boolean(true));
				mainFrame.notifyActions(cxt);
				try {
					tomcat.startTomcat();
				} catch (Exception e) {
				}
			}
		});
		logPage.addSubmit(1, new Submit() {
			public void run(Object args) {
				if (!Config.getConfig().isTomcatFilled()) {
					logPage.append(
							getResourceStr("message.preferences.catalinaHomePath.non"),
							true);
					return;
				}
				Context cxt = new ContextImpl();
				cxt.setData("tomcat.state", new Boolean(false));
				mainFrame.notifyActions(cxt);
				try {
					tomcat.stopTomcat();
				} catch (Exception e) {
				}
			}
		});
		LogPageAppender.getAppender().setLog(logPage);
		return logPage;
	}

	public void stateChanged(Object state) {
		if (state instanceof Context) {
			Context cxt = (Context) state;
			if (cxt.getData("tomcat.state", Boolean.class) != null) {
				Boolean boo = cxt.getData("tomcat.state", Boolean.class);
				if (getChild() != null) {
					((LogPage) getChild()).setButtonsEnable(new boolean[] {
							!boo, boo });
				}
			} else if (cxt.getData("tomcat", Tomcat.class) != null) {
				tomcat = cxt.getData("tomcat", Tomcat.class);
			}
		}
	}
}
