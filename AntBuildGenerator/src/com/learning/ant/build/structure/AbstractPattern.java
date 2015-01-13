/**
 * 
 */
package com.learning.ant.build.structure;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class AbstractPattern implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pattern;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
}
