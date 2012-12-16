package org.howard.portal.kit.gui.component.powerbuild;

import java.util.List;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.howard.portal.kit.config.PowerBuildConfig;
import org.howard.portal.kit.gui.component.powerbuild.ExecuteList.ExecuteItem;
import org.howard.portal.kit.gui.factory.BuildStatus;
import org.howard.portal.kit.gui.factory.CompositeFactory;
import org.howard.portal.kit.gui.factory.DialogFactory;
import org.howard.portal.kit.gui.factory.TableLogic;
import org.howard.portal.kit.gui.listener.TextChanedCallback;

/**
 * The purpose of this class is to provide Settings component in
 * StackBuild page
 */
public class SettingsInStackBuild {
    private Composite mainSettings;
    private Composite stack;
    private Composite buildPage;
    private Composite settingsPage;
    private Button bSave;
    private TableLogic tabSelection;

    private TableLogic tabSetting;

    private PowerBuildConfig config;

    private ExecuteList exeList;

    /**
     * Creates a new instance of <code>SettingsInStackBuild</code>.
     * 
     * @param parent
     */
    public SettingsInStackBuild(Composite parent) {
        config = PowerBuildConfig.getConfig();
        mainSettings = CompositeFactory.createGridComposite(parent, 1);
        stack = CompositeFactory.createStackComposite(mainSettings);
        //side 1
        buildPage = CompositeFactory.createGridComposite(stack, 1);
        Composite cBuildTitle = CompositeFactory.createHorFillComposite(buildPage);
        CompositeFactory.createPushButton(cBuildTitle, "Settings", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tabSetting.onShow();
                ((StackLayout) stack.getLayout()).topControl = settingsPage;
                stack.layout();
                bSave.setEnabled(false);
            }
        });

        tabSelection = new TableLogic(buildPage);
        tabSelection.addTableHeader("Item", false);
        tabSelection.addTableHeader("Status", false);
        tabSelection.prepareTable();

        Composite cBuildBottom = CompositeFactory.createHorFillComposite(buildPage);

        Button bStart = CompositeFactory.createPushButton(cBuildBottom, "Start", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        exeList.startExec();
                    }
                });
            }
        });
        Button bCheckBuild = CompositeFactory.createCheckButton(cBuildBottom, "Build", null);
        Button bCheckDeploy = CompositeFactory.createCheckButton(cBuildBottom, "Deploy", null);

        //side 2
        settingsPage = CompositeFactory.createGridComposite(stack, 1);
        Composite csetTitle = CompositeFactory.createHorFillComposite(settingsPage);

        bSave = CompositeFactory.createPushButton(csetTitle, "Save", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    List<List<String>> result = tabSetting.getResult();
                    if (config.resetConfig(result)) {
                        DialogFactory.openInfo(mainSettings.getShell(), "Save success!");
                        tabSetting.resetData(result);
                        bSave.setEnabled(false);
                    } else
                        DialogFactory.openInfo(mainSettings.getShell(), "Nothing changed!");
                } catch (RuntimeException ex) {
                    DialogFactory.openError(mainSettings.getShell(), ex.getMessage());
                }
            }
        });
        Button bBack = CompositeFactory.createPushButton(csetTitle, "Back", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ((StackLayout) stack.getLayout()).topControl = buildPage;
                stack.layout();
            }
        });
        tabSetting = new TableLogic(settingsPage);
        tabSetting.addTableHeader("Key", false);
        tabSetting.addTableHeader("Value", true);
        tabSetting.addTableColumn("Design Path", "");
        tabSetting.addTableColumn("Deploy Path");
        tabSetting.prepareTable();
        tabSetting.setCallback(new TextChanedCallback() {
            @Override
            public void onChanged(boolean isChanged) {
                bSave.setEnabled(isChanged);
            }
        });

        ((StackLayout) stack.getLayout()).topControl = buildPage;
    }

    /**
     * @param item
     * @param status
     */
    public void addItem(TreeItem item, BuildStatus status) {
        ExecuteItem e = exeList.addItem(item.getText(), (String) item.getData("path"), status);
        tabSelection.addTableColumn(e.itemText, e.status.getValue());
    }

    /**
     * @param text
     * @param status
     */
    public void setItem(String text, BuildStatus status) {
        int index = exeList.indexOf(text);
        tabSelection.setTableColumn(index, 1, status.getValue());

    }

    /**
     * @param exeList
     */
    public void setExeList(ExecuteList exeList) {
        this.exeList = exeList;
    }

}
