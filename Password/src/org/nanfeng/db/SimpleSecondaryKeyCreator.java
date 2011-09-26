/*
 * 
 *  
 *  ��Ŀ���ƣ�   BDBTest 
 *
 *  ��˾����:   �Ϻ�ŵ���Ƽ����޹�˾
 *  
 *  ��ݣ�     2011
 *  ��������:   2011-1-14 
 *  CVS�汾:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.db;

import org.apache.commons.beanutils.BeanUtils;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-14
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
