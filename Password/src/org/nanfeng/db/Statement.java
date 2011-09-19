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

package org.nanfeng.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nanfeng.exception.DatabaseException;
import org.nanfeng.exception.RecordExistsException;
import org.nanfeng.exception.RecordNotExistsException;
import org.nanfeng.table.TableStructure;
import org.nanfeng.util.Context;
import org.nanfeng.util.StringUtil;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.JoinCursor;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

/**
 * @purpose
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-14
 */
public class Statement<E> {
	private final Connection<E> connection;

	private final String table_name;

	private final Class<E> database_class;

	private Database database_primarykey;

	private EntryBinding<E> bind_primary_key;

	private DatabaseConfig config_primarykey;

	private Database database_class_info;

	private StoredClassCatalog log_class_info;

	private SecondaryConfig config_secondarykey;

	private Map<String, SecondaryDatabase> secondaryDatabases = new LinkedHashMap<String, SecondaryDatabase>();

	private Transaction transactions;
	private TableStructure<E> tableStructure;

	protected Statement(Connection<E> conn, Class<E> cls) {
		tableStructure = TableStructure.wrapTableStructure(cls);
		connection = conn;
		database_class = cls;
		table_name = tableStructure.tableName();
		initDatabaseConfig();
		initSecondaryConfig();
	}

	private void initDatabaseConfig() {
		config_primarykey = new DatabaseConfig();
		config_primarykey.setAllowCreate(true);
		config_primarykey.setReadOnly(false);
		config_primarykey.setTransactional(true);
		database_primarykey = connection.getEnvironment().openDatabase(null,
				table_name, config_primarykey);

		database_class_info = connection.getEnvironment().openDatabase(null,
				"CLASS_INFO", config_primarykey);

		log_class_info = new StoredClassCatalog(database_class_info);

		bind_primary_key = new SerialBinding<E>(log_class_info, database_class);
	}

	private void initSecondaryConfig() {
		List<String> secondaryKeys = tableStructure.secondaryKeys();
		for (String key : secondaryKeys) {
			config_secondarykey = new SecondaryConfig();
			config_secondarykey.setAllowCreate(true);
			config_secondarykey.setReadOnly(false);
			config_secondarykey.setSortedDuplicates(true);
			config_secondarykey.setTransactional(true);
			config_secondarykey.setKeyCreator(new SimpleSecondaryKeyCreator<E>(
					new SerialBinding<E>(log_class_info, database_class), key));
			secondaryDatabases.put(
					key,
					connection.getEnvironment().openSecondaryDatabase(null,
							table_name + "_" + key, database_primarykey,
							config_secondarykey));
		}

	}

