/**
 * 
 */
package com.etipl.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.etipl.labOrder.service.ILabResultService;
import com.etipl.labOrder.service.ILabTestService;
import com.etipl.labOrder.service.LabResultServiceImpl;
import com.etipl.labOrder.service.LabTestServiceImpl;
import com.etipl.labResults.nosql.framework.nosql.spi.HBaseServiceImpl;
import com.etipl.labResults.nosql.framework.nosql.spi.IHBaseService;
/**
 * @author Sushant
 *
 */
public class ApplicationActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Bundle Started");
		
		context.registerService(IHBaseService.class.getName(), new HBaseServiceImpl<>(), null);
		context.registerService(ILabTestService.class.getName(), new LabTestServiceImpl(), null);		
		context.registerService(ILabResultService.class.getName(), new LabResultServiceImpl(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		
	}

}
