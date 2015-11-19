/**
 * 
 */
package com.etipl.hbase.spi.internal;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.OneToMany;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.etipl.hbase.annotations.HJSONField;
import com.etipl.hbase.annotations.HRowIdentifier;
import com.etipl.hbase.connection.HConfiguration;
import com.etipl.hbase.exception.ApplicationException;
import com.etipl.hbase.exception.HBaseException;
import com.etipl.hbase.spi.IHBaseService;

/**
 * @author Sushant
 *
 */
public class HBaseServiceImpl<T> extends AbstractService<T> implements IHBaseService<T>{

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getResultList(Class<T> t) {
		
		if (t == null)
			throw new IllegalArgumentException("Input Class is null");
		
		HTable table = null;
		Scan scan = new Scan();
		
		ResultScanner rs = null;
		List<T> lpd = null;
		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), HBaseUtils.getTableName(t));
			
			rs = table.getScanner(scan);
			if (rs != null) {
				lpd = new ArrayList<T>();

				for (Result rr : rs) {
						lpd.add((T)getNewObject(t,rr));
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

	@SuppressWarnings("unchecked")
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
			return (T)getNewObject(t, rr);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			closeHTableConnection(table);

		}

		return null;
	}

	@Override
	public Object persist(Object t) throws HBaseException{
		
		return save(t, null, null);
	}

	private Object save(Object t, String foreignKeyRowId, String foreignKeyColumnName) throws HBaseException {
		
		if (t == null)
			throw new IllegalArgumentException("Classname cannot be null");
		
		HTable table = null;
		final String rowKey = HBaseUtils.getKeyFieldValue(t);
		final String tableName = HBaseUtils.getTableName(t.getClass());
		Put put = new Put(Bytes.toBytes(rowKey));
		Field fields[] = t.getClass().getDeclaredFields();

		for (Field f : fields) {

			/**
			 * Ensure that you skip the RowIdentifer and the Relationships. 
			 * We are not supporting any bidirectional relationships here.
			 */
			if (f.isAnnotationPresent(HRowIdentifier.class) || f.isAnnotationPresent(OneToMany.class))
			{
				continue;
			}			
			String columnFamilyName = HBaseUtils.getColumnFamily(f);
			String columnName = HBaseUtils.getColumnName(f);			
			String columnValue;
			
			if (f.isAnnotationPresent(HJSONField.class))
			{
				f.setAccessible(true);				
				try {
					columnValue = JSONUtils.getJSONFromObject(f.get(t));
				} catch (IllegalArgumentException | IllegalAccessException | ApplicationException e) {
					throw new HBaseException(e);
				}
			}
			else
			{
				columnValue = ReflectionUtils.getFieldValue(t, f.getName());
			}
			if (columnValue == null)
			{
				columnValue = "";
			}
			put.add(Bytes.toBytes(columnFamilyName), Bytes.toBytes(columnName),
					Bytes.toBytes(columnValue));
		}

		try {
			table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), tableName);
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

		

		/**
		 * Handle Joins
		 */
		for (Field f : fields) {
			if (!f.isAnnotationPresent(OneToMany.class))
			{
				continue;
			}
			
			OneToMany otm = f.getAnnotation(OneToMany.class);
			
//			final String mappedBy[] = otm.mappedBy().split(":");
			
			ParameterizedType pt = (ParameterizedType)f.getGenericType();
			Type pt2 = pt.getActualTypeArguments()[0];
			
			final String className = pt2.toString().split("\\s")[1];
			Class<?> clazz;
			try {
				 clazz = Class.forName(className);
				 
				 /**
				  * Get Object's value. If the value is null, then we are not persisting data here. 
				  * Hence continue.
				  */
				 Object obj = ReflectionUtils.getFieldValue(t, f.getName());
				 Object val = null;
				 if (obj == null) {
					 continue;
				 }
				 				 
				 if (List.class.isAssignableFrom(f.getType()))
				 {
					 List col = (List) obj;					 
					 Iterator i = col.iterator();
					 while(i.hasNext())
					 {
						 Object o = i.next();
						 val = clazz.cast(o);
					 }
					 
				 }
				 
				 save(val, rowKey, tableName);
				 
			} catch (ClassNotFoundException e) {
				throw new HBaseException(e);
			}
						
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
