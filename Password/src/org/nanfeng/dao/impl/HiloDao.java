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

import org.nanfeng.bean.impl.HiLo;
import org.nanfeng.db.Connection;
import org.nanfeng.db.Statement;
import org.nanfeng.exception.RecordNotExistsException;
import org.nanfeng.util.DataBaseConstants;

import com.sleepycat.je.Transaction;

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

	public String getMaxValue(String tableName) {
		Connection<HiLo> connection = Connection.createConnection(
				DataBaseConstants.envHome, HiLo.class);
		Statement<HiLo> stmt = connection.createStatement();
		Transaction tran = stmt.beginTransaction();
		HiLo hilo = null;
		try {
			hilo = stmt.getByPrimaryKey(tableName);
		} catch (RecordNotExistsException e) {

		}
		if (hilo == null) {
			hilo = new HiLo();
			hilo.setMax_value("1");
			hilo.setTable_name(tableName);
			stmt.insert(hilo);
		} else {
			hilo.setMax_value((Integer.parseInt(hilo.getMax_value()) + 1) + "");
			stmt.update(hilo);
		}
		tran.commit();
		connection.close();
		return hilo.getMax_value();
	}

}
