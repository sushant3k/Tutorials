/**
 * 
 */
package com.etipl.labOrder.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.etipl.hbase.annotations.HColumnQualifier;
import com.etipl.hbase.annotations.HJSONField;
import com.etipl.hbase.annotations.HRowIdentifier;
import com.etipl.hbase.annotations.HRowIdentifierGenerator;
import com.etipl.hbase.annotations.HTableQualifier;

/**
 * @author Sushant
 *
 */
@HTableQualifier(tableName="labTest")
public class LabTest {

	@HRowIdentifier(identifier=HRowIdentifierGenerator.AUTO)
	private String rowKey;
	@HColumnQualifier(columnFamily="testDetails",columnName="testName")
	private String testName;
	
	@HColumnQualifier(columnFamily="testDetails",columnName="analyte")
	@HJSONField
	@OneToMany(fetch=FetchType.EAGER,mappedBy="cf:labTest_Id")
	@JoinTable(name="ANALYTE")
	private List<Analyte> analyte;
	

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public List<Analyte> getAnalyte() {
		return analyte;
	}

	public void setAnalyte(List<Analyte> analyte) {
		this.analyte = analyte;
	}

	
	public static class Analyte
	{
		private String sequenceNo;
		private String analyteName;
		
		private String analyteCode;
		
		public Analyte()
		{
			
		}

		public String getSequenceNo() {
			return sequenceNo;
		}

		public void setSequenceNo(String sequenceNo) {
			this.sequenceNo = sequenceNo;
		}

		public String getAnalyteName() {
			return analyteName;
		}

		public void setAnalyteName(String analyteName) {
			this.analyteName = analyteName;
		}

		public String getAnalyteCode() {
			return analyteCode;
		}

		public void setAnalyteCode(String analyteCode) {
			this.analyteCode = analyteCode;
		}
		
		
	}
	
}
