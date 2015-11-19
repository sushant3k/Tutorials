/**
 * 
 */
package com.etipl.labResults.nosql.framework.nosql.spi;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.etipl.labResults.nosql.framework.annotations.HRowIdentifier;

/**
 * @author Sushant
 *
 */
public abstract class AbstractService <T>{

	protected void closeHTableConnection(HTable table) {
		if (table != null) {
			try {
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected T  getNewObject(Class<T> t, Result rr) 
	{
		T t1 = null;
		try {
			 t1 = t.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Field fs [] = t.getDeclaredFields();
		for (Field f : fs)
		{
			final String fieldName = f.getName();
			String val;
			
			if (f.isAnnotationPresent(HRowIdentifier.class))
			{
				// IT IS rowKey
				val = Bytes.toString(rr.getRow());
			}
			else
			{
				final String columnName = HBaseUtils.getColumnName(f);			
				final String columnFamily = HBaseUtils.getColumnFamily(f);
				
				
				val = Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
						Bytes.toBytes(columnFamily),
						Bytes.toBytes(columnName))));
			}
			
			// TODO: Handle rowKey
			ReflectionUtils.setFieldValue(t1, fieldName, val);
		}
		return t1;
		
	}
}
