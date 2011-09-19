/*
 * 
 *  
 *  ��Ŀ���ƣ�   PasswordProtector 
 *
 *  ��˾����:   �Ϻ�ŵ���Ƽ����޹�˾
 *  
 *  ��ݣ�     2011
 *  ��������:   2011-1-12 
 *  CVS�汾:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.exception;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-12
 */
public class RecordExistsException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4552796412566265101L;
	
	public RecordExistsException()
	{
		super();
	}
	
	public RecordExistsException(String msg)
	{
		super(msg);
	}
	
	public RecordExistsException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
