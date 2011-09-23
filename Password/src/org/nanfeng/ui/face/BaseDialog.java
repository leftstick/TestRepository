package org.nanfeng.ui.face;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class BaseDialog extends ApplicationWindow implements IDialog {

	private Map<String, Object> context;
	protected final String simpleClassName;

	public BaseDialog(Shell parentShell) {
		super(parentShell);
		context = new HashMap<String, Object>();
		simpleClassName = getClass().getSimpleName();
	}

	public void setData(String key, Object obj) {
		if (key != null && obj != null)
			context.put(key, obj);
		else if (key != null && obj == null)
			context.remove(key);
		else if (key == null)
			throw new RuntimeException(
					"set data error:the key must not be null");
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(String key, Class<T> cls) {
		if (key == null)
			throw new RuntimeException(
					"set data error:the key must not be null");
		Object obj = null;
		obj = context.get(key);
		if (obj != null)
			return (T) obj;
		return null;
	}

	protected Control createContents(Composite parent) {
		parent.getShell().setImage(
				new Image(null, this.getClass().getResourceAsStream(
						"icon/password.jpg")));
		initContents(parent);
		return parent;
	}

	protected abstract void initContents(Composite parent);

	public void show(boolean isBlock) {
		setBlockOnOpen(isBlock);
		open();
	}

}
