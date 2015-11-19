/**
 * 
 */
package com.etipl.labResults.nosql.framework.nosql.spi;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.etipl.labResults.nosql.framework.annotations.HRowIdentifier;
import com.etipl.nosql.framework.connection.HConfiguration;
import com.etipl.nosql.framework.exception.HBaseException;

/**
 * @author Sushant
 *
 */
public class HBaseServiceImpl<T> extends AbstractService<T> implements IHBaseService<T>{

	
	@Override
	public List<T> getResultList(Class<T> t) {
		
		if (t == null)
			throw new IllegalArgumentException("Input Class is null");
		
		HTable table = null;
		Scan scan = new Scan();
		//scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL));
		ResultScanner rs = null;
		List<T> lpd = null;
		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), HBaseUtils.getTableName(t));
			rs = table.getScanner(scan);
			if (rs != null) {
				lpd = new ArrayList<T>();

				for (Result rr : rs) {
						lpd.add(getNewObject(t,rr));
				}
				
				return lpd;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			closeHTableConnection(table);

		}

		return lpd;
	}

	@Override
	public T getSingleResult(Class<T> t, String rowKey) {
		if (t == null)
			throw new IllegalArgumentException("Input Class is null");
		if (rowKey == null || rowKey.isEmpty())
		{
			throw new IllegalArgumentException("Rowkey cannot be null");
			
		}
		HTable table = null;
		
		Get get = new Get(Bytes.toBytes(rowKey));
		
		//scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL));
		Result rr = null;
		
		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), HBaseUtils.getTableName(t));
			rr = table.get(get);
			return getNewObject(t, rr);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			closeHTableConnection(table);

		}

		return null;
	}

	@Override
	public T persist(T t) throws HBaseException{
		
		if (t == null)
			throw new IllegalArgumentException("Classname cannot be null");
		
		HTable table = null;
		Put put = new Put(Bytes.toBytes(HBaseUtils.getKeyFieldValue(t)));
		Field fields[] = t.getClass().getDeclaredFields();

		for (Field f : fields) {

			if (f.isAnnotationPresent(HRowIdentifier.class))
			{
				continue;
			}
			String columnFamilyName = HBaseUtils.getColumnFamily(f);
			String columnName = HBaseUtils.getColumnName(f);
			String columnValue = ReflectionUtils.getFieldValue(t, f.getName());
			if (columnValue == null)
			{
				columnValue = "";
			}
			put.add(Bytes.toBytes(columnFamilyName), Bytes.toBytes(columnName),
					Bytes.toBytes(columnValue));
		}

		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), HBaseUtils.getTableName(t.getClass()));
			table.put(put);
			table.flushCommits();
		} catch (RetriesExhaustedWithDetailsException e) {
			throw new HBaseException(e);
		} catch (InterruptedIOException e) {
			throw new HBaseException(e);
		} catch (IOException e) {
			throw new HBaseException(e);
		} finally {
			closeHTableConnection(table);
		}

		return t;
	}

	@Override
	public T update(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Class<T> t, String rowKey) throws HBaseException{
		// TODO: Perform validation over t and rowKey for null and empty checks.
		HTable table;
		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), HBaseUtils.getTableName(t));
		} catch (IOException e) {
			throw new HBaseException(e);
		}

		/**
		 * For deleting the entire row, just instantiate the Delete class with
		 * the rowKey as the unique Identifier.
		 */
		Delete delete = new Delete(Bytes.toBytes(rowKey));

		try {
			table.delete(delete);
		} catch (IOException e) {
			throw new HBaseException(e);
		} finally {
			closeHTableConnection(table);
		}
	}

}
