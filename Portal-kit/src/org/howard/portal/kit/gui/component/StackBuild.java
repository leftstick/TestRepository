package org.howard.portal.kit.gui.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
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

        treeLogic.createTree(mainForm);
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
        Composite eastNorth = CompositeFactory.createGridComposite(eastMainForm, 1);
        Composite eastNorthNorth = CompositeFactory.createHorFillComposite(eastNorth);
        Button bSettings = CompositeFactory.createPushButton(eastNorthNorth, "Settings", null);
        Button bFwBuild = CompositeFactory.createRadioButton(eastNorthNorth, "FW", null);
        Button bMockBuild = CompositeFactory.createRadioButton(eastNorthNorth, "Mock", null);

        Composite eastNorthSouth = new Composite(eastNorth, SWT.NONE);
        eastNorthSouth.setLayout(new StackLayout());
        eastNorthSouth.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite eastNorthSouthSettings = new Composite(eastNorthSouth, SWT.NONE);
        eastNorthSouthSettings.setLayout(new GridLayout(1, false));
        eastNorthSouthSettings.setLayoutData(new GridData(GridData.FILL_BOTH));
        Text tSelection = new Text(eastNorthSouthSettings, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
        tSelection.setEnabled(false);
        tSelection.setLayoutData(new GridData(GridData.FILL_BOTH));
        tSelection.setBackground(new Color(null, 255, 255, 255));
        Composite eastNorthSouthBuild = new Composite(eastNorthSouthSettings, SWT.NONE);
        FillLayout eastNorthSouthBuildFillLayout = new FillLayout(SWT.HORIZONTAL);
        eastNorthSouthBuildFillLayout.spacing = 5;
        eastNorthSouthBuild.setLayout(eastNorthSouthBuildFillLayout);
        Button bBuild = new Button(eastNorthSouthBuild, SWT.PUSH);
        bBuild.setText("Build");

        ((StackLayout) eastNorthSouth.getLayout()).topControl = eastNorthSouthSettings;

        Composite eastSouth = new Composite(eastMainForm, SWT.NONE);
        eastSouth.setLayout(new GridLayout(1, false));
        eastSouth.setLayoutData(new GridData(GridData.FILL_BOTH));
        Text tConsole = new Text(eastSouth, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
        tConsole.setEnabled(false);
        tConsole.setLayoutData(new GridData(GridData.FILL_BOTH));
        tConsole.setBackground(new Color(null, 255, 255, 255));

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
