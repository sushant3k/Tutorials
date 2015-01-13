/**
 * Every Java Project has a compilation Structure. 
 * This class encapsulates compile time libraries, user libraries, output location,
 * reference projects and the source folders information.
 *  
 */
package com.learning.ant.build.structure;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IPackageFragmentRoot;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0O
 *
 */
public class ProjectCompilationStructure implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Stores compile time libraries
	 */
 
	private IClasspathEntry[] compileTimeLibraries;
	
	/**
	 * Stores user libraries
	 */
	private Set<UserLibrary> compileTimeUserLibraries;
	
	/**
	 * Stores Source Project for this compilation structure.
	 */
	private IProject iProject;
	
	/**
	 * Stores the output folder in which the class files need to be generated
	 * after the compilation.
	 */

	private IPath outputLocation;
	
	
	/**
	 * Stores references to all the projects that are being referenced from this project.
	 */
	private Set<IProject> referencedProjects;
	
	/**
	 * Stores all the source folder that are being used in this project.
	 */
	private List<IPackageFragmentRoot>  sourceFolders;
	
	
	
	
	private Hashtable<String, List<Set<AbstractPattern>>> pattern;
	
	public void addToExclusionInclusionPattern(final String sourceFolder, List<Set<AbstractPattern>> lst)
	{
		if (sourceFolder == null || lst == null)
		{
			return;
		}
		if (pattern == null)
		{
			pattern = new Hashtable<String, List<Set<AbstractPattern>>>();
		}
		pattern.put(sourceFolder, lst);
	}
	
	

	public Hashtable<String, List<Set<AbstractPattern>>> getPattern() {
		return pattern;
	}



	public void setPattern(Hashtable<String, List<Set<AbstractPattern>>> pattern) {
		this.pattern = pattern;
	}



	public List<IPackageFragmentRoot> getSourceFolders() {
		return sourceFolders;
		
	}

	public void setSourceFolders(List<IPackageFragmentRoot> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	
	public IClasspathEntry[] getCompileTimeLibraries() {
		return compileTimeLibraries;
	}

	public void setCompileTimeLibraries(IClasspathEntry[] compileTimeLibraries) {
		this.compileTimeLibraries = compileTimeLibraries;
	}

	public IPath getOutputLocation() {
		return outputLocation;
	}

	public void setOutputLocation(IPath outputLocation) {
		this.outputLocation = outputLocation;
	}

	public IProject getiProject() {
		return iProject;
	}

	public void setiProject(IProject iProject) {
		this.iProject = iProject;
	}

	
	public Set<IProject> getReferencedProjects() {
		return referencedProjects;
	}

	public void setReferencedProjects(Set<IProject> referencedProjects) {
		this.referencedProjects = referencedProjects;
	}


	private String classPathId;
	public ProjectCompilationStructure()
	{
		
	}
	
	public Set<UserLibrary> getCompileTimeUserLibraries() {
		return compileTimeUserLibraries;
	}
	public void setCompileTimeUserLibraries(
			Set<UserLibrary> compileTimeUserLibraries) {
		this.compileTimeUserLibraries = compileTimeUserLibraries;
	}
	public String getClassPathId() {
		return classPathId;
	}
	public void setClassPathId(String classPathId) {
		this.classPathId = classPathId;
	}
	
	
}
