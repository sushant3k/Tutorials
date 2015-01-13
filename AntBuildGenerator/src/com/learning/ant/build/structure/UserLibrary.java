/**
 * This Class represents User Library Structure.
 */
package com.learning.ant.build.structure;

import java.io.Serializable;

import org.eclipse.jdt.core.IClasspathEntry;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
public class UserLibrary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores User library name
	 */
	private String name;
	
	/**
	 * Stores Classpath Entries.
	 */
	private IClasspathEntry[] listOfJars;
	
	
	public UserLibrary(final String name, final IClasspathEntry[] ice)
	{
		this.name = name;
		this.listOfJars = ice;
	}
	public String getName() {
		return name;
	}
	public IClasspathEntry[] getListOfJars() {
		return listOfJars;
	}
	
}
