package org.sf.easyexplore.actions;

import java.io.File;
import java.text.MessageFormat;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.sf.easyexplore.EasyExplorePlugin;

public class EasyExploreAction
  implements IObjectActionDelegate
{
  private Object selected = null;
  private Class selectedClass = null;

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  public void run(IAction action)
  {
    try
    {
      if ("unknown".equals(this.selected)) {
        MessageDialog.openInformation(new Shell(), "Easy Explore", "Unable to explore " + this.selectedClass.getName());
        EasyExplorePlugin.log("Unable to explore " + this.selectedClass);
        return;
      }
      File directory = null;
      if ((this.selected instanceof IResource))
        directory = new File(((IResource)this.selected).getLocation().toOSString());
      else if ((this.selected instanceof File)) {
        directory = (File)this.selected;
      }
      if ((this.selected instanceof IFile)) {
        directory = directory.getParentFile();
      }
      if ((this.selected instanceof File)) {
        directory = directory.getParentFile();
      }
      String target = EasyExplorePlugin.getDefault().getTarget();

      if (!EasyExplorePlugin.getDefault().isSupported()) {
        MessageDialog.openInformation(new Shell(), "Easy Explore", 
          "This platform (" + 
          System.getProperty("os.name") + 
          ") is currently unsupported.\n" + 
          "You can try to provide the correct command to execute in the Preference dialog.\n" + 
          "If you succeed, please be kind to post your discovery on EasyExplore website http://sourceforge.net/projects/easystruts,\n" + 
          "or by email farialima@users.sourceforge.net. Thanks !");
        return;
      }

      if (target.indexOf("{0}") == -1) {
        target = target.trim() + " {0}";
      }

      target = MessageFormat.format(target, new String[] { directory.toString() });
      try
      {
        EasyExplorePlugin.log("running: " + target);
        Runtime.getRuntime().exec(target);
      } catch (Throwable t) {
        MessageDialog.openInformation(new Shell(), "Easy Explore", "Unable to execute " + target);
        EasyExplorePlugin.log(t);
      }
    } catch (Throwable e) {
      EasyExplorePlugin.log(e);
    }
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    try
    {
      IAdaptable adaptable = null;
      this.selected = "unknown";
      if ((selection instanceof IStructuredSelection)) {
        adaptable = (IAdaptable)((IStructuredSelection)selection).getFirstElement();
        this.selectedClass = adaptable.getClass();
        if ((adaptable instanceof IResource))
          this.selected = ((IResource)adaptable);
        else if (((adaptable instanceof PackageFragment)) && ((((PackageFragment)adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot)))
          this.selected = getJarFile(((PackageFragment)adaptable).getPackageFragmentRoot());
        else if ((adaptable instanceof JarPackageFragmentRoot))
          this.selected = getJarFile(adaptable);
        else
          this.selected = ((IResource)adaptable.getAdapter(IResource.class));
      }
    }
    catch (Throwable e) {
      EasyExplorePlugin.log(e);
    }
  }

  protected File getJarFile(IAdaptable adaptable) {
    JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot)adaptable;
    File selected = jpfr.getPath().makeAbsolute().toFile();
    if (!selected.exists()) {
      File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
      selected = new File(projectFile.getParent() + selected.toString());
    }
    return selected;
  }
}