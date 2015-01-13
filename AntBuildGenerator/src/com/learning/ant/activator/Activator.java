package com.learning.ant.activator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.learning.management.ManagementAgent;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	

	// The shared instance
	private static Activator plugin;
	
	private static ManagementAgent ma ;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	public static ManagementAgent getManagementAgent()
	{
		return ma;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		ma = new ManagementAgent();
		Thread t = new Thread(ma);
		t.start();
//		t.join(); // Let the Management server start first.		
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		ma.unregisterRegisteredMBeans();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
