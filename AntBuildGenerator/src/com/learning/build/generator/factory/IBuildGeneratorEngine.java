/**
 * This interface is main interface for the generation of ANT builds.
 * 
 */
package com.learning.build.generator.factory;

import org.eclipse.core.resources.IProject;

import com.learning.ant.build.structure.ProjectBuildStructure;
import com.learning.ant.build.xml.structure.Project;

public interface IBuildGeneratorEngine {

	/**
	 * This method is used to init the build process. 
	 * @param ipr
	 * @return
	 */
	public ProjectBuildStructure generateBuild(IProject ipr);
	
	/**
	 * This method is used to get the Project Structure. This Project Structure contains data relevant for generating the build file. 
	 *  
	 * @param iproj - {@link IProject}
	 * @return {@link Project}
	 */
	
	public Project packageArchive(IProject iproj, IProject relativeTo);
	
	public Project getPostProcessCompilationTasks(IProject relativeTo, final IProject ipr);
}
