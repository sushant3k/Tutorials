/**
 * 
 */
package com.etipl.labOrder.serviceImpl;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.hbase.spi.IHBaseService;
import com.etipl.labOrder.model.LabTest;
import com.etipl.labOrder.service.ILabTestService;

/**
 * @author Sushant
 *
 */
public class LabTestServiceImpl implements ILabTestService, BundleActivator {

	
	ServiceReference sr;
	IHBaseService<LabTest> service;
	
	/**
	 * FIXME: Remove this constructor
	 */
//	public LabTestServiceImpl()
//	{
//		service = new HBaseServiceImpl<LabTest>();
//	}
	@Override
	public List<LabTest> getAllTests() {
		List<LabTest> lst = service.getResultList(LabTest.class);
		return lst;
	}

	@Override
	public LabTest getTestById(String testId) {
		return service.getSingleResult(LabTest.class, testId);
		
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(BundleContext context) throws Exception {
		sr = context.getServiceReference(IHBaseService.class.getName());
		service = (IHBaseService) context.getService(sr);
//		LabTestServiceReference  lrServiceRef = new LabTestServiceReference(context);
//		lrServiceRef.initiateLabTest();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.ungetService(sr);
		
	}

}
