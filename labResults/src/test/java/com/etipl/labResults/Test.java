package com.etipl.labResults;

import java.util.Calendar;
import java.util.List;

import com.etipl.labResults.model.LabResult;
import com.etipl.labResults.model.LabTest;

public class Test {

	public static void main(String args[])
	{
		ILabResultService ilrs = new LabResultServiceImpl();
		LabResult lr = new LabResult();
		lr.setRowKey("RowKey^OrderNo^"+System.currentTimeMillis());
		lr.setAnalyteSequenceId("1");
		lr.setClinicalInfo("this is clinical Info");
		lr.setCollectionDateTime(Calendar.getInstance().getTime().toString());
		lr.setCreatedBy("TestCreator");
		lr.setCreatedOn("createdToday");
		lr.setFillerOrderNo("fillerOrderNo1");
		lr.setLabDirectorId("labDirectorId1");
		lr.setLabName("labName");
		lr.setLastModifiedBy("lastModifiedBy1");
		lr.setLastModifiedDate(Calendar.getInstance().getTime().toString());
		lr.setLmodifiedDate(Calendar.getInstance().getTime().toString());
		lr.setModifiedBy("modifiedBy1");
		lr.setObservationNotes("observationNotes1");
		lr.setOrderDateTime(Calendar.getInstance().getTime().toString());
		lr.setOrderHL7Msg("orderHL7Msg");
		ilrs.storeResults(lr);
		
		ILabTestService ilts = new LabTestServiceImpl();
		List<LabTest> lst = ilts.getAllTests();
		String rowKey = display(lst);
		
		displayTest(ilts.getTestById(rowKey));
	}
	private static void displayTest(LabTest lt)
	{
		System.out.println("name:="+lt.getTestName());
		System.out.println("rowKey:="+lt.getRowKey());
		System.out.println("analytes:"+lt.getAnalyte());
	}
	private static String display(List<LabTest> lst)
	{
		if (lst == null || lst.isEmpty())
		{
			System.err.println("No data found in all tests");
			return null;
		}
		String rowKey = null;
		for (LabTest lt : lst)
		{
			System.out.println("name:="+lt.getTestName());
			System.out.println("rowKey:="+lt.getRowKey());
			rowKey = lt.getRowKey();
		}
		return rowKey;
	}
}
