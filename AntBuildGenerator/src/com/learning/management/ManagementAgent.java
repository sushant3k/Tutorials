/**
 * This class is used to start and stop the MBeanServer.
 * 
 */
package com.learning.management;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * @author Sushant
 *
 */
public class ManagementAgent implements Runnable{

	MBeanServer mbs = null;
	IBuildGenerator ibg = null;
	ObjectName name; 
	@Override
	public void run() {
		try
		{
			 mbs  = ManagementFactory.getPlatformMBeanServer(); 
			 name = new ObjectName("com.learning.management:type=IBuildGenerator"); 
	        ibg = new IBuildGenerator();
	        mbs.registerMBean(ibg, name); 
	        
		}
		catch(MalformedObjectNameException mone)
		{
			System.out.println(mone.getMessage());
		}
		catch(NotCompliantMBeanException ncmbe)
		{
			System.out.println(ncmbe.getMessage());
		}
		catch(MBeanRegistrationException mbre)
		{
			System.out.println(mbre.getMessage());
		}
		catch(InstanceAlreadyExistsException iare)
		{
			System.out.println(iare.getMessage());
		}
		
	}

	/**
	 * Unregister MBean
	 */
	public void unregisterRegisteredMBeans()
	{
		if (mbs !=null && name != null)
		{
			try
			{
			
				mbs.unregisterMBean(name);
			}
			catch(Throwable t)
			{
				
			}
		}
	}
	
	/**
	 * Get MBean Interface
	 * @return {@link IBuildGenerator}
	 */
	public IBuildGenerator getMBeanInterface()
	{
		return ibg;
	}	
}