	public List<E> get() {
		List<E> list = new LinkedList<E>();
		Cursor cursor = null;
		try {
			if (transactions != null)
				cursor = database_primarykey.openCursor(transactions, null);
			else
				cursor = database_primarykey.openCursor(null, null);
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();
			while (cursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				E e = bind_primary_key.entryToObject(data);
				list.add(e);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return list;
	}

	public E getByPrimaryKey(String primaryKey) {
		DatabaseEntry key = new DatabaseEntry();
		key.setData(StringUtil.getBytes(primaryKey, "UTF-8"));
		DatabaseEntry data = new DatabaseEntry();
		E po = null;
		if (transactions != null) {
			if (database_primarykey.get(transactions, key, data,
					LockMode.DEFAULT) == OperationStatus.SUCCESS)
				po = bind_primary_key.entryToObject(data);
		} else {

			if (database_primarykey.get(null, key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS)
				po = bind_primary_key.entryToObject(data);

		}
		if (po == null)
			throw new RecordNotExistsException("record doesn't exists");
		return po;
	}

	public List<E> getBySecondaryKey(Context context) {
		List<E> list = new LinkedList<E>();
		JoinCursor joinCursor = null;
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		List<SecondaryCursor> cursors = new ArrayList<SecondaryCursor>();
		try {
			for (Entry<String, String> entry : context.entrySet()) {
				DatabaseEntry data = new DatabaseEntry(StringUtil.getBytes(
						entry.getValue(), "UTF-8"));
				DatabaseEntry foundData1 = new DatabaseEntry();
				SecondaryCursor secCursor;
				if (transactions != null)
					secCursor = secondaryDatabases.get(entry.getKey())
							.openCursor(transactions, null);
				else
					secCursor = secondaryDatabases.get(entry.getKey())
							.openCursor(null, null);
				secCursor.getSearchKey(data, foundData1, LockMode.DEFAULT);
				cursors.add(secCursor);
			}
			if (cursors.isEmpty())
				return list;
			joinCursor = database_primarykey.join(
					cursors.toArray(new SecondaryCursor[cursors.size()]), null);
			while (joinCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				E e = bind_primary_key.entryToObject(foundData);
				list.add(e);
			}
		} finally {
			for (int i = 0; i < cursors.size(); i++) {
				if (cursors.get(i) != null)
					cursors.get(i).close();
			}
			if (joinCursor != null)
				joinCursor.close();
		}
		return list;
	}

	public void insert(E pojo) {
		DatabaseEntry key = new DatabaseEntry();
		String primaryValue = tableStructure.primaryValue(pojo);
		key.setData(StringUtil.getBytes(primaryValue, "UTF-8"));
		DatabaseEntry data = new DatabaseEntry();
		E info = null;
		try {
			info = getByPrimaryKey(primaryValue);
		} catch (RecordNotExistsException e) {
		}
		if (info != null)
			throw new RecordExistsException("数据表[" + table_name + "]中键["
					+ primaryValue + "]已存在");
		bind_primary_key.objectToEntry(pojo, data);
		OperationStatus os;
		if (transactions != null)
			os = database_primarykey.put(transactions, key, data);
		else
			os = database_primarykey.put(null, key, data);
		if (OperationStatus.SUCCESS != os)
			throw new DatabaseException("insert "
					+ pojo.getClass().getSimpleName() + " error");
	}

	public void update(E pojo) {
		DatabaseEntry key = new DatabaseEntry();
		String primaryKey = tableStructure.primaryValue(pojo);

		key.setData(StringUtil.getBytes(primaryKey, "UTF-8"));
		DatabaseEntry data = new DatabaseEntry();
		getByPrimaryKey(primaryKey);
		bind_primary_key.objectToEntry(pojo, data);
		OperationStatus os;
		if (transactions != null)
			os = database_primarykey.put(transactions, key, data);
		else
			os = database_primarykey.put(null, key, data);
		if (OperationStatus.SUCCESS != os)
			throw new DatabaseException("update "
					+ pojo.getClass().getSimpleName() + " error");
	}

	public void delete(String primaryKey) {
		DatabaseEntry key = new DatabaseEntry();
		key.setData(StringUtil.getBytes(primaryKey, "UTF-8"));
		OperationStatus os;
		if (transactions != null)
			os = database_primarykey.delete(transactions, key);
		else
			os = database_primarykey.delete(null, key);
		if (OperationStatus.SUCCESS != os)
			throw new DatabaseException("delete " + primaryKey + " error");
	}

	public Transaction beginTransaction() {
		transactions = connection.getEnvironment().beginTransaction(null, null);
		return transactions;
	}

	protected void close() {
		if (transactions != null && transactions.isValid())
			transactions.commit();
		for (Iterator<Entry<String, SecondaryDatabase>> iterator = secondaryDatabases
				.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, SecondaryDatabase> entry = iterator.next();
			if (entry.getValue() != null) {
				entry.getValue().close();
				iterator.remove();
			}
		}
		if (log_class_info != null)
			log_class_info.close();
		if (database_class_info != null)
			database_class_info.close();
		if (database_primarykey != null)
			database_primarykey.close();
	}
}
