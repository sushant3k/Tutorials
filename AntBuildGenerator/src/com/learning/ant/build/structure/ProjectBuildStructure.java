/**
 * Every Java Project has a build structure which comprises of 
 * compilation and deployment structure. This class encapsulates both project compilation
 * structure and the project deployment structure. 
 */
package com.learning.ant.build.structure;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.learning.ant.build.xml.structure.BuildTarget;

/**
 * 
 * @author Sushant jain
 * @version 1.0
 * @since 1.0
 *
 */
public class ProjectBuildStructure {

	// Project Compilation Structure
	private ProjectCompilationStructure pcs;
	// Project Deployment Structure
	private ProjectDeploymentStructure pds;
	
	// Classpath variables
	private Map <String, IPath> classpathVariables;
	private Collection<BuildTarget> cbt ;
	
	// Collection of Build Targets to generate the final ANT build.
	public Collection<BuildTarget> getCbt() {
		return cbt;
	}

	public void setCbt(Collection<BuildTarget> cbt) {
		this.cbt = cbt;
	}

	public ProjectBuildStructure(ProjectCompilationStructure pcs, ProjectDeploymentStructure pds)
	{
		this.pcs = pcs;
		this.pds = pds;
	}

	public Map<String, IPath> getClasspathVariables() {
		return classpathVariables;
	}

	public void setClasspathVariables(Map<String, IPath> classpathVariables) {
		this.classpathVariables = classpathVariables;
	}

	public IProject getProject()
	{
		if (pcs != null)		
		{
			return this.pcs.getiProject();
		}
		return null;
	}
	
	public ProjectCompilationStructure getPcs() {
		return pcs;
	}

	public void setPcs(ProjectCompilationStructure pcs) {
		this.pcs = pcs;
	}

	public ProjectDeploymentStructure getPds() {
		return pds;
	}

	public void setPds(ProjectDeploymentStructure pds) {
		this.pds = pds;
	}
	
	
}
