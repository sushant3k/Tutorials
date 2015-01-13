/**
 * This is a controller Interface that is used to trigger the build process.
 */
package com.learning.ant.build.action;

import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import com.learning.ant.build.exception.AccessibilityException;
import com.learning.ant.build.exception.ApplicationException;

/**
 * 
 * @author Sushant
 * @version 1.0
 * @since 1.0
 *
 */
public interface IBuildTriggerEngine {

	/**
	 * This method expects collection of java projects and iterates over them to generate a separate build file for each project.
	 * @param ijp - Instance of IJavaProject ({@linkplain IJavaProject}
	 * @throws AccessibilityException - If the project is not accessible, then this exception is thrown
	 * @throws ApplicationException - If the project type for which build generation is not supported, this exception is thrown.
	 */
	public void triggerBuild(List<IJavaProject> ijp) throws AccessibilityException, ApplicationException;
	
	/**
	 * This method expects a java project for which build file is to be generated.
	 * @param ijp - Instance of IJavaProject ({@linkplain IJavaProject}
	 * @throws AccessibilityException - If the project is not accessible, then this exception is thrown
	 * @throws ApplicationException - If the project type for which build generation is not supported, this exception is thrown.
	 */
	public void triggerBuild(IJavaProject ijp) throws AccessibilityException, ApplicationException;
}
