package com.etipl.labconsumer.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.etipl.labconsumer.service.ILabConsumer;
import com.etipl.labconsumer.serviceImpl.LabConsumerServiceImpl;

/**
 * Activator
 *
 */
public class ApplicationActivator implements BundleActivator
{
	ServiceReference sr;
	public void start(BundleContext context) throws Exception {
		sr = context.registerService(ILabConsumer.class.getName(), new LabConsumerServiceImpl(), null).getReference();
		
	}

	public void stop(BundleContext context) throws Exception {
		context.ungetService(sr);
		
	}
    
}
