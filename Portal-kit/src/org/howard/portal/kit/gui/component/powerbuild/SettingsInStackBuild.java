package org.howard.portal.kit.gui.component.powerbuild;

import java.util.List;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.howard.portal.kit.config.PowerBuildConfig;
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

    private TableLogic tableLogic;

    private PowerBuildConfig config;

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
                ((StackLayout) stack.getLayout()).topControl = settingsPage;
                stack.layout();
                tableLogic.onShow();
                bSave.setEnabled(false);
            }
        });

        Text tSelection = CompositeFactory.createReadOnlyText(buildPage);
        Composite cBuildBottom = CompositeFactory.createHorFillComposite(buildPage);

        Button bBuild = CompositeFactory.createPushButton(cBuildBottom, "Start", null);
        Button bCheckBuild = CompositeFactory.createCheckButton(cBuildBottom, "Build", null);
        Button bCheckDeploy = CompositeFactory.createCheckButton(cBuildBottom, "Deploy", null);

        //side 2
        settingsPage = CompositeFactory.createGridComposite(stack, 1);
        Composite csetTitle = CompositeFactory.createHorFillComposite(settingsPage);

        bSave = CompositeFactory.createPushButton(csetTitle, "Save", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    List<List<String>> result = tableLogic.getResult();
                    if (config.resetConfig(result)) {
                        DialogFactory.openInfo(mainSettings.getShell(), "Save success!");
                        tableLogic.resetData(result);
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
        tableLogic = new TableLogic(settingsPage);
        tableLogic.addTableHeader("Key", false);
        tableLogic.addTableHeader("Value", true);
        tableLogic.addTableColumn("Design Path", "");
        tableLogic.addTableColumn("Deploy Path");
        tableLogic.prepareTable();
        tableLogic.setCallback(new TextChanedCallback() {
            @Override
            public void onChanged(boolean isChanged) {
                bSave.setEnabled(isChanged);
            }
        });

        ((StackLayout) stack.getLayout()).topControl = buildPage;
    }
}
