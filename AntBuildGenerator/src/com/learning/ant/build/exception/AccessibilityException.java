
/**
 * This exception class is specifically used whenever there is an
 * Accessibility issue for a resource
 * e.g. IProject instance is not accessible.
 */
package com.learning.ant.build.exception;


/**
 * 
 * @author Sushant Jain
 * @since 1.0
 *
 */
public class AccessibilityException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccessibilityException()
	{
		super();
	}
	
	public AccessibilityException(String msg)
	{
		super(msg);
	}
	
	public AccessibilityException(Exception e)
	{
		super(e);
	}
}
