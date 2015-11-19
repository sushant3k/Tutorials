/**
 * 
 */
package com.etipl.labOrder.serviceImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.hbase.exception.HBaseException;
import com.etipl.hbase.spi.IHBaseService;
import com.etipl.labOrder.model.LabResult;
import com.etipl.labOrder.service.ILabResultService;

/**
 * @author Sushant
 *
 */
public class LabResultServiceImpl implements ILabResultService, BundleActivator{

	ServiceReference sr;
	IHBaseService<LabResult> service;
	@Override
	public void storeResults(LabResult lr) {
		try {
			// FIXME: Remove Service reference from here. 
			//service = new HBaseServiceImpl<LabResult>();
			// FIXME: Above line needs removal
			service.persist(lr);
		} catch (HBaseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		sr = context.getServiceReference(IHBaseService.class.getName());
		service = (IHBaseService<LabResult>) context.getService(sr);
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.ungetService(sr);
		
	}

}
