/**
 * This is a generic utility interface which operates on the ClassPath Variables only.
 * 
 */
package com.learning.ant.build.interfaces;

import java.util.Map;

import org.eclipse.core.runtime.IPath;



/**
 * @author Sushant Jain
 * @since 1.0
 * @version 1.0
 *
 */
public interface IGenericUtilities {

	/**
	 * This method is a special case of getAllClassPathVariables method.
	 * It returns only the user defined variables and not the pre-defined variables.
	 * The pre-defined variables list is as follows:
	 * 
	 *  JRE_SRC/
	 *  ECLIPSE_HOME/
	 *  M2_REPO
	 *  JUNIT_HOME
	 *  JRE_SRCROOT/
	 *  JRE_LIB
	 *  JUNIT_SRC_HOME
	 *  
	 * As of now, I couldn't find any mechanism to figure out the list of variables that are
	 * being used in the program and then use them for generating the build script.
	 * The API returns the absolute location to which the relevant variable points.
	 *  
	 * This API can be deprecated in the future.
	 * @return key value pair for the Variable Name, Absolute Location.
	 */
	public Map<String, IPath> getRelevantClassPathVariables();
	
	/**
	 * This method is used to return the ClassPath variable's value
	 * @param classpathVariable String
	 * @return {@link IPath}
	 */
	public IPath getClassPathVariableValue(final String classpathVariable);
	
	/**
	 * This method is a returns all the defined ClassPath variables. 
	 *  
	 * As of now, I couldn't find any mechanism to figure out the list of variables that are
	 * being used in the program and then use them for generating the build script.
	 * The API returns the absolute location to which the relevant variable points.
	 *  
	 * 
	 * @return key value pair for the Variable Name, Absolute Location.
	 */
	public Map<String, IPath> getAllClassPathVariables();
	
	
}
