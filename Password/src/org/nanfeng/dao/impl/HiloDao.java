/*
 * 
 *  
 *  ��Ŀ���ƣ�   PasswordProtector 
 *
 *  ��˾����:   �Ϻ�ŵ���Ƽ����޹�˾
 *  
 *  ��ݣ�     2011
 *  ��������:   2011-1-17 
 *  CVS�汾:    $Id: codetemplates.xml,v 1.1 2007/08/06 08:17:54 linxin Exp $
 */

package org.nanfeng.dao.impl;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-17
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
