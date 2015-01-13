/**
 * Java Projects has a deployment structure which is configured in the Deployment Assembly.
 * This class stores information related to the deployment assembly configuratio of the
 * project.
 */
package com.learning.ant.build.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
public class ProjectDeploymentStructure extends AbstractDeploymentProjectStructure{

	/**
	 * Stores list of the Projects configured in the deployment assembly.
	 */
	private List<Projects> projects; 
	/**
	 * Stores libraries configured in the deployment assembly.
	 */
	private List<Libraries> libs;
	
	/**
	 * Any meta properties.
	 */
	private Properties metaProperties;
	
	/**
	 * Any meta sources.
	 */
	private IPath[] metaSources;
	
	/**
	 * Project Instance for which this deployment structure is valid.
	 */
	private IProject ipr;
	
	/**
	 * Stores root folder e.g. EarContent, WebContent
	 */
	private IPath rootFolder;
	
	/**
	 * Stores the runtime path of the root folder. 
	 * This is used to determine where the libraries need to go during the packaging process.
	 */
	private IPath rootFolderRuntimePath;
	
	


	public IPath getRootFolderRuntimePath() {
		return rootFolderRuntimePath;
	}

	public void setRootFolderRuntimePath(IPath rootFolderRuntimePath) {
		this.rootFolderRuntimePath = rootFolderRuntimePath;
	}

	public IPath getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(IPath rootFolder) {
		this.rootFolder = rootFolder;
	}

	public void setProjects(List<Projects> projects) {
		this.projects = projects;
	}

	public void setLibs(List<Libraries> libs) {
		this.libs = libs;
	}

	public IProject getIpr() {
		return ipr;
	}

	public void setIpr(IProject ipr) {
		this.ipr = ipr;
	}

	public Properties getMetaProperties() {
		return metaProperties;
	}

	public void setMetaProperties(Properties metaProperties) {
		this.metaProperties = metaProperties;
	}

	public IPath[] getMetaSources() {
		return metaSources;
	}

	public void setMetaSources(IPath[] metaSources) {
		this.metaSources = metaSources;
	}

	public void addProject(Projects project)
	{
		if (project == null)
			throw new IllegalArgumentException("Argument cannot be null");
		if (projects == null)
			projects = new ArrayList<ProjectDeploymentStructure.Projects>();
		
		projects.add(project);
		
	}
	
	public void addLibrary(Libraries library)
	{
		if (library == null)
			throw new IllegalArgumentException("Argument cannot be null");
		if (libs == null)
			libs = new ArrayList<ProjectDeploymentStructure.Libraries>();
		libs.add(library);
	}
	public Projects getProjectsNewInstance()
	{
		return new Projects();
	}
	public Libraries getLibrariesNewInstance()
	{
		return new Libraries();
	}
	
	/**
	 * This method returns a cloned list of the Projects.
	 * @return
	 */
	public List<Projects> getProjects() {
		if (projects == null)
		{
			return new ArrayList<ProjectDeploymentStructure.Projects>();			
		}
		List<Projects> lst = new ArrayList<ProjectDeploymentStructure.Projects>(this.projects.size());
		
		for (Projects proj: this.projects)
		{
			lst.add((Projects)proj.clone());
		}
		return lst;
	}
	
	
	/**
	 * This method returns a cloned list of the libraries
	 * @return
	 */
	
	public List<Libraries> getLibs() {
		
		if (libs == null)
		{
			return new ArrayList<ProjectDeploymentStructure.Libraries>();			
		}
		List<Libraries> lst = new ArrayList<ProjectDeploymentStructure.Libraries>(this.libs.size());
		
		for (Libraries lib: this.libs)
		{
			lst.add((Libraries)lib.clone());
		}
		return lst;
		
	}
	
	
	public class AbstractDeploymentAssembly implements Cloneable
	{
		private String runtimePath;
		
		private IPath iruntimePath;
		
		private String name;
		
		private IVirtualComponent ivc;
		
		private IProject iProject;
		
		private String archiveName;
		
		
		public String getArchiveName() {
			return archiveName;
		}

		public void setArchiveName(String archiveName) {
			this.archiveName = archiveName;
		}

		public IProject getiProject() {
			return iProject;
		}

		public void setiProject(IProject iProject) {
			this.iProject = iProject;
		}

		public IVirtualComponent getIvc() {
			return ivc;
		}

		public void setIvc(IVirtualComponent ivc) {
			this.ivc = ivc;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public IPath getIruntimePath() {
			return iruntimePath;
		}

		public void setIruntimePath(IPath iruntimePath) {
			this.iruntimePath = iruntimePath;
		}

		public String getRuntimePath() {
			return runtimePath;
		}

		public void setRuntimePath(String runtimePath) {
			this.runtimePath = runtimePath;
		}
		
		@Override
		public Object clone() throws CloneNotSupportedException
		{
			return super.clone();
		}
	}
	public  class Projects extends AbstractDeploymentAssembly implements Cloneable
	{
		private String deployedName;
		
		
		public String getDeployedName() {
			return deployedName;
		}


		public void setDeployedName(String deployedName) {
			this.deployedName = deployedName;
		}


		@Override
		public Object clone() 
		{
			Projects proj = null;
			try
			{
				proj = (Projects)super.clone();
				proj.setRuntimePath(this.getRuntimePath());
				proj.setIruntimePath(this.getIruntimePath());
				proj.setName(this.getName());
				proj.setDeployedName(this.getDeployedName());
				proj.setiProject(this.getiProject());
				proj.setArchiveName(this.getArchiveName());
			}
			catch(CloneNotSupportedException cnse)
			{
				throw new RuntimeException(cnse.getMessage());
			}
			return proj;
		}
	}
	public  class Libraries extends AbstractDeploymentAssembly
	{
		private IPath archivePath;
		
		public IPath getArchivePath() {
			return archivePath;
		}

		public void setArchivePath(IPath archivePath) {
			this.archivePath = archivePath;
		}

		@Override
		public Object clone() 
		{
			Libraries libs = null;
			try
			{
				libs = (Libraries)super.clone();
				libs.setRuntimePath(this.getRuntimePath());
				libs.setIruntimePath(this.getIruntimePath());
				libs.setArchivePath(this.getArchivePath());
			}
			catch(CloneNotSupportedException cnse)
			{
				throw new RuntimeException(cnse.getMessage());
			}
			return libs;
		}
	}
	
}
