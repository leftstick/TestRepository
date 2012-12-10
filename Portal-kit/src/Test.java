import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The purpose of this class is main entrance of this toolkit.
 */
public class Test {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        // Construct a Tree object.  
        final Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK);

        File rootFile = new File("C:\\Users\\ehaozuo\\workenv\\msdk\\portal-team");

        // Add root file to the tree.  
        TreeItem root = new TreeItem(tree, SWT.NONE);

        // Set the text displayed on the tree node.  
        root.setText(rootFile.getName());

        // Set the data type of the tree node.  
        root.setData(rootFile);

        // Add a empty sub-node to make the node can be expanded.  
        new TreeItem(root, SWT.NONE);

        tree.addListener(SWT.Expand, new Listener() {
            @Override
            public void handleEvent(Event event) {
                // Get the event-source  
                TreeItem root = (TreeItem) event.item;

                // Dispose the empty node  
                TreeItem[] items = root.getItems();
                for (TreeItem item : items) {
                    if (item.getData() == null) {
                        item.dispose();
                    }
                }

                File rootFile = (File) root.getData();
                File[] files = rootFile.listFiles();

                for (File file : files) {
                    if (file.getName().startsWith(".")) {
                        continue;
                    }

                    TreeItem item = new TreeItem(root, SWT.NONE);
                    item.setText(file.getName());
                    item.setData(file);

                    if (file.isDirectory()) {
                        // Add a empty sub-node to make the node can be  
                        // expanded.  
                        System.out.println(file.getAbsolutePath());
                        new TreeItem(item, SWT.NONE);
                    }
                }
            }
        });

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
