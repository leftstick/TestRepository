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

package org.nanfeng.exception;

/**
 * @purpose ���ݿ��쳣
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-14
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
