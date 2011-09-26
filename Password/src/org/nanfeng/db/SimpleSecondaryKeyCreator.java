/*
 * 
 *  
 *  项目名称：   BDBTest 
 *
 *  公司名称:   上海诺祺科技有限公司
 *  
 *  年份：     2011
 *  创建日期:   2011-1-14 
 *  CVS版本:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.db;

import org.apache.commons.beanutils.BeanUtils;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-14
 */
public class SimpleSecondaryKeyCreator<E> implements SecondaryKeyCreator {

	private final EntryBinding<E> binding;

	private final String keyName;

	public SimpleSecondaryKeyCreator(EntryBinding<E> bind, String key_name) {
		binding = bind;
		keyName = key_name;
	}

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		try {
			E e = binding.entryToObject(data);
			Object keyValue = BeanUtils.getProperty(e, keyName);
			result.setData(keyValue.toString().getBytes("UTF-8"));
		} catch (Exception e1) {
			return false;
		}
		return true;
	}

}
