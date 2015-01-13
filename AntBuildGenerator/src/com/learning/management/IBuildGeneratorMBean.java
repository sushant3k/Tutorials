/**
 * This is the management interface for the Build Generator Utility.
 * This interface exposes a JMX interface to figure out:
 * (i) Total number of exceptions occured while generating build in one complete session of Eclipse.
 * (ii) Exception Reasons
 * (iii) Successful build generation count in one complete session of Eclipse.
 * One complete session of Eclipse aggregates all the exceptions and successes of build generation.
 */
package com.learning.management;

import java.util.Vector;

/**
 * @author Sushant
 * @version 1.0
 * @since 1.0
 *
 */
public interface IBuildGeneratorMBean {

	public int getApplicationExceptionsCount();
	
	public Vector<String> getApplicationExceptionsContent();
	
	public void clearApplicationExceptionsCount();
	
	public int getSuccessfulProjectCount();
	
}
