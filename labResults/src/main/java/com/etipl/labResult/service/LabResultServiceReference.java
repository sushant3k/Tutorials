/**
 * 
 */
package com.etipl.labResult.service;

import java.util.Calendar;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.labOrder.service.ILabResultService;
import com.etipl.labResults.model.LabResult;

/**
 * @author Sushant
 *
 */
public class LabResultServiceReference {

	ServiceReference reference ;
	BundleContext context;
	public LabResultServiceReference(BundleContext context)
	{
		
		this.context = context;
		reference = context.getServiceReference(ILabResultService.class.getName());		
		
		
	}
	public void initiateLabResult()
	{

		reference = context.getServiceReference(ILabResultService.class.getName());
		ILabResultService i = (ILabResultService)context.getService(reference);		
		
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
		i.storeResults(null);
		context.ungetService(reference);
	}
}
