/**
 * 
 */
package com.learning.management;

import java.util.Vector;

/**
 * @author Sushant
 *
 */

public class IBuildGenerator implements IBuildGeneratorMBean{

	private volatile int applicationExceptionsCounter;
	private volatile int successfulCounter;
	private Vector<String> applicationExceptionContent = new Vector<String>();
	
	public void setApplicationException(final String applicationException)
	{
		applicationExceptionContent.add(applicationException);
	}
	@Override
	public int getApplicationExceptionsCount() {
		return applicationExceptionsCounter;
	}

	@Override
	public Vector<String> getApplicationExceptionsContent() {
		return applicationExceptionContent;
	}

	@Override
	public void clearApplicationExceptionsCount() {
		applicationExceptionsCounter = 0;
		
	}
	
	public void incrementApplicationExceptionsCounter()
	{
		applicationExceptionsCounter++;
	}
	@Override
	public int getSuccessfulProjectCount() {
		return successfulCounter;
	}
	
	public void incrementSuccessFulProjectCount()
	{
		successfulCounter++;
	}

}
