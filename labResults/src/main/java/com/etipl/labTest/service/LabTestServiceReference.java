/**
 * 
 */
package com.etipl.labTest.service;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.labOrder.service.ILabTestService;
import com.etipl.labResults.model.LabTest;

/**
 * @author Sushant
 *
 */
public class LabTestServiceReference {

	ServiceReference reference ;
	BundleContext context;
	public LabTestServiceReference(BundleContext context)
	{
		
		this.context = context;
		reference = context.getServiceReference(ILabTestService.class.getName());		
		
		
	}
	
	public void initiateLabTest()
	{
		ILabTestService ils = (ILabTestService)context.getService(reference);
		List<LabTest> lst = ils.getAllTests();
		String rowKey = display(lst);
		
		displayTest(ils.getTestById(rowKey));
		context.ungetService(reference);		
	}

	private void displayTest(LabTest lt)
	{
		System.out.println("name:="+lt.getTestName());
		System.out.println("rowKey:="+lt.getRowKey());
		System.out.println("analytes:"+lt.getAnalyte());
	}
	private String display(List<LabTest> lst)
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
