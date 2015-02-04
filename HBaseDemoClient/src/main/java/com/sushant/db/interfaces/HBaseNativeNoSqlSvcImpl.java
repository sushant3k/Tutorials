package com.sushant.db.interfaces;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.UUID;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import com.sushant.LabResults;
import com.sushant.PatientPersonalDetails;
import com.sushant.annotations.HBaseColumnQualifier;
import com.sushant.common.ApplicationConstants;
import com.sushant.exception.ApplicationException;
import com.sushant.mapReduce.jobs.PatientByCustomerJob;
import com.sushant.mapReduce.jobs.PatientByCustomerJob.PatientToCustomerMapper;
import com.sushant.mapReduce.jobs.PatientByCustomerJob.PatientToCustomerReducer;

/**
 * 
 * @author Sushant
 *
 */
public class HBaseNativeNoSqlSvcImpl extends NoSqlSvc {

	@Override
	public List<PatientPersonalDetails> fetchAllPersonalRecord() throws ApplicationException {
		HTable table = null;
		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL));
		ResultScanner rs = null;
		List<PatientPersonalDetails> lpd = null;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
			rs = table.getScanner(scan);
			if (rs != null) {
				lpd = new ArrayList<PatientPersonalDetails>();

				for (Result rr : rs) {

					PatientPersonalDetails ppd = new PatientPersonalDetails();

					ppd.setFirstName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("firstName")))));
					ppd.setLastName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("lastName")))));
					lpd.add(ppd);
				}
				displayPatientsPersonalDetails(lpd);
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

	private void closeHTableConnection(HTable table) {
		if (table != null) {
			try {
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void insertPersonalRecord(final String rowKey, PatientPersonalDetails ppd) throws ApplicationException {
		HTable table;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
		} catch (IOException e1) {
			throw new ApplicationException(e1);
		}
		Put put = new Put(Bytes.toBytes(rowKey));

		Field fields[] = ppd.getClass().getDeclaredFields();

		for (Field f : fields) {

			if (f.isAnnotationPresent(HBaseColumnQualifier.class)) {
				HBaseColumnQualifier hbcq = f.getAnnotation(HBaseColumnQualifier.class);
				String columnName = hbcq.columnName();
				if (columnName.isEmpty()) {
					columnName = f.getName();
				}
			}
			String name = f.getName();
			String value = "";
			try {
				Method m = ppd.getClass().getMethod(
						"get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()),
						new Class<?>[] {});
				try {
					Object obj = m.invoke(ppd, new Object[] {});
					if (obj != null) {
						value = obj.toString();
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException nsme) {
				nsme.printStackTrace();
			}

			put.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL), Bytes.toBytes(name),
					Bytes.toBytes(value));
		}

		try {
			table.put(put);
			table.flushCommits();
		} catch (RetriesExhaustedWithDetailsException e) {
			throw new ApplicationException(e);
		} catch (InterruptedIOException e) {
			throw new ApplicationException(e);
		} finally {
			closeHTableConnection(table);
		}

	}

	public List<PatientPersonalDetails> fetchPersonalRecord(final String rowKey) throws ApplicationException {
		/**
		 * Note here, that we are not using the GET API. Get API is a trivial
		 * API that can be used similar to the Scan API. For the demonstration
		 * purpose, only Scan is being shown here.
		 */
		Scan scan = new Scan(Bytes.toBytes(rowKey));

		/**
		 * Get complete Column Family
		 */
		scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL));

		HTable table = null;
		ResultScanner rs = null;

		List<PatientPersonalDetails> lpd = null;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
			rs = table.getScanner(scan);
			if (rs != null) {
				lpd = new ArrayList<PatientPersonalDetails>();
				for (Result rr : rs) {

					PatientPersonalDetails ppd = new PatientPersonalDetails();

					ppd.setFirstName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("firstName")))));
					ppd.setLastName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("lastName")))));
					lpd.add(ppd);
				}
				rs.close();
				closeHTableConnection(table);
				displayPatientsPersonalDetails(lpd);
				return lpd;
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			rs.close();
			closeHTableConnection(table);

		}

		return lpd;
	}

	public void deletePersonalRecord(final String rowKey) throws ApplicationException {
		HTable table;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}

		/**
		 * For deleting the entire row, just instantiate the Delete class with
		 * the rowKey as the unique Identifier.
		 */
		Delete delete = new Delete(Bytes.toBytes(rowKey));

		try {
			table.delete(delete);
		} catch (IOException e) {
			throw new ApplicationException(e);
		} finally {
			closeHTableConnection(table);
		}
	}

	@Override
	public void generateBulkData() throws ApplicationException {

		HTable table;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}

		List<Put> l = new ArrayList<Put>(10000);
		// Perform insertion for 3 Customers
		for (int customerId = 1; customerId < 4; customerId++) {

			for (int counter = 0; counter < 10001; counter++) {
				String rowKey = UUID.randomUUID().toString() + "#customer" + customerId;
				Put p = new Put(Bytes.toBytes(rowKey));
				// Add Unique First name
				p.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL), Bytes.toBytes("firstName"),
						Bytes.toBytes("FirstName:" + counter));
				p.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL), Bytes.toBytes("lastName"),
						Bytes.toBytes("LastName:" + counter));

				l.add(p);
			}

			// Let CPU breathe
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			table.put(l);
			table.flushCommits();
		} catch (RetriesExhaustedWithDetailsException e) {
			throw new ApplicationException(e);
		} catch (InterruptedIOException e) {
			throw new ApplicationException(e);
		} finally {
			closeHTableConnection(table);
		}
	}

	public List<PatientPersonalDetails> getAllPatientswithARegexFilteronFirstName(final String firstNameFilter)
			throws ApplicationException {
		RegexStringComparator comp = new RegexStringComparator(firstNameFilter);

		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("personal"),
				Bytes.toBytes("firstName"), CompareOp.EQUAL, comp);

		HTable table = null;
		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL));
		scan.setFilter(filter);
		ResultScanner rs = null;
		List<PatientPersonalDetails> lpd = null;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
			rs = table.getScanner(scan);
			if (rs != null) {
				lpd = new ArrayList<PatientPersonalDetails>();

				for (Result rr : rs) {

					PatientPersonalDetails ppd = new PatientPersonalDetails();
					ppd.setFirstName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("firstName")))));
					ppd.setLastName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
							Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL),
							Bytes.toBytes("lastName")))));
					lpd.add(ppd);
				}
				displayPatientsPersonalDetails(lpd);
				return lpd;
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			rs.close();
			closeHTableConnection(table);

		}

		return lpd;

	}

	public void runMapReduce() throws ApplicationException {
		try {

			Job job = Job.getInstance(getHBaseConfiguration(), "PatientCollectionByCustomer");

			job.setJarByClass(PatientByCustomerJob.class);

			Scan scan = new Scan();
			scan.setCaching(500);

			scan.setCacheBlocks(false); // Don't set for MapReduce Jobs

			TableMapReduceUtil.initTableMapperJob(ApplicationConstants.DatabaseConstants.TABLE_NAME, // input
																										// HBase
																										// table
																										// name
					scan, // Scan instance to control CF and attribute selection
					PatientToCustomerMapper.class, // mapper
					null, // mapper output key
					null, // mapper output value
					job);
			job.setOutputFormatClass(NullOutputFormat.class); // because we
																// aren't
																// emitting
																// anything from
																// mapper

			job.setReducerClass(PatientToCustomerReducer.class);
			job.setNumReduceTasks(1);

			// FileOutputFormat.setOutputPath(job, new
			// Path("/tmp/mr/mySummaryFile"));

			boolean b = job.waitForCompletion(true);
			if (!b) {
				throw new IOException("error with job!");
			}

		} catch (IOException e) {
			throw new ApplicationException(e);
		} catch (ClassNotFoundException e) {
			throw new ApplicationException(e);
		} catch (InterruptedException e) {
			throw new ApplicationException(e);
		}
	}

	public void insertLabResultsForPatient(final String rowKey, PatientPersonalDetails ppd, List<LabResults> labResults)
			throws ApplicationException {

		if (ppd == null || rowKey == null || rowKey.isEmpty())
			return;

		List<String> lRowKeys = stroreLabResults(labResults);
		HTable table;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
		} catch (IOException e1) {
			throw new ApplicationException(e1);
		}

		Put put = new Put(Bytes.toBytes(rowKey));

		Field fields[] = ppd.getClass().getDeclaredFields();

		for (Field f : fields) {

			if (f.isAnnotationPresent(HBaseColumnQualifier.class)) {
				HBaseColumnQualifier hbcq = f.getAnnotation(HBaseColumnQualifier.class);
				String columnName = hbcq.columnName();
				if (columnName.isEmpty()) {
					columnName = f.getName();
				}
			}
			String name = f.getName();
			String value = "";
			try {
				Method m = ppd.getClass().getMethod(
						"get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()),
						new Class<?>[] {});
				try {
					Object obj = m.invoke(ppd, new Object[] {});
					if (obj != null) {
						value = obj.toString();
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException nsme) {
				nsme.printStackTrace();
			}

			put.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_PERSONAL), Bytes.toBytes(name),
					Bytes.toBytes(value));

		}

		if (lRowKeys != null && !lRowKeys.isEmpty()) {
			int count = 0;
			for (String rk : lRowKeys) {
				put.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_LABRESULTS), Bytes.toBytes(++count),
						Bytes.toBytes(rk));
			}

			put.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_LABRESULTS), Bytes.toBytes("resultsCount"),
					Bytes.toBytes(count));
		}

		try {
			table.put(put);
			table.flushCommits();
		} catch (RetriesExhaustedWithDetailsException e) {
			throw new ApplicationException(e);
		} catch (InterruptedIOException e) {
			throw new ApplicationException(e);
		} finally {
			closeHTableConnection(table);
		}
	}

	private List<String> stroreLabResults(List<LabResults> lab) throws ApplicationException {
		if (lab == null || lab.isEmpty())
			return null;

		HTable table;
		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.LABRESULTS_TABLE_NAME);
		} catch (IOException e1) {
			throw new ApplicationException(e1);
		}

		List<String> lst = new ArrayList<String>(lab.size());
		List<Put> lPut = new ArrayList<Put>(lab.size());
		for (LabResults lr : lab) {
			Field fields[] = lr.getClass().getDeclaredFields();
			String rowKey = java.util.UUID.randomUUID().toString();
			Put put = new Put(Bytes.toBytes(rowKey));
			lst.add(rowKey);

			for (Field f : fields) {

				if (f.isAnnotationPresent(HBaseColumnQualifier.class)) {
					HBaseColumnQualifier hbcq = f.getAnnotation(HBaseColumnQualifier.class);
					String columnName = hbcq.columnName();
					if (columnName.isEmpty()) {
						columnName = f.getName();
					}
				}
				String name = f.getName();
				String value = "";
				try {
					Method m = lr.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()),
							new Class<?>[] {});
					try {
						Object obj = m.invoke(lr, new Object[] {});
						if (obj != null) {
							value = obj.toString();
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException nsme) {
					nsme.printStackTrace();
				}

				put.add(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_RESULTS), Bytes.toBytes(name),
						Bytes.toBytes(value));
				lPut.add(put);
			}
		}

		try {
			table.put(lPut);
			table.flushCommits();
			return lst;
		} catch (RetriesExhaustedWithDetailsException e) {
			throw new ApplicationException(e);
		} catch (InterruptedIOException e) {
			throw new ApplicationException(e);
		} finally {
			closeHTableConnection(table);

		}

	}

	@Override
	public void displayLabResultsForPaient(String rowKey) throws ApplicationException {

		/**
		 * Note here, that we are not using the GET API. Get API is a trivial
		 * API that can be used similar to the Scan API. For the demonstration
		 * purpose, only Scan is being shown here.
		 */
		Scan scan = new Scan(Bytes.toBytes(rowKey));

		/**
		 * Get complete Column Family
		 */
		scan.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_LABRESULTS));

		HTable table = null;
		ResultScanner rs = null;

		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.TABLE_NAME);
			rs = table.getScanner(scan);
			if (rs != null) {
				List<String> keys = getAllLabResultKeys(rs.iterator().next());
				rs.close();
				closeHTableConnection(table);
				getTestResults(keys);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			rs.close();
			closeHTableConnection(table);

		}

	}

	@SuppressWarnings("resource")
	private List<LabResults> getTestResults(List<String> keys) throws ApplicationException {
		HTable table = null;

		try {
			table = new HTable(getHBaseConfiguration(), ApplicationConstants.DatabaseConstants.LABRESULTS_TABLE_NAME);
		} catch (IOException e1) {
			throw new ApplicationException(e1);
		}

		List<LabResults> lpd = new ArrayList<LabResults>(keys.size());

		for (String key : keys) {

			Get get = new Get(Bytes.toBytes(key));
			get.addFamily(Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_RESULTS));

			Result rr;
			try {
				rr = table.get(get);
				LabResults lr = new LabResults();
				lr.setTestName(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
						Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_RESULTS), Bytes.toBytes("testName")))));
				lr.setResult(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
						Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_RESULTS), Bytes.toBytes("result")))));
				System.out.println(lr);
				lpd.add(lr);
			} catch (IOException e) {
				closeHTableConnection(table);
				throw new ApplicationException(e);
			}

		}
		closeHTableConnection(table);
		return null;
	}

	private List<String> getAllLabResultKeys(Result rr) {
		NavigableMap<byte[], byte[]> familyMap = rr.getFamilyMap(Bytes
				.toBytes(ApplicationConstants.DatabaseConstants.CF_LABRESULTS));
		Set<byte[]> set = familyMap.keySet();

		List<String> l = new ArrayList<String>(set.size());

		for (byte[] qualifier : set) {
			String s = Bytes.toString(qualifier);
			if (s != null && !s.equalsIgnoreCase("resultsCount")) {
				l.add(Bytes.toString(CellUtil.cloneValue(rr.getColumnLatestCell(
						Bytes.toBytes(ApplicationConstants.DatabaseConstants.CF_LABRESULTS), qualifier))));
			}
		}
		return l;
	}

	@Override
	public void createAnyTable(String tableName, Set<String> columnFamilies) throws ApplicationException {
		HBaseAdmin admin = null;

		try {

			admin = new HBaseAdmin(getHBaseConfiguration());

			if (admin.tableExists(tableName)) {
				System.out.println("Table: " + tableName + " already exists");
				admin.close();
				return;
			}
			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
			for (String s : columnFamilies) {
				tableDescriptor.addFamily(new HColumnDescriptor(s));
			}
			admin.createTable(tableDescriptor);
		} catch (IOException e1) {
			throw new ApplicationException(e1);
		} finally {

			/**
			 * Ensure that HBaseAdmin is closed, otherwise a resource leak is
			 * detected.
			 */
			if (admin != null) {
				try {
					admin.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
