package org.nanfeng.common.util.parser.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.action.MenuManager;
import org.nanfeng.common.action.AbstractAction;
import org.nanfeng.common.stateManager.AbstractStatusLine;
import org.nanfeng.common.util.parser.XmlParser;
import org.nanfeng.common.util.parser.exception.UrlException;
import org.nanfeng.common.util.parser.exception.XmlStructureException;
import org.nanfeng.common.util.reflect.InstanceUtils;
import org.nanfeng.common.util.resource.Resource;

public class GuiSettingsParser implements XmlParser {
	public static final String MENU = "MENU";
	public static final String STATE = "STATE";

	private static final String MANAGERS = "Managers";
	private static final String MENU_MANAGER = "MenuManager";
	private static final String ACTION = "Action";

	private static final String STATE_LINE_MANAGER = "StateLineManager";

	private static final String NAME = "name";
	private static final String NAME_KEY = "nameKey";
	private static final String SHORTCUT = "shortcut";

	private Resource resource;

	@SuppressWarnings("unchecked")
	public Object parse(URL url) {
		Map<String, Object> settings = new HashMap<String, Object>();
		Map<MenuManager, ArrayList<AbstractAction>> menus = new LinkedHashMap<MenuManager, ArrayList<AbstractAction>>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(url);
		} catch (DocumentException e1) {
			throw new UrlException(e1);
		}
		Element root = document.getRootElement();
		if (!MANAGERS.equals(root.getName()))
			throw new XmlStructureException(
					"Error structure in GuiSettings file [" + url.getFile()
							+ "],error root");
		List<Element> managers = root.elements(MENU_MANAGER);
		if (managers != null) {
			Class<Resource>[] types = new Class[] { Resource.class,String.class };
			Object[] args = new Object[] { resource,null };
			for (Element manager : managers) {
				final String mName = manager.attributeValue(NAME);
				final String mNameKey = manager.attributeValue(NAME_KEY);
				final String mshortcut = manager.attributeValue(SHORTCUT);
				checkAttributes(mName,mNameKey,mshortcut,url,MENU_MANAGER);
				
				final String nameValue = mName == null ? resource
						.getString(mNameKey) : mName;
				MenuManager mm = new MenuManager(nameValue
						+ ((mshortcut == null) ? "" : mshortcut.trim().equals("")?"":"(&" + mshortcut + ")"));
				menus.put(mm, new ArrayList<AbstractAction>());

				List<Element> actions = manager.elements(ACTION);
				for (Element action : actions) {
					final String aName = action.attributeValue(NAME);
					final String aNameKey = action.attributeValue(NAME_KEY);
					final String ashortcut = action.attributeValue(SHORTCUT);
					checkAttributes(aName,aNameKey,ashortcut,url,ACTION);
					Class<AbstractAction> class1 = null;
					try {
						class1 = (Class<AbstractAction>) ClassUtils
								.getClass(action.getText());
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					String text=aName ==null?resource.getString(aNameKey):aName;
					String shortKey=ashortcut==null?"":ashortcut.trim().equals("")?"":"@"+ashortcut;
					args[1]="&"+text+shortKey;
					AbstractAction newAction = (AbstractAction) InstanceUtils
							.getInstance(class1, types, args);
					menus.get(mm).add(newAction);
				}
			}
			settings.put(MENU, menus);
		}
		Element stateLine = root.element(STATE_LINE_MANAGER);
		if (stateLine != null) {
			Class<Resource>[] types = new Class[] { Resource.class };
			Object[] args = new Object[] { resource };
			Class<AbstractStatusLine> class2 = null;
			try {
				class2 = (Class<AbstractStatusLine>) ClassUtils
						.getClass(stateLine.getText());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			AbstractStatusLine newState = (AbstractStatusLine) InstanceUtils
					.getInstance(class2, types, args);
			settings.put(STATE, newState);
		}
		return settings;
	}

	public void checkAttributes(String name, String nameKey, String shortcut,
			URL url, String source) {
		if (name != null && nameKey != null)
			throw new XmlStructureException(source
					+ " Error in file [" + url.getFile() + "],"
					+ NAME + " and " + NAME_KEY
					+ " can't be used at the same time");
		if (name == null && nameKey == null)
			throw new XmlStructureException(source
					+ " Error in file [" + url.getFile()
					+ "],at least one of " + NAME + " and " + NAME_KEY
					+ " should be used");
		if (resource == null && name == null)
			throw new XmlStructureException(source
					+ " Error in file [" + url.getFile() + "],"
					+ NAME + " is mandatory if there is no Resource implementation");
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
