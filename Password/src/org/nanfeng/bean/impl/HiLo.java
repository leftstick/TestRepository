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

package org.nanfeng.bean.impl;

import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.bean.Pojo;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-17
 */
public class HiLo extends Pojo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4165508451692095771L;

	@PrimaryKey
	private String table_name;

	private String max_value;

	/**
	 * @return ���� table_name��
	 */
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @param tableName
	 *            Ҫ���õ� table_name��
	 */
	public void setTable_name(String tableName) {
		table_name = tableName;
	}

	/**
	 * @return ���� max_value��
	 */
	public String getMax_value() {
		return max_value;
	}

	/**
	 * @param maxValue
	 *            Ҫ���õ� max_value��
	 */
	public void setMax_value(String maxValue) {
		max_value = maxValue;
	}

	public String getTableName() {
		return "HI_LO";
	}

}
