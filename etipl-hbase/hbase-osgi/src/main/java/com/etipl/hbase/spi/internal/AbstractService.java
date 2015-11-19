/**
 * 
 */
package com.etipl.hbase.spi.internal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.jboss.netty.handler.codec.oneone.OneToOneStrictEncoder;

import com.etipl.hbase.annotations.HJSONField;
import com.etipl.hbase.annotations.HRowIdentifier;
import com.etipl.hbase.connection.HConfiguration;

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
	
	
	
	protected Object  getNewObject(Class<?> t, Result rr) 
	{
		Object t1 = null;
		try {
			 t1 = t.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Field fs [] = t.getDeclaredFields();
		
		final String rowKey = Bytes.toString(rr.getRow());
		for (Field f : fs)
		{
			final String fieldName = f.getName();
			String val = null;
			
			if (f.isAnnotationPresent(HRowIdentifier.class))
			{
				// IT IS rowKey
				val = Bytes.toString(rr.getRow());
			}
			if(!f.isAnnotationPresent(HRowIdentifier.class))
			{
				final String columnName = HBaseUtils.getColumnName(f);			
				final String columnFamily = HBaseUtils.getColumnFamily(f);
				
				if (!f.isAnnotationPresent(OneToMany.class))
				{
					val = Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
						Bytes.toBytes(columnFamily),
						Bytes.toBytes(columnName))));
					
					if (f.isAnnotationPresent(HJSONField.class))
					{
						Object obj;
						try {
							obj = JSONUtils.generateObjectFromJSON(f.getType(), val);
							ReflectionUtils.setFieldValue(t, fieldName, obj);
						} catch (Exception e) {
							System.err.println("Could not set the Field Value");
						}
						
					}
					else
					{
						ReflectionUtils.setFieldValue(t1, fieldName, val);
					}
				}
				else
				{
					List<?> l = handleOneToManyJoin(f, rowKey);
					ReflectionUtils.setFieldValueForCollections(t1, fieldName, l);
				}
			}
				
			
		}
		return t1;
		
	}
	
	private List<?> handleOneToManyJoin(final Field f, final String rowKey)
	{
		if (f.isAnnotationPresent(OneToMany.class))
		{
			OneToMany otm = f.getAnnotation(OneToMany.class);
			if (otm.fetch() == null || otm.fetch() == FetchType.LAZY)
			{
				return null;
			}
			
			String c[] = otm.mappedBy().split(":");
			String columnFamily = c[0];
			String columnName=c[1]; 
			String tableName = null;
			
			/**
			 * We don't need the JoinTable annotation 
			 * 
			 * TODO: Fix me, don't need the JoinTable annotation
			 */
			if (f.isAnnotationPresent(JoinTable.class))
			{
				JoinTable jt = f.getAnnotation(JoinTable.class);
				tableName =jt.name();
			}
			else
			{
				//TODO: What if the JoinTable is not present.
				tableName ="test"; // TODO: THis should be replaced by the classname
			}
			
			HTable table = null;
			Scan scan = new Scan();
			
			
			ResultScanner rs = null;
			List<Object> lpd = null;
			try {
				table = new HTable(HConfiguration.getConfiguration().getHBaseConfiguration(), tableName);
				SingleColumnValueFilter scvf = new SingleColumnValueFilter(Bytes.toBytes(columnFamily), 
						Bytes.toBytes(columnName), CompareOp.EQUAL,Bytes.toBytes(rowKey));
				scan.setFilter(scvf);
				rs = table.getScanner(scan);
				
				if (rs != null) {
					lpd = new ArrayList<Object>();

					for (Result rr : rs) {
						
						ParameterizedType pt = (ParameterizedType)f.getGenericType();
						Type pt2 = pt.getActualTypeArguments()[0];
						Object o = getNewObject(Class.forName(pt2.toString()),rr);
						
						lpd.add(o);
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
		else
		{
			return null;
		}
	}
}
