package org.nanfeng.common.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.nanfeng.common.action.AbstractAction;
import org.nanfeng.common.state.State;
import org.nanfeng.common.stateManager.AbstractStatusLine;
import org.nanfeng.common.util.parser.impl.GuiSettingsParser;
import org.nanfeng.common.util.resource.Resource;

public abstract class MainFrame extends ApplicationWindow {

	public static final String PARENT = "PARENT";

	private String titleName;
	private Image titleIcon;
	private int width;
	private int height;
	private Resource resource;
	private GuiSettingsParser parser;
	private URL menuSettingUrl;

	private List<State> actionsStateListeners;
	private List<State> stateStateListeners;

	private Map<String, Object> guiSettings;

	private Composite centralComposite;

	public MainFrame() {
		this(null, null, null);
	}

	public MainFrame(Resource resource) {
		this(null, resource, null);
	}

	public MainFrame(URL menuUrl) {
		this(null, null, menuUrl);
	}

	public MainFrame(Resource resource, URL settings) {
		this(null, resource, settings);
	}

	@SuppressWarnings("unchecked")
	public MainFrame(Shell parentShell, Resource resource, URL menuUrl) {
		super(parentShell);
		actionsStateListeners = new ArrayList<State>();
		stateStateListeners = new ArrayList<State>();
		parser = new GuiSettingsParser();
		if (resource != null)
			parser.setResource(resource);
		if (menuUrl != null) {
			setMenuSettingUrl(menuUrl);
			guiSettings = (Map<String, Object>) parser.parse(menuSettingUrl);
			addMenuBar();
			addStatusLine();
		}

	}

	protected Control createContents(Composite parent) {
		if (titleName != null)
			parent.getShell().setText(titleName);
		if (titleIcon != null)
			parent.getShell().setImage(titleIcon);
		if (width > 0 && height > 0)
			parent.setSize(width, height);
		parent.setLocation(Display.getCurrent().getClientArea().width / 2
				- parent.getShell().getSize().x / 2, Display.getCurrent()
				.getClientArea().height / 2 - parent.getShell().getSize().y / 2);
		centralComposite = new Composite(parent, SWT.BORDER);
		centralComposite.setLayout(new StackLayout());
		GridData gd = new GridData();
		centralComposite.setLayoutData(gd);
		setContents(centralComposite);
		if (guiSettings != null
				&& guiSettings.containsKey(GuiSettingsParser.STATE)) {
			AbstractStatusLine line = (AbstractStatusLine) guiSettings
					.get(GuiSettingsParser.STATE);
			stateStateListeners.add(line);
			notifyLineState(null);
		}
		return parent;
	}

	protected void setContents(Composite main) {

	}

	public Composite getCentralComposite() {
		return centralComposite;
	}

	public final void notifyActions(Object state) {
		for (State actionState : actionsStateListeners) {
			actionState.stateChanged(state);
		}
	}

	public final void notifyLineState(Object state) {
		for (State stateLine : stateStateListeners) {
			stateLine.stateChanged(state);
		}
	}

	@SuppressWarnings("unchecked")
	protected MenuManager createMenuManager() {
		MenuManager main_menu = new MenuManager();
		if (guiSettings != null
				&& guiSettings.containsKey(GuiSettingsParser.MENU)) {
			Map<MenuManager, ArrayList<AbstractAction>> menus = (Map<MenuManager, ArrayList<AbstractAction>>) guiSettings
					.get(GuiSettingsParser.MENU);
			for (Iterator<Entry<MenuManager, ArrayList<AbstractAction>>> iterator = menus
					.entrySet().iterator(); iterator.hasNext();) {
				Entry<MenuManager, ArrayList<AbstractAction>> entry = iterator
						.next();
				MenuManager mm = entry.getKey();
				main_menu.add(mm);
				ArrayList<AbstractAction> actions = entry.getValue();
				for (AbstractAction abstractAction : actions) {
					abstractAction.getContext().setData(PARENT, this);
					mm.add(abstractAction);
					actionsStateListeners.add(abstractAction);
				}
			}
		}
		return main_menu;
	}

	protected StatusLineManager createStatusLineManager() {
		if (guiSettings != null
				&& guiSettings.containsKey(GuiSettingsParser.STATE))
			return (AbstractStatusLine) guiSettings
					.get(GuiSettingsParser.STATE);
		return super.createStatusLineManager();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Image getTitleIcon() {
		return titleIcon;
	}

	public void setTitleIcon(Image titleIcon) {
		this.titleIcon = titleIcon;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public Resource getResource() {
		return resource;
	}

	private void setMenuSettingUrl(URL url) {
		this.menuSettingUrl = url;
	}

	public void show(boolean isBlock) {
		setBlockOnOpen(isBlock);
		open();
	}
}
