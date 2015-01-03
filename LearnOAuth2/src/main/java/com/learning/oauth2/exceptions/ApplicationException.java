package com.learning.oauth2.exceptions;

/**
 * This class is an extension of the System level Exception class
 * This is a bare class just for the sake of implementation. 
 * @author Sushant
 * @version 1.0
 */
public class ApplicationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int exceptionCode;
	private String message;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public ApplicationException()
	{
		super();
	}
	
	public ApplicationException(Exception e)
	{
		super(e);
		
	}
	
	public ApplicationException(String exceptionMessage)
	{	
		super(exceptionMessage);
		this.message = exceptionMessage;
	}
	
	public ApplicationException(String message, Exception e)
	{
		super(message, e);
	}
	public ApplicationException(int code, String message)
	{
		this.exceptionCode = code;
		this.message = message;
	}
}
