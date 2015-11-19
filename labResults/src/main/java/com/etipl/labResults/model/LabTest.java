/**
 * 
 */
package com.etipl.labResults.model;

import com.etipl.labResults.nosql.framework.annotations.HColumnQualifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifierGenerator;
import com.etipl.labResults.nosql.framework.annotations.HTableQualifier;

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
	private String analyte;

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

	public String getAnalyte() {
		return analyte;
	}

	public void setAnalyte(String analyte) {
		this.analyte = analyte;
	}

	
	
}
