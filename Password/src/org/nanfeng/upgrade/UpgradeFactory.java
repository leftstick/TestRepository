package org.nanfeng.upgrade;

import java.util.ArrayList;
import java.util.List;

import org.nanfeng.upgrade.impl.Upgrade1;

public class UpgradeFactory {
	private static List<Upgrade> list = new ArrayList<Upgrade>();
	static {
		list.add(new Upgrade1());
	}

	public static List<Upgrade> getUpgradeList() {
		return list;
	}
}
