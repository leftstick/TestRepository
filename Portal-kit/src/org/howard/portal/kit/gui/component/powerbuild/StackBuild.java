package org.howard.portal.kit.gui.component.powerbuild;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.howard.portal.kit.config.PowerBuildConfig;
import org.howard.portal.kit.gui.PortalKit;
import org.howard.portal.kit.gui.component.StackComponent;
import org.howard.portal.kit.gui.component.powerbuild.ExecuteList.ExecuteItem;
import org.howard.portal.kit.gui.factory.BuildStatus;
import org.howard.portal.kit.gui.factory.CompositeFactory;
import org.howard.portal.kit.gui.factory.TreeLogic;
import org.howard.portal.kit.gui.listener.ExecutionCallback;
import org.howard.portal.kit.gui.listener.SelectCallback;

/**
 * The purpose of this class is to provide composite of build page
 */
public class StackBuild implements StackComponent, Observer {
    private SashForm mainForm;
    private StackLayout stackLayout;

    private TreeLogic treeLogic;

    private List<TreeItem> selection;
    private SettingsInStackBuild settings;
    private ExecuteList exeList;

    private PortalKit kit;

    /**
     * Creates a new instance of <code>StackBuild</code>.
     * 
     * @param kit
     */
    public StackBuild(PortalKit kit) {
        this.kit = kit;
        treeLogic = new TreeLogic();
        selection = new ArrayList<TreeItem>();
        exeList = new ExecuteList();
        exeList.setCallback(new ExecutionCallback() {
            @Override
            public void onChanged(ExecuteItem item) {
                settings.setItem(item.itemText, item.status);
            }
        });
        PowerBuildConfig.getConfig().addObserver(this);
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
                for (TreeItem item : treeItem) {
                    settings.addItem(item, BuildStatus.NOTEXEC);
                }
            }

            @Override
            public void onCanceled(List<TreeItem> treeItem) {
                System.out.println(selection.removeAll(treeItem));
            }
        });

        SashForm eastMainForm = CompositeFactory.createSashForm(mainForm, SWT.VERTICAL | SWT.BORDER, 1);
        settings = new SettingsInStackBuild(eastMainForm);
        settings.setExeList(exeList);

        Composite eastSouth = CompositeFactory.createGridComposite(eastMainForm, 1);
        Text tConsole = CompositeFactory.createReadOnlyText(eastSouth);

        stackLayout = ((StackLayout) composite.getLayout());
        return mainForm;
    }

    @Override
    public void close() {
        if (mainForm != null) {
            mainForm.setVisible(false);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PowerBuildConfig) {
            String designPath = ((PowerBuildConfig) o).getDesignPath();
            treeLogic.setRoot(new File(designPath));
        }
    }

    @Override
    public void show() {
        kit.setTextToStatusbar("Power Build");
        kit.setToTop(mainForm);
    }
}
