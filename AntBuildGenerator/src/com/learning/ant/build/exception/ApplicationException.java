/**
 * This is an extension of the Exception Class which is used to throw 
 * application specific exceptions.
 * 
 */
package com.learning.ant.build.exception;

/**
 * @author Sushant Jain
 * @since 1.0
 */
public class ApplicationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationException()
	{
		super();
	}
	public ApplicationException(Exception e)
	{
		super(e);
	}
	
	public ApplicationException (final String msg)
	{
		super(msg);
	}
}
