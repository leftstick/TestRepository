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

package org.nanfeng.exception;

/**
 * @purpose 数据库异常
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-14
 */
public class DatabaseException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6803474352767480778L;
	
	public DatabaseException()
	{
		super();
	}
	
	public DatabaseException(String msg)
	{
		super(msg);
	}
	
	public DatabaseException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
