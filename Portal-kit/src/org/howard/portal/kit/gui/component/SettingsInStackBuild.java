package org.howard.portal.kit.gui.component;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.howard.portal.kit.gui.util.CompositeFactory;
import org.howard.portal.kit.gui.util.TableLogic;

/**
 * The purpose of this class is to provide Settings component in
 * StackBuild page
 */
public class SettingsInStackBuild {
    private Composite mainSettings;
    private Composite stack;
    private Composite buildPage;
    private Composite settingsPage;

    private TableLogic tableLogic;

    /**
     * Creates a new instance of <code>SettingsInStackBuild</code>.
     * 
     * @param parent
     */
    public SettingsInStackBuild(Composite parent) {
        mainSettings = CompositeFactory.createGridComposite(parent, 1);
        stack = CompositeFactory.createStackComposite(mainSettings);
        //side 1
        buildPage = CompositeFactory.createGridComposite(stack, 1);
        Composite cBuildTitle = CompositeFactory.createHorFillComposite(buildPage);
        Button bSettings = CompositeFactory.createPushButton(cBuildTitle, "Settings", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ((StackLayout) stack.getLayout()).topControl = settingsPage;
                stack.layout();
            }
        });

        Button bFwBuild = CompositeFactory.createRadioButton(cBuildTitle, "FW", null);
        Button bMockBuild = CompositeFactory.createRadioButton(cBuildTitle, "Mock", null);
        Text tSelection = CompositeFactory.createReadOnlyText(buildPage);
        Composite cBuildBottom = CompositeFactory.createHorFillComposite(buildPage);

        Button bBuild = CompositeFactory.createPushButton(cBuildBottom, "Start", null);
        Button bCheckBuild = CompositeFactory.createCheckButton(cBuildBottom, "Build", null);
        Button bCheckDeploy = CompositeFactory.createCheckButton(cBuildBottom, "Deploy", null);

        //side 2
        settingsPage = CompositeFactory.createGridComposite(stack, 1);
        Composite csetTitle = CompositeFactory.createHorFillComposite(settingsPage);

        Button bSave = CompositeFactory.createPushButton(csetTitle, "Save", null);
        Button bBack = CompositeFactory.createPushButton(csetTitle, "Back", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ((StackLayout) stack.getLayout()).topControl = buildPage;
                stack.layout();
            }
        });
        tableLogic= new TableLogic(settingsPage);
        tableLogic.addTableHeader("Key", false);
        tableLogic.addTableHeader("Value", true);
        tableLogic.addTableColumn("Tomcat");
        tableLogic.addTableColumn("");
        tableLogic.prepareTable();

        ((StackLayout) stack.getLayout()).topControl = buildPage;
    }
}
