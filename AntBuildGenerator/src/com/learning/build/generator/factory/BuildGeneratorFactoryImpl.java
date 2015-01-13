/**
 * 
 */
package com.learning.build.generator.factory;

import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.build.constants.Constants;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
public class BuildGeneratorFactoryImpl implements IBuildGeneratorFactory {

	
	/* (non-Javadoc)
	 * @see com.learning.build.generator.factory.IBuildGeneratorFactory#getInstance(com.learning.ant.build.interfaces.JSProjectType.ProjectType)
	 */
	@Override
	public BaseBuildGeneratorEngine getInstance(ProjectType prjType) {
		BaseBuildGeneratorEngine ibe = null;
		if (prjType == null)
			throw new IllegalArgumentException(Constants.NULL_PROJECT_TYPE);
		
		if (prjType.equals(ProjectType.EAR))
			{
				ibe = new EARBuildGeneratorEngine();
			}
		
		else if (prjType.equals(ProjectType.WEB))
		{
			ibe = new WEBBuildGeneratorEngine();
		}
		
		else if (prjType.equals(ProjectType.JAVA))
		{
			ibe = new JarBuildGeneratorEngine();
		}
		else if (prjType.equals(ProjectType.EJB))
		{
			ibe = new EJBJarBuildGeneratorEngine();
		}
		else if (prjType.equals(ProjectType.SAR))
		{
			ibe = new SARBuildGeneratorEngine();
		}
		
		return ibe;
	}

}
