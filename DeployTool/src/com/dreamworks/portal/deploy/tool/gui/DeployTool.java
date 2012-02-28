package com.dreamworks.portal.deploy.tool.gui;

import java.net.URL;
import java.util.Observer;

import org.apache.log4j.PropertyConfigurator;
import org.nanfeng.common.context.Context;
import org.nanfeng.common.context.impl.ContextImpl;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.util.resource.Resource;

import com.dreamworks.portal.deploy.tool.configuration.Config;
import com.dreamworks.portal.deploy.tool.container.Tomcat;
import com.dreamworks.portal.deploy.tool.container.impl.ProcessTomcat;
import com.dreamworks.portal.deploy.tool.resource.impl.ResourceWithoutDB;
import com.dreamworks.portal.deploy.tool.thread.TaskRunner;
import com.dreamworks.portal.deploy.tool.thread.util.ConfigStateThread;
import com.dreamworks.portal.deploy.tool.thread.util.TomcatStateThread;

public class DeployTool extends MainFrame {

	public DeployTool(URL settings) {
		super(null, null, settings);
	}

	public DeployTool(Resource resource, URL settings) {
		super(null, resource, settings);
	}

	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("profile/log4j.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resource res = new ResourceWithoutDB();
		DeployTool portal = new DeployTool(res,
				DeployTool.class.getResource("GuiSetting.xml"));
		portal.setTitleName(res.getString("deploy.tool.title"));
		Tomcat tomcat = ProcessTomcat.getInstance(Config.getConfig());
		Context cxt = new ContextImpl();
		cxt.setData("tomcat", tomcat);
		portal.notifyActions(cxt);
		ConfigStateThread conThread = ConfigStateThread.getInstance();
		conThread.addObserver((Observer) tomcat);
		TaskRunner.instance.addService(conThread);
		TaskRunner.instance.addService(TomcatStateThread.getInstance(portal,
				tomcat));
		TaskRunner.instance.startAll();
		portal.show(true);
		System.out.println("stopAll");
		TaskRunner.instance.stopAll();
	}
}
