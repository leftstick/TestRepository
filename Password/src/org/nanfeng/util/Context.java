/*
 * 
 *  
 *  ��Ŀ���ƣ�   BDBTest 
 *
 *  ��˾����:   �Ϻ�ŵ���Ƽ����޹�˾
 *  
 *  ��ݣ�     2011
 *  ��������:   2011-1-17 
 *  CVS�汾:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-17
 */
public class Context
{
	private Map<String, String>	map	= new HashMap<String, String>();
	
	public void setValue(String key, String value)
	{
		map.put(key, value);
	}
	
	public Set<Entry<String, String>> entrySet()
	{
		return map.entrySet();
	}
}
