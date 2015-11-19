/**
 * 
 */
package com.etipl.hbase.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.etipl.hbase.spi.IHBaseService;
import com.etipl.hbase.spi.internal.HBaseServiceImpl;

/**
 * @author Sushant
 *
 */
public class HBaseActivator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		
		System.out.println("START com.etipl.hbase.activator");
		
		System.out.println("REGISTER com.etipl.hbase");
		context.registerService(IHBaseService.class.getName(), new HBaseServiceImpl<>(), null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		
	}

}
