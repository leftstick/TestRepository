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

package org.nanfeng.bean.impl;

import org.nanfeng.annotation.PrimaryKey;
import org.nanfeng.bean.Pojo;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-17
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
	 * @return 返回 table_name。
	 */
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @param tableName
	 *            要设置的 table_name。
	 */
	public void setTable_name(String tableName) {
		table_name = tableName;
	}

	/**
	 * @return 返回 max_value。
	 */
	public String getMax_value() {
		return max_value;
	}

	/**
	 * @param maxValue
	 *            要设置的 max_value。
	 */
	public void setMax_value(String maxValue) {
		max_value = maxValue;
	}

	public String getTableName() {
		return "HI_LO";
	}

}
