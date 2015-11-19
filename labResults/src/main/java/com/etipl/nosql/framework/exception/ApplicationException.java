/**
 * 
 */
package com.etipl.nosql.framework.exception;

/**
 * @author Sushant
 *
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
	public ApplicationException(final String message)
	{
		super(message);
	}
	
	public ApplicationException(Throwable e)
	{
		super(e);
	}
}
