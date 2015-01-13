/**
 * This class is an abstraction of the project deployment structure.
 * 
 */
package com.learning.ant.build.structure;

/**
 * @author Sushant jain
 * @version 1.0
 * @since 1.0
 *
 */
public abstract class AbstractDeploymentProjectStructure {

	private String archiveName;
	
	public AbstractDeploymentProjectStructure()
	{
		
	}

	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}
	
	
}
