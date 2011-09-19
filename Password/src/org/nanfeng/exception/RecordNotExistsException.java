/*
 * 
 *  
 *  项目名称：   PasswordProtector 
 *
 *  公司名称:   上海诺祺科技有限公司
 *  
 *  年份：     2011
 *  创建日期:   2011-1-12 
 *  CVS版本:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.exception;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-12
 */
public class RecordNotExistsException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4552796412566265101L;
	
	public RecordNotExistsException()
	{
		super();
	}
	
	public RecordNotExistsException(String msg)
	{
		super(msg);
	}
	
	public RecordNotExistsException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
