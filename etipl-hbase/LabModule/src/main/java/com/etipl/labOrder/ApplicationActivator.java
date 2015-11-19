/**
 * 
 */
package com.etipl.labOrder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.etipl.labOrder.service.ILabResultService;
import com.etipl.labOrder.service.ILabTestService;
import com.etipl.labOrder.serviceImpl.LabResultServiceImpl;
import com.etipl.labOrder.serviceImpl.LabTestServiceImpl;

/**
 * @author Sushant
 *
 */
public class ApplicationActivator implements BundleActivator{

	@Override
	public void start(BundleContext context) throws Exception {
		
		context.registerService(ILabResultService.class.getName(), new LabResultServiceImpl(), null);
		context.registerService(ILabTestService.class.getName(), new LabTestServiceImpl(), null);
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
