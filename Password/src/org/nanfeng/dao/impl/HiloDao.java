/*
 * 
 *  
 *  项目名称：   PasswordProtector 
 *
 *  公司名称:   上海诺祺科技有限公司
 *  
 *  年份：     2011
 *  创建日期:   2011-1-17 
 *  CVS版本:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.dao.impl;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-17
 */
public class HiloDao {
	private static HiloDao instance;

	public synchronized static HiloDao getInstance() {
		if (instance == null)
			instance = new HiloDao();
		return instance;
	}

	public String getValue() {
		long l = System.currentTimeMillis();
		return l + "";
	}

}
