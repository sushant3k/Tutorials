/**
 * 
 * 
 */
package com.learning.ant.build.interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import com.learning.ant.build.exception.AccessibilityException;
import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.ant.build.structure.UserLibrary;
import com.learning.build.constants.Constants;
import com.learning.project.facet.ProjectFacetDTO;

/**
 * @author Sushant
 * @version 1.0
 *
 */
public class ProjectUtilitiesImpl implements IProjectUtilities {

	
	public List<ProjectFacetDTO> getProjectFacets(IProject ipr)
	throws ApplicationException
	{
		List<ProjectFacetDTO> pfd = null;		
		
		try
		{
			
			// Create call here is kind of misleading. This doesn't create any facets, instead it converts
			// IProject in the IFacetedProject. This information is not available on any site.
			// I myself have discovered it using some Trial and Error method.
			IFacetedProject ifp = ProjectFacetsManager.create(ipr);			
			if (ifp == null)
			{
				return null;
			}
			Set<IProjectFacetVersion> s = ifp.getProjectFacets();
			
			// Initialize your ProjectFacetDTO list here.
			
			if ( s !=null && !s.isEmpty())
			{
				pfd = new ArrayList<ProjectFacetDTO>(s.size());
			}
			else
			{
				return pfd;
			}
			
			// Find out all the Project Facets...
			for(IProjectFacetVersion ipfv: s)
			{
				
				ProjectFacetDTO pfdt = new ProjectFacetDTO();
				pfdt.setFacetName(ipfv.getProjectFacet().getLabel());
				pfdt.setFacetVersion(ipfv.getVersionString());
				
				pfd.add(pfdt);
			}
		}
		catch(CoreException ce)
		{
			throw new ApplicationException(ce);
		}
//		System.out.println("------------- Worked on Project ------------------ " + ipr);
		return pfd;
	}
	/* (non-Javadoc)
	 * @see com.learning.ant.build.interfaces.IProjectUtilities#getProjectType(org.eclipse.core.resources.IProject)
	 */
	@Override
	public ProjectType getProjectType(IProject ipr) throws ApplicationException{
//		System.out.println("Value of ipr="+ipr);
		List<ProjectFacetDTO> pfd = getProjectFacets(ipr);
		if (pfd == null || pfd.isEmpty())
		{
			throw new ApplicationException("Not a standard Java Project. Or This type of Project is not supported");
		}
		ProjectType pt = null;
		for(ProjectFacetDTO pfdt : pfd)
		{
			if (pfdt.getFacetName().equalsIgnoreCase(Constants.JAVA_PROJECT))
			{
				pt = ProjectType.JAVA;
			}
			if(pfdt.getFacetName().equalsIgnoreCase(Constants.EJB_MODULE))
			{
				pt = ProjectType.EJB;
				return pt;
			}
			if (pfdt.getFacetName().equalsIgnoreCase(Constants.DYNAMIC_WEB_MODULE))
			{
				pt = ProjectType.WEB;
				return pt;
			}
			if (pfdt.getFacetName().equalsIgnoreCase(Constants.EAR))
			{
				pt = ProjectType.EAR;
				return pt;
			}
		}
		return pt;
	}

	/* (non-Javadoc)
	 * @see com.learning.ant.build.interfaces.IProjectUtilities#getAllEARProjectsInWorkspace(org.eclipse.core.resources.IWorkspace)
	 */
	@Override
	public Set<IProject> getAllEARProjectsInWorkspace()
	throws ApplicationException
	{
		
		Set <IProject> setIProject = null;
		try
		{
			// GET ALL EAR Projects From the Workspace.
			Set<IFacetedProject> s =  ProjectFacetsManager.
					getFacetedProjects(ProjectFacetsManager.getProjectFacet(Constants.EAR_FACET));
			if ( null == s || s.isEmpty())
				return setIProject;
			
			
			setIProject = new HashSet<IProject>(s.size());
			
			// Retrieve IProject from IFacetedProject
			for (IFacetedProject ifp : s)
			{
				setIProject.add(ifp.getProject());
			}		
		}
		catch(CoreException e)
		{
			throw new ApplicationException(e);
		}
		return setIProject;
	}

	@Override
	public boolean isEARProject(IProject ipr) throws ApplicationException{
		
		return getProjectType(ipr).equals(ProjectType.EAR);
				
	}

