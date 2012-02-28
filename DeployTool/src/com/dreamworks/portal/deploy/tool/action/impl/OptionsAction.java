package com.dreamworks.portal.deploy.tool.action.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.swt.widgets.Composite;
import org.nanfeng.common.action.SwitchPanelBaseAction;
import org.nanfeng.common.gui.DialogFactory;
import org.nanfeng.common.gui.MainFrame;
import org.nanfeng.common.gui.SettingsPage;
import org.nanfeng.common.state.Submit;
import org.nanfeng.common.util.resource.Resource;

import com.dreamworks.portal.deploy.tool.configuration.Config;
import com.dreamworks.portal.deploy.tool.thread.util.ConfigStateThread;

public class OptionsAction extends SwitchPanelBaseAction {

	public OptionsAction(Resource resource, String text) {
		super(resource, text);
	}

	public int getStyle() {
		return AS_PUSH_BUTTON;
	}

	public Composite makeChild(MainFrame mainFrame) {
		final Config config = Config.getConfig();
		Composite centralComposite = mainFrame.getCentralComposite();
		final SettingsPage settings = new SettingsPage(centralComposite,
				getResourceStr("settingsPage.key.text"),
				getResourceStr("settingsPage.value.text"),
				SettingsPage.TEXT_COLUMN);
		final String prefix = "menu.preferences.options.";
		final List<String> keys = getKeys(prefix);
		final List<Entry<String, String>> pairs = new ArrayList<Entry<String, String>>();
		for (int i = 0; i < keys.size(); i++) {
			final String key = keys.get(i);
			final String value = getResourceStr(keys.get(i));
			pairs.add(new Entry<String, String>() {
				public String setValue(String value) {
					return null;
				}

				public String getValue() {
					return value;
				}

				public String getKey() {
					return key;
				}
			});
		}
		Collections.sort(pairs, new Comparator<Entry<String, String>>() {
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				return Collator.getInstance(getLocale()).compare(o1.getValue(),
						o2.getValue());
			}
		});
		for (int i = 0; i < pairs.size(); i++) {
			try {
				settings.addLineText(
						(i + 1) + "." + pairs.get(i).getValue(),
						PropertyUtils.getProperty(config,
								pairs.get(i).getKey().substring(prefix.length()))
								.toString());
			} catch (Exception e) {
				settings.addLineText(pairs.get(i).getValue(), "");
			}
		}
		settings.setSubmit(getResourceStr("settingsPage.submit.text"),
				new Submit() {
					public void run(Object args) {
						@SuppressWarnings("unchecked")
						List<Object> list = (List<Object>) args;
						Properties p = null;
						try {
							p = ConfigStateThread.loadProperties();
						} catch (Exception e) {
							p = new Properties();
						}
						boolean isDirty = false;
						String key;
						for (int i = 0; i < list.size(); i++) {
							String value = list.get(i).toString();
							key = pairs.get(i).getKey().substring(prefix.length());
							try {
								if (!value.equals(PropertyUtils.getProperty(
										config, key).toString())) {
									p.setProperty(key, value);
									isDirty = true;
								}
							} catch (Exception e) {

							}
						}

						if (isDirty) {
							try {
								ConfigStateThread.storeProperties(p);
								config.makeDirty();
							} catch (Exception e) {
							}
							DialogFactory.openInformation(
									settings.getShell(),
									getResourceStr("dialog.information.title"),
									getResourceStr("dialog.information.button.ok"),
									getResourceStr("dialog.information.message.preferences.success"));
						} else
							DialogFactory.openInformation(
									settings.getShell(),
									getResourceStr("dialog.information.title"),
									getResourceStr("dialog.information.button.ok"),
									getResourceStr("dialog.information.message.preferences.nonUpdate"));
					}
				});
		mainFrame.notifyLineState(getResourceStr("menu.preferences.options"));
		return settings;
	}

	@Override
	public void operation() {
		setStateStr(getResourceStr("menu.preferences.options"));
	}
}
