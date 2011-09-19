/*
 * 
 *  
 *  项目名称：   BDBTest 
 *
 *  公司名称:   上海诺祺科技有限公司
 *  
 *  年份：     2011
 *  创建日期:   2011-1-17 
 *  CVS版本:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-17
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
