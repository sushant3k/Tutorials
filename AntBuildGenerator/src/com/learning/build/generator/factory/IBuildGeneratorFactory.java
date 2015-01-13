/**
 *	This is a factory interface for getting a reference to the BuildGeneration Engine.
 *	The implemenation ensures that the correct reference to the BuildGeneration Engine
 *	is available to the client based on the Project Type.
 * 
 */
package com.learning.build.generator.factory;

import com.learning.ant.build.interfaces.CProjectType.ProjectType;

/**
 * @author sushant
 *
 */
public interface IBuildGeneratorFactory {

	/**
	 * Fetch an instance of the Build Generator Engine.
	 * This makes sure that the Client gets a reference of the correct 
	 * implementation. The method throws a runtime exception if the ProjectType is null.
	 * @param prjType  - Type of Project {@link ProjectType}
	 * @return 
	 */
	public BaseBuildGeneratorEngine getInstance(ProjectType prjType);
	
	
}