	@Override
	public boolean isWebProject(IProject ipr) throws ApplicationException{
		return getProjectType(ipr).equals(ProjectType.WEB);
	}

	@Override
	public boolean isEJBProject(IProject ipr) throws ApplicationException{
		return getProjectType(ipr).equals(ProjectType.EJB);
	}

	@Override
	public boolean isJavaProject(IProject ipr) throws ApplicationException{
		return getProjectType(ipr).equals(ProjectType.JAVA);
	}



	@Override
	public boolean isSpecifiedProject(IProject ipr, CProjectType.ProjectType prjType) throws ApplicationException{
		
		if(prjType.equals(ProjectType.EAR))
			return isEARProject(ipr);
		
		if(prjType.equals(ProjectType.EJB))
			return isEJBProject(ipr);
		
		if(prjType.equals(ProjectType.WEB))
			return isWebProject(ipr);
		
		if(prjType.equals(ProjectType.JAVA) || prjType.equals(ProjectType.SAR))
			return isJavaProject(ipr);
		
		return false;
	}

	public Set<IJavaProject> getAllAccessibleEARJavaProjectsInWorkspace() throws ApplicationException
	{
		Set <IJavaProject> setIProject = null;
		try
		{
			// GET ALL EAR Projects From the Workspace.
			Set<IFacetedProject> s =  ProjectFacetsManager.
					getFacetedProjects(ProjectFacetsManager.getProjectFacet(Constants.EAR_FACET));
			if ( null == s || s.isEmpty())
				return setIProject;
			
			
			setIProject = new HashSet<IJavaProject>(s.size());
			
			// Retrieve IProject from IFacetedProject
			for (IFacetedProject ifp : s)
			{
				IProject ipr = ifp.getProject();
				if (ipr.isAccessible())
				{
					setIProject.add(JavaCore.create(ipr));
				}
			}		
		}
		catch(CoreException e)
		{
			throw new ApplicationException(e);
		}
		return setIProject;
	}
	@Override
	public Set<IProject> getAllAccessibleEARProjectsInWorkspace() throws ApplicationException{
		Set<IProject> set = getAllEARProjectsInWorkspace();
		if (set == null || set.isEmpty())
			return null;
		
		for(IProject s : set)
		{
			if (!s.isAccessible())
			{
				set.remove(s);
			}
		}
		return set;
	}

	@Override
	public IJavaProject getJavaProjectFromIProject(IProject ipr) {
		
		IJavaProject jp = JavaCore.create(ipr);
		
		return jp;
	}
	@Override
	public IProject getAccessibleProjectByProjectName(String projectName) 
	throws ApplicationException
	{
		
		IProject ipr = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (ipr == null)
		{
			throw new ApplicationException("Could not find project with the name="+projectName);
		}
		if (!ipr.isAccessible())
		{
			throw new AccessibilityException("Could not access project with the name="+projectName);
		}
		return ipr;
	}
	@Override
	public Set<IProject> getAllReferencedProjects(IProject ipr)
			throws ApplicationException {
		if (ipr == null)
			throw new IllegalArgumentException("Input Project cannot be null");
		
		IProject iprs[] = null;
		try {
			iprs = ipr.getReferencedProjects();
		} catch (CoreException e) {
			throw new ApplicationException(e);
		}
		if(iprs == null || iprs.length ==0 )
			return null;
		Set<IProject> set = null;
		for(IProject ip: iprs)
		{
			if (set == null)
				set = new HashSet<IProject>();
			if(ip.isAccessible())
				set.add(ip);
		}
		return set;
	}

