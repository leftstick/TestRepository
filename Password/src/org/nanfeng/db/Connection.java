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

package org.nanfeng.db;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @purpose
 * @author �Ϸ�
 * @version $Revision: 1.1 $ �������� 2011-1-14
 */
public final class Connection<E> {
	private Environment env;
	private Class<E> clazz;

	private Statement<E> statements;

	public static <E> Connection<E> createConnection(File envHome, Class<E> cls) {
		return new Connection<E>(envHome, cls);
	}

	private Connection(File envHome, Class<E> cls) {
		clazz = cls;
		try {
			FileUtils.forceMkdir(envHome);
		} catch (IOException e) {
			e.printStackTrace();
		}
		EnvironmentConfig envConfig = new EnvironmentConfig();

		envConfig.setReadOnly(false);

		envConfig.setAllowCreate(true);

		envConfig.setTransactional(true);
		env = new Environment(envHome, envConfig);
	}

	public Statement<E> createStatement() {
		statements = new Statement<E>(this, clazz);
		return statements;
	}

	protected Environment getEnvironment() {
		return env;
	}

	public void close() {
		statements.close();
		if (env != null)
			env.close();
	}
}
