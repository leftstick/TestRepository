package org.howard.portal.kit.gui.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.howard.portal.kit.gui.util.CompositeFactory;
import org.howard.portal.kit.gui.util.SelectCallback;
import org.howard.portal.kit.gui.util.TreeLogic;

/**
 * The purpose of this class is to provide composite of build page
 */
public class StackBuild implements StackComponent {
    private SashForm mainForm;
    private StackLayout stackLayout;

    private TreeLogic treeLogic;

    private List<TreeItem> selection;

    /**
     * Creates a new instance of <code>StackBuild</code>.
     */
    public StackBuild() {
        treeLogic = new TreeLogic();
        selection = new ArrayList<TreeItem>();
    }

    public String convert() {
        StringBuffer sb = new StringBuffer();
        for (TreeItem s : selection) {
            sb.append(s.getText() + "||");
        }
        return sb.toString();
    }

    @Override
    public Composite ceatePanel(Composite composite) {
        if (mainForm != null)
            return mainForm;
        mainForm = CompositeFactory.createSashForm(composite, SWT.HORIZONTAL, 2);

        treeLogic.createTree(mainForm, new File("E:\\Study\\portal-team"));
        treeLogic.setOnSelectCallback(new SelectCallback() {
            @Override
            public void onSelected(List<TreeItem> treeItem) {
                selection.addAll(treeItem);
                System.out.println("ALL after add = " + convert());
            }

            @Override
            public void onCanceled(List<TreeItem> treeItem) {
                System.out.println(selection.removeAll(treeItem));
                System.out.println("ALL after remove = " + convert());
            }
        });

        SashForm eastMainForm = CompositeFactory.createSashForm(mainForm, SWT.VERTICAL | SWT.BORDER, 1);
        SettingsInStackBuild settings=new SettingsInStackBuild(eastMainForm);
        
//        Composite eastNorth = CompositeFactory.createGridComposite(eastMainForm, 1);
//        Composite eastNorthNorth = CompositeFactory.createHorFillComposite(eastNorth);
//        Button bSettings = CompositeFactory.createPushButton(eastNorthNorth, "Settings", null);
//
//        Composite eastNorthSouthStack = CompositeFactory.createStackComposite(eastNorth);
//        
//        Composite buildComp=CompositeFactory.createGridComposite(eastNorthSouthStack, 1);
//        final Composite eastNorthSouthSettings = CompositeFactory.createGridComposite(eastNorthSouthStack, 1);
//        Text tSelection = CompositeFactory.createReadOnlyText(eastNorthSouthSettings);
//        Composite eastNorthSouthBuild = CompositeFactory.createHorFillComposite(eastNorthSouthSettings);
//        
//        Button bFwBuild = CompositeFactory.createRadioButton(eastNorthNorth, "FW", null);
//        Button bMockBuild = CompositeFactory.createRadioButton(eastNorthNorth, "Mock", null);
//        
//        Button bBuild = CompositeFactory.createPushButton(eastNorthSouthBuild, "Start", null);
//        Button bCheckBuild = CompositeFactory.createCheckButton(eastNorthSouthBuild, "Build", null);
//        Button bCheckDeploy = CompositeFactory.createCheckButton(eastNorthSouthBuild, "Deploy", null);
//
//        ((StackLayout) eastNorthSouthStack.getLayout()).topControl = eastNorthSouthSettings;
//
        Composite eastSouth = CompositeFactory.createGridComposite(eastMainForm, 1);
        Text tConsole = CompositeFactory.createReadOnlyText(eastSouth);

        stackLayout = ((StackLayout) composite.getLayout());
        stackLayout.topControl = mainForm;
        mainForm.layout();
        return mainForm;
    }

    @Override
    public void close() {
        if (mainForm != null) {
            mainForm.setVisible(false);
        }
    }
}
