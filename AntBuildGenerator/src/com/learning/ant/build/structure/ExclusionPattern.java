/**
 * 
 */
package com.learning.ant.build.structure;

/**
 * @author Sushant
 *
 */
public class ExclusionPattern extends AbstractPattern{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExclusionPattern(String s)
	{
		super.setPattern(s);
	}
}
