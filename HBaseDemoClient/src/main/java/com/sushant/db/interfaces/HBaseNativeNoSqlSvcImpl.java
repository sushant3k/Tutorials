package com.sushant.db.interfaces;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.sushant.PatientPersonalDetails;
import com.sushant.annotations.HBaseColumnQualifier;
import com.sushant.exception.ApplicationException;

/**
 * 
 * @author Sushant
 *
 */
public class HBaseNativeNoSqlSvcImpl extends NoSqlSvc {

	private static final String TABLE_NAME = "Patient";
	
	private static final String CF_PERSONAL = "personal";
	
	private static final String CF_LABRESULTS ="labResults";

	@Override
	public void createTable( ) throws ApplicationException
    {
		HBaseAdmin admin = null;
    	
	    	try {
	    		
	    		admin = new HBaseAdmin(getHBaseConfiguration());
		    	
		    	if (admin.tableExists(TABLE_NAME))
		    	{
		    		System.out.println("Table: " +TABLE_NAME +" alread exists");
		    		admin.close();
		    		return;
		    	}
		    	HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
		    	tableDescriptor.addFamily(new HColumnDescriptor(CF_PERSONAL));
		    	tableDescriptor.addFamily(new HColumnDescriptor(CF_LABRESULTS));
				admin.createTable(tableDescriptor);
			} catch (IOException e1) {
				throw new ApplicationException(e1);
			}
	    	finally {
		    	
		    	/**
		    	 * Ensure that HBaseAdmin is closed, otherwise a resource leak is detected.
		    	 */
		    	if (admin !=null)
	    		{
	    			try {
						admin.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}
	    	
    }

	@Override
	public List<PatientPersonalDetails> fetchAllPersonalRecord() throws ApplicationException {
		HTable table = null;
    	Scan scan = new Scan();
    	scan.addFamily(Bytes.toBytes(CF_PERSONAL));
    	ResultScanner rs = null;
    	List<PatientPersonalDetails> lpd = null;
    	try
    	{
	    	table = new HTable(getHBaseConfiguration(),TABLE_NAME);
	    	rs = table.getScanner(scan);
	    	if (rs != null)
	    	{
	    		lpd = new ArrayList<PatientPersonalDetails>();
	    		
	    		for (Result rr : rs)
	    		{
	    		
		    		PatientPersonalDetails ppd = new PatientPersonalDetails();
		    		
		    		ppd.setFirstName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(Bytes.toBytes(CF_PERSONAL), Bytes.toBytes("firstName")))));				    	
		    		ppd.setLastName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(Bytes.toBytes(CF_PERSONAL), Bytes.toBytes("lastName")))));
		    		lpd.add(ppd);
	    		}
				
	    		return lpd;
	    	}
	    	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		closeHTableConnection(table);
    		rs.close();
    	}
    	displayPatientsPersonalDetails(lpd);
    	return lpd;
	}

	private void closeHTableConnection(HTable table)
    {
    	if (table != null)
		{
			try {
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	
	 public void insertPersonalRecord(final String rowKey,  PatientPersonalDetails ppd) throws ApplicationException 
	    {
	    	HTable table;
			try {
				table = new HTable(getHBaseConfiguration(),TABLE_NAME);
			} catch (IOException e1) {
				throw new ApplicationException (e1);
			}
	    	Put put = new Put(Bytes.toBytes(rowKey));
	    	
	    	Field fields []  = ppd.getClass().getDeclaredFields();
	    	
	    	for (Field f : fields)
	    	{
	    		
	    		if ( f.isAnnotationPresent(HBaseColumnQualifier.class) )
	    		{
	    			HBaseColumnQualifier hbcq = f.getAnnotation(HBaseColumnQualifier.class);
	    			String columnName = hbcq.columnName();
	    			if (columnName.isEmpty())
	    			{
	    				columnName = f.getName();
	    			}
	    		}
	    		String name = f.getName();
	    		String value = "";
	    		try
	    		{
	    			Method m = ppd.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length())  , new Class<?>[]{});
	    			try {
						Object obj = m.invoke(ppd, new Object[]{});
						if (obj != null)
						{
							value = obj.toString();
						}
					} catch (IllegalAccessException e) {					
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
	    		}
	    		catch(NoSuchMethodException nsme)
	    		{
	    			nsme.printStackTrace();
	    		}
	    		
	    		put.add(Bytes.toBytes(CF_PERSONAL),Bytes.toBytes(name),Bytes.toBytes(value));    		
	    	}
	    	
	    	try {
				table.put(put);
				table.flushCommits();		    	
			} catch (RetriesExhaustedWithDetailsException e) {
				throw new ApplicationException(e);
			} catch (InterruptedIOException e) {
				throw new ApplicationException(e);
			}
	    	finally
	    	{
	    		closeHTableConnection(table);
	    	}
	    		
	    }

	 public PatientPersonalDetails fetchPersonalRecord(final String rowKey) throws ApplicationException 
	    {
	    	/**
	    	 * Note here, that we are not using the GET API. 
	    	 * Get API is a trivial API that can be used similar to the Scan API. 
	    	 * For the demonstration purpose, only Scan is being shown here. 
	    	 */
	    	Scan scan = new Scan(Bytes.toBytes(rowKey));
	    	
	    	/**
	    	 * Get complete Column Family
	    	 */
	    	scan.addFamily(Bytes.toBytes(CF_PERSONAL));
	    	
	    	HTable  table = null;
	    	ResultScanner rs = null;
	    	try
	    	{
		    	table = new HTable(getHBaseConfiguration(),TABLE_NAME);
		    	rs = table.getScanner(scan);
		    	if (rs != null)
		    	{
		    		Result rr = rs.iterator().next();
		    		
		    		PatientPersonalDetails ppd = new PatientPersonalDetails();
		    		
		    		ppd.setFirstName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(Bytes.toBytes(CF_PERSONAL), Bytes.toBytes("firstName")))));				    	
		    		ppd.setLastName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(Bytes.toBytes(CF_PERSONAL), Bytes.toBytes("lastName"))))); 
					
		    		return ppd;
		    	}
		    	
	    	}
	    	catch(Exception e)
	    	{
	    		throw new ApplicationException(e);
	    	}
	    	finally
	    	{
	    		closeHTableConnection(table);
	    		rs.close();
	    	}
	    	
	    	return null;
	    }

	 public void deletePersonalRecord(final String rowKey) throws ApplicationException
	 {
	    	HTable table;
			try {
				table = new HTable(getHBaseConfiguration(), TABLE_NAME);
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
	    	
	    	/**
	    	 * For deleting the entire row, just instantiate the Delete class with the rowKey as the unique Identifier.
	    	 */
	    	Delete delete = new Delete(Bytes.toBytes(rowKey));
	    	
	    	try {
				table.delete(delete);
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
	    	finally {
	    		closeHTableConnection(table);
	    	}
	  }
}
