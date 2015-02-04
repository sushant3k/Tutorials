/**
 * 
 */
package com.sushant;

import com.sushant.annotations.HBaseColumnQualifier;

/**
 * @author Sushant
 *
 */
public class LabResults {

	@HBaseColumnQualifier(columnName="testName")
	private String testName;
	
	@HBaseColumnQualifier(columnName="result")
	private String result;
	
	public LabResults()
	{
		
	}

	public LabResults(String testName, String result)
	{
		this.testName = testName;
		this.result = result;
		 
	}
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString()
	{
		return "Test name:"+testName +", result="+result;
	}
}
