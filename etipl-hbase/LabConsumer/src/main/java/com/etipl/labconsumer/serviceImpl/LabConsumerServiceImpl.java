/**
 * 
 */
package com.etipl.labconsumer.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.labOrder.model.LabResult;
import com.etipl.labOrder.model.LabTest;
import com.etipl.labOrder.service.ILabResultService;
import com.etipl.labOrder.service.ILabTestService;
import com.etipl.labconsumer.service.ILabConsumer;

/**
 * @author Sushant
 *
 */
public class LabConsumerServiceImpl implements ILabConsumer, BundleActivator{
	
	ILabTestService ilts;
	ILabResultService ilrs;
	
	public void start(BundleContext context) throws Exception {
		ServiceReference sr = context.getServiceReference(ILabTestService.class.getName());
		ilts = (ILabTestService) context.getService(sr);
		
		sr = context.getServiceReference(ILabResultService.class.getName());
		ilrs = (ILabResultService) context.getService(sr);
		
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void initiateAllTests() {
		
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