	public boolean isLibraryExistsInProjectsClassPath(IJavaProject ijp, IPath libraryPath )
	{		
		if (ijp == null || libraryPath ==null)
			throw new IllegalArgumentException("Either IJavaProject is invalid or library path is  missing.");
		IClasspathEntry ices[] = ijp.readRawClasspath();
		if (ices == null || ices.length ==0)
			return false;
		boolean found = false;
		for(IClasspathEntry ice : ices)
		{		
			// Retreiving the Container type entries, because libraries are of type CPE_CONTAINER
			if (ice.getEntryKind() == IClasspathEntry.CPE_CONTAINER)
			{
				IPath ip2 = ice.getPath();
				if(ip2.toString().equals(libraryPath.toString()))
				{
					found = true;
					break;
				}
			}
		}
		return found;
	}
	public Set<UserLibrary> getUserLibraries(IProject ipr) throws ApplicationException
	{
		
		Set <UserLibrary> setUL = null;
		
		// Get All UserLibrary References
		String ulnames [] = JavaCore.getUserLibraryNames();		
		for(String ul : ulnames)
		{
			IPath ip = new Path(JavaCore.USER_LIBRARY_CONTAINER_ID).append(ul);
			IClasspathContainer icc;
			try {
				icc = JavaCore.getClasspathContainer(ip, JavaCore.create(ipr));
				
			} catch (JavaModelException e) {
				throw new ApplicationException(e);
			}
			
			IJavaProject ijp = JavaCore.create(ipr);
			
			if (!isLibraryExistsInProjectsClassPath(ijp, ip))
				continue;
			
			// Process only when the library is required by a project.
			if (setUL == null)
			{
				setUL = new HashSet<UserLibrary>();
				
			}
			
			UserLibrary u = new UserLibrary(ul, icc.getClasspathEntries());
			setUL.add(u);

			
		}
		return setUL;
	}
	

	@Override
	public IClasspathEntry[] getProjectClassPath(IProject ipr)
			throws ApplicationException {
		if (ipr == null)
			throw new ApplicationException("Invalid value");
		
		IJavaProject ijp = JavaCore.create(ipr);
		if(ijp == null)
		{
			throw new ApplicationException("The Project is not of type Java.");
		}
		IClasspathEntry [] ice = ijp.readRawClasspath();
		
		//TODO  i.getExclusionPatterns()
		//TODO i.getInclusionPatterns();
		
		return ice;
	}
	
	@Override
	public IPath getProjectCompilationOutputLocation(IProject ipr)
			throws ApplicationException {
		if (ipr == null)
			throw new IllegalArgumentException("Invalid Project");
		IJavaProject ijp = JavaCore.create(ipr);
		IPath ip = null;
		
		ip = ijp.readOutputLocation();//.getOutputLocation();
		
		return ip;
	}
	@Override
	public Set<IJavaProject> getAllAccessibleEJBProjectsInWorkspace()
			throws ApplicationException {
		Set <IJavaProject> setIProject = null;
		try
		{
			// GET ALL EAR Projects From the Workspace.
			Set<IFacetedProject> s =  ProjectFacetsManager.
					getFacetedProjects(ProjectFacetsManager.getProjectFacet(Constants.EJB_FACET));
			if ( null == s || s.isEmpty())
				return setIProject;
			
			
			setIProject = new HashSet<IJavaProject>(s.size());
			
			// Retrieve IProject from IFacetedProject
			for (IFacetedProject ifp : s)
			{
				setIProject.add(JavaCore.create(ifp.getProject()));
			}		
		}
		catch(CoreException e)
		{
			throw new ApplicationException(e);
		}
		return setIProject;
	}
	@Override
	public Set<IJavaProject> getAllAccessibleWebProjectsInWorkspace()
			throws ApplicationException {
		Set <IJavaProject> setIProject = null;
		try
		{
			// GET ALL EAR Projects From the Workspace.
			Set<IFacetedProject> s =  ProjectFacetsManager.
					getFacetedProjects(ProjectFacetsManager.getProjectFacet(Constants.WEB_FACET));
			if ( null == s || s.isEmpty())
				return setIProject;
			
			
			setIProject = new HashSet<IJavaProject>(s.size());
			
			// Retrieve IProject from IFacetedProject
			for (IFacetedProject ifp : s)
			{
				setIProject.add(JavaCore.create(ifp.getProject()));
			}		
		}
		catch(CoreException e)
		{
			throw new ApplicationException(e);
		}
		return setIProject;
	}
	@Override
	public Set<IJavaProject> getAllAccessibleUtilityProjectsInWorkspace()
			throws ApplicationException {
		Set <IJavaProject> setIProject = null;
		try
		{
			// GET ALL EAR Projects From the Workspace.
			Set<IFacetedProject> s =  ProjectFacetsManager.
					getFacetedProjects(ProjectFacetsManager.getProjectFacet(Constants.UTILITY_FACET));
			if ( null == s || s.isEmpty())
				return setIProject;
			
			
			setIProject = new HashSet<IJavaProject>(s.size());
			
			// Retrieve IProject from IFacetedProject
			for (IFacetedProject ifp : s)
			{
				setIProject.add(JavaCore.create(ifp.getProject()));
			}		
		}
		catch(CoreException e)
		{
			throw new ApplicationException(e);
		}
		return setIProject;
	}
}
