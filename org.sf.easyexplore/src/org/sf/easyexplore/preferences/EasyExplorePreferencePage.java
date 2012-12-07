package org.sf.easyexplore.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.sf.easyexplore.EasyExplorePlugin;

public class EasyExplorePreferencePage extends FieldEditorPreferencePage
  implements IWorkbenchPreferencePage
{
  public static final String P_TARGET = "org.sf.easyexplore.targetPreference";

  public EasyExplorePreferencePage()
  {
    super(1);
    setPreferenceStore(EasyExplorePlugin.getDefault().getPreferenceStore());
    setDescription("Set up your file explorer application.");
  }

  public void createFieldEditors()
  {
    addField(new StringFieldEditor("org.sf.easyexplore.targetPreference", "&Target:", getFieldEditorParent()));
  }

  public void init(IWorkbench workbench)
  {
  }
}