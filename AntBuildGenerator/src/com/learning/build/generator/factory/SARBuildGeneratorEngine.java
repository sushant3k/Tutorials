/**
 * 
 */
package com.learning.build.generator.factory;

import org.eclipse.core.resources.IProject;

import com.learning.ant.build.structure.ProjectBuildStructure;

/**
 * @author Sushant Jain
 * @version 1.0
 *
 */
public class SARBuildGeneratorEngine extends JarBuildGeneratorEngine{

	public SARBuildGeneratorEngine()
	{
		
		
	}

	@Override
	public ProjectBuildStructure generateBuild(IProject ipr) {
		return super.generateBuild(ipr);
	}


}
