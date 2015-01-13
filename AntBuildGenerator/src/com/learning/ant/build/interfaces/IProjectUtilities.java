/**
 * Project Utilities interface provides utility functions for the Java Projects.
 * The Utility functions can be categorized into:
 * <ol>
 * <li>Project Facets</li>
 * <li>Type of Project</li>
 * <li>Validation functions</li>
 * </ol>
 * 	 
 */
package com.learning.ant.build.interfaces;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.ant.build.structure.UserLibrary;
import com.learning.project.facet.ProjectFacetDTO;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 * 
 */
public interface IProjectUtilities {

	/**
	 * The API is used to fetch facets information of a Project.
	 * 
	 * @param ipr {@link IProject}
	 * @return {@link ProjectFacetDTO}
	 * @throws ApplicationException
	 */
	
	public List<ProjectFacetDTO> getProjectFacets(IProject ipr)
			throws ApplicationException;
			
	/**
	 * This method is used to get the type of Project based on the IProject. It determines a match by browsing
	 * through the facets associated with the IProject instance.
	 * @param ipr  - {@linkplain IProject}
	 * @return {@link ProjectType}
	 * @throws ApplicationException
	 */
	public ProjectType getProjectType(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to fetch all the EAR Projects that 
	 * are available in the Current Eclipse Workspace. This method 
	 * only lists those projects which are accessible in the workspace. 
	 * By accessibility it means
	 * (i) It is present
	 * (ii) It is open
	 * @return  {@link Set}  {@Link IProject}
	 *  @throws ApplicationException
	 */
	public Set<IProject> getAllAccessibleEARProjectsInWorkspace() throws ApplicationException;
	
	/**
	 * This Method returns all the EAR Projects present in the workspace. This method utilizes the 
	 * Facets Information available with the Java Project. If the project is configured with the EAR facet
	 * then it is determined to be an EAR project.
	 * @return
	 * @throws ApplicationException
	 */
	public Set<IProject> getAllEARProjectsInWorkspace() throws ApplicationException;
	
	/**
	 * This method is used to check whether a Project is of specified type or not. This method utilizes the 
	 * Facets Information available with the Java Project. It returns true if an exact match is found.
	 * @param ipr {@link IProject}
	 * @param prjType {@link ProjectType}
	 * @return true/false
	 * @throws ApplicationException
	 */
	public boolean isSpecifiedProject(IProject ipr, CProjectType.ProjectType prjType) throws ApplicationException;
	
	/**
	 * This method is used to check whether a Project is an EAR Project or not. This method utilizes the 
	 * Facets Information available with the Java Project. If the facet doesn't reflect that the IProject instance
	 * contains any EAR facet then it returns false. It returns true, if a match is found.
	 * @param ipr {@link IProject}
	 * @return true/false
	 * @throws ApplicationException
	 */
	public boolean isEARProject(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to check whether a Project is an Dynamic Web Project or not.
	 * It returns true, if a match is found.
	 * @param ipr {@link IProject}
	 * @return true/false
	 * @throws ApplicationException
	 */
	
	public boolean isWebProject(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to check whether a Project is an EJB Project or not.
	 * It returns true, if a match is found.
	 * @param ipr {@link IProject}
	 * @return true/false
	 * @throws ApplicationException
	 */
	
	public boolean isEJBProject(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to check whether a Project is only a Java Project or not.
	 * It returns true, if a match is found.
	 * @param ipr {@link IProject}
	 * @return true/false
	 * @throws ApplicationException
	 */
	public boolean isJavaProject(IProject ipr) throws ApplicationException;
	
	
	/**
	 * This method is used to convert IProject in IJavaProject 
	 * @param ipr {@link IProject}
	 * @return {@link IJavaProject}
	 */
	public IJavaProject getJavaProjectFromIProject(final IProject ipr);
	
	
	/**
	 * Eclipse provides mechanism to close the projects in the workspace. In that scenario, the projects
	 * are not accessible. This method takes the name of the project as an input and determine whether the
	 * project is accessible or not. It returns true if the project is accessible otherwise it returns false. 
	 * @param projectName
	 * @return
	 * @throws ApplicationException
	 */
	public IProject getAccessibleProjectByProjectName(final String projectName) throws ApplicationException;
	
	/**
	 * In the build path of a Java Project, there is an option to reference other java projects. 
	 * This method returns all the accessible referenced projects for the IProject instance.
	 * @param ipr
	 * @return
	 * @throws ApplicationException
	 */
	public Set<IProject> getAllReferencedProjects(final IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to returns the configured classpath of the project.
	 * This doesn't return anything related to the User Library.
	 * @param ipr
	 * @return
	 * @throws ApplicationException
	 */
	public IClasspathEntry[] getProjectClassPath(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to fetch the Project's compilation output location.
	 * e.g. If the project is configured to spit out the classes in the build/classes directory.
	 * This method returns this value.
	 * @param ipr
	 * @return
	 * @throws ApplicationException
	 */
	public IPath getProjectCompilationOutputLocation(IProject ipr) throws ApplicationException;
	
	/**
	 * The .classpath file of a java project has entries for the Classpath in the following format:
	 * &lt;classpath &gt;
	 * .
	 * .
	 * &lt;classpathentry kind="con" path="org.eclipse.jdt.USER_LIBRARY/my-custom-library"&gt;
	 * &lt;/classpath&gt;
	 * 
	 * This method is used to determine whether a User library exists in the classpath of the project
	 * or not.
	 * It returns true if the library is found the build path of the project. Otherwise it returns false.
	 * The method can throw a runtime exception, if either IProject is not a valid java project or 
	 * LibraryPath is null. 
	 * @param ijp - {@link IJavaProject} - IJavaProject instance in which library is to be found.
	 * @param libraryPath - Library path in the format as provided in the .classpath file.
	 * @return boolean
	 */
	public boolean isLibraryExistsInProjectsClassPath(IJavaProject ijp, IPath libraryPath );
	
	/**
	 * The .classpath file of a java project has entries for the Classpath in the following format:
	 * &lt;classpath &gt;
	 * .
	 * .
	 * &lt;classpathentry kind="con" path="org.eclipse.jdt.USER_LIBRARY/my-custom-library"&gt;
	 * &lt;/classpath&gt;
	 * 
	 * This method is used to getUserLibraries name and the corresponding Jars. This method is not suitable
	 * for fetching all the user libraries irrespective of whether they are being used in the projects or not.
	 * 
	 * @return Set <UserLibrary> {@link UserLibrary}
	 * 
	 */
	public Set<UserLibrary> getUserLibraries(IProject ipr) throws ApplicationException;
	
	/**
	 * This method is used to fetch all the EAR Projects that 
	 * are available in the Current Eclipse Workspace. This method 
	 * only lists those projects which are accessible in the workspace. 
	 * By accessibility it means
	 * (i) It is present
	 * (ii) It is open
	 * @return  {@link Set}  {@Link IJavaProject}
	 *  @throws ApplicationException
	 */
	
	public Set<IJavaProject> getAllAccessibleEARJavaProjectsInWorkspace() throws ApplicationException;
	
	/**
	 * This method is used to fetch all the EJB Projects that 
	 * are available in the Current Eclipse Workspace. This method 
	 * only lists those projects which are accessible in the workspace. 
	 * By accessibility it means
	 * (i) It is present
	 * (ii) It is open
	 * @return  {@link Set}  {@Link IJavaProject}
	 *  @throws ApplicationException
	 */
	public Set<IJavaProject> getAllAccessibleEJBProjectsInWorkspace() throws ApplicationException;
	
	/**
	 * This method is used to fetch all the Web Projects that 
	 * are available in the Current Eclipse Workspace. This method 
	 * only lists those projects which are accessible in the workspace. 
	 * By accessibility it means
	 * (i) It is present
	 * (ii) It is open
	 * @return  {@link Set}  {@Link IJavaProject}
	 *  @throws ApplicationException
	 */
	public Set<IJavaProject> getAllAccessibleWebProjectsInWorkspace() throws ApplicationException;
	
	
	/**
	 * This method is used to fetch all the Utility Projects that 
	 * are available in the Current Eclipse Workspace. This method 
	 * only lists those projects which are accessible in the workspace. 
	 * By accessibility it means
	 * (i) It is present
	 * (ii) It is open
	 * @return  {@link Set}  {@Link IJavaProject}
	 *  @throws ApplicationException
	 */
	public Set<IJavaProject> getAllAccessibleUtilityProjectsInWorkspace() throws ApplicationException;
	
}
