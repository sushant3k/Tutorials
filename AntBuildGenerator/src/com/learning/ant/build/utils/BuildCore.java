/**
 * This is the core utility class that provide various utility methods.
 */
package com.learning.ant.build.utils;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.learning.ant.build.interfaces.GenericUtilitiesImpl;
import com.learning.ant.build.interfaces.IGenericUtilities;
import com.learning.ant.build.structure.ProjectBuildStructure;
import com.learning.ant.build.structure.ProjectCompilationStructure;
import com.learning.ant.build.structure.UserLibrary;
import com.learning.ant.build.xml.structure.PathElement;
import com.learning.ant.build.xml.structure.PathTask;
import com.learning.ant.build.xml.structure.Project;
import com.learning.ant.build.xml.structure.Property;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
public class BuildCore {

	public static final String LIBRARY_CLASSPATH_SUFFIX = "libraryclasspath";
	public static final String PROJECT_CLASSPATH_SUFFIX = "projectclasspath";

	public static ConcurrentMap<String, Integer> projectsWorkedOn = new ConcurrentHashMap<String, Integer>();
	
	public static Set<String> postProcessCompilationTasks = new HashSet<String>();
	
	public static Set<String> classPathLibraries = new HashSet<String>();
	
	private volatile static IPath workspaceLocation = null;

	/**
	 * This method is used to return the Project Properties. Especially, it iterates over
	 * all the classpath variables and then generates the corresponding targets in the ANT build file.
	 *  
	 * @param i {@link IPath}
	 * @return Collection of Property.
	 */
	public static Collection<Property> getProjectProperties(final IPath i)
	{
		Collection<Property> colProperty = null;
		
		IGenericUtilities igu = new GenericUtilitiesImpl();
		Map<String,IPath> mp = igu.getRelevantClassPathVariables();
		if (mp == null || mp.isEmpty())
			return colProperty;
		colProperty = new ArrayList<Property>();
		Property p = new Property();
		p.setEnvironment("env");
		colProperty.add(p);
		for(Map.Entry<String, IPath> m : mp.entrySet())
		{
			
			IPath ip = m.getValue();
			if (ip.isEmpty())
			{
				continue;
			}
		
			Property prop = new Property();
			prop.setName(m.getKey());
			prop.setValue(getRelativePath(i, ip).toString());
			colProperty.add(prop);				
		}
		
		return colProperty;
	}
	

	/**
	 * This method returns the ANT build's Path tasks for all the compilation units. It iterates through the classpath entries and 
	 * generates the ANT path task.
	 * @param pcs - {@link ProjectCompilationStructure}
	 * @param relativeTo - {@linkplain IProject}
	 * @return - Returns collection of ANT Path tasks.
	 */
	public static Collection<PathTask> getCompilationAntPathElements(
			final ProjectCompilationStructure pcs, final IProject relativeTo)
	{

		if(postProcessCompilationTasks.contains(pcs.getiProject().getName()))
		{
			return null;
		}
		Collection<PathTask> pathTasks = new ArrayList<PathTask>();
		// GET Individual ClassPath Structure here.
		Collection<PathTask> c = getProjectClasspathStructure(pcs, relativeTo);
		if (c != null && !c.isEmpty())
		{
			pathTasks.addAll(c);
		}
		
		// Get User Library Structure here
		Set<UserLibrary> set =pcs.getCompileTimeUserLibraries();
		// If you don't find the library in the Project Compilation STructure
		// Then instead of returning, just continue with the next iteration.
		if (set == null || set.isEmpty())
		{
			return pathTasks;
		}
		for(UserLibrary ul : set )
		{
			IClasspathEntry ice []  = ul.getListOfJars();
			if (ice == null || ice.length ==0 )
			{
				continue;
			}
			
			// Library path generation nomenclature
			final String id = ul.getName() + "." + LIBRARY_CLASSPATH_SUFFIX;

			
			PathTask pt = new PathTask();
			pt.setId(id);
			
			// Set PathElement Here
			Collection<PathElement> pes = new ArrayList<PathElement>();
			for (IClasspathEntry ic : ice)
			{
				PathElement pe = new PathElement();						
				String path = getRelativePath(relativeTo, ic.getPath()).toString();
				pe.setLocation(path);
				pes.add(pe);									
			}
			pt.setPathElement(pes);
			pathTasks.add(pt);
		}
	
		return pathTasks;
	}
	/**
	 * This method is used to get all the compile time User Libraries Path.
	 * This method gets all the "User" libraries irrespective of the Project.
	 * @param pbs
	 */
	public static Collection<PathTask> getAllCompilationAntPathElements(final List<ProjectBuildStructure> pbs, final IProject srcIpr)
	{
		
		Collection<PathTask> pathTasks = new ArrayList<PathTask>();
		
		// Following MapEntry is used to preserve the User Library entry that has already been converted to PathTask
		// Ideally PathTask should have been implemented in a better way (propery, hashcoding and the equals);
		Map<String, Integer> mp = new HashMap<String, Integer>();
//		Map<String, Set<String>> mapLibrary = null;
		if (pbs == null || pbs.isEmpty())
			throw new IllegalArgumentException("Project Build Structure is empty. Cannot find Path Elements");
		
		// Iterate over the ProjectBuildStructure
		for (ProjectBuildStructure p : pbs)
		{
			// Get a ProjectCompilationStructure instance.
			final ProjectCompilationStructure pcs = p.getPcs();
			
//			getPackagingStructure(pds, pcs, srcIpr);
			
			if (pcs != null)
			{
				Collection<PathTask> c = getProjectClasspathStructure(pcs, srcIpr);
				// GET Individual ClassPath Structure here. 
				if (c !=null && !c.isEmpty())
				{
					pathTasks.addAll(c);
				}
				
				// Get User Library Structure here
				Set<UserLibrary> set =pcs.getCompileTimeUserLibraries();
				// If you don't find the library in the Project Compilation STructure
				// Then instead of returning, just continue with the next iteration.
				if (set == null || set.isEmpty())
				{
					continue;
				}
				for(UserLibrary ul : set )
				{
					IClasspathEntry ice []  = ul.getListOfJars();
					if (ice == null || ice.length ==0 )
					{
						continue;
					}
					
					final String id = ul.getName() + "." + LIBRARY_CLASSPATH_SUFFIX;
					
					Integer i = mp.get(id);
					if (i != null)
					{
						continue;
					}
					else
						mp.put(id,  1);
					
					PathTask pt = new PathTask();
					pt.setId(id);
					
					// Set PathElement Here
					Collection<PathElement> pes = new ArrayList<PathElement>();
					for (IClasspathEntry ic : ice)
					{
						PathElement pe = new PathElement();						
						String path = getRelativePath(srcIpr, ic.getPath()).toString();
						pe.setLocation(path);
						pes.add(pe);									
					}
					pt.setPathElement(pes);
					pathTasks.add(pt);
				}
			}
			else 
			{
				//TODO: log PCS is null;
			}
		}
		return pathTasks;
	}
	
	private static Collection<PathTask> getProjectClasspathStructure(final ProjectCompilationStructure pcs, final IProject srcIpr)
	{
		
		IClasspathEntry ices []  = pcs.getCompileTimeLibraries();
		if (ices == null || ices.length == 0)
		{
			return null;
		}
		final String classpathname = pcs.getiProject().getName() + "." + PROJECT_CLASSPATH_SUFFIX;
		
		PathTask pt = new PathTask();
		pt.setId(classpathname);
		Collection<PathTask> colPathTask = new ArrayList<PathTask>();
		
		Collection<PathElement> pes = new ArrayList<PathElement>();
		
		
		PathElement pe = new PathElement();
		// Setting PathElement for the output location.
		String loc = ".."+ pcs.getOutputLocation().toString();
		pe.setLocation(loc.startsWith("/")?".."+loc:loc);
		pes.add(pe);
		
		for (IClasspathEntry ice : ices)
		{
			final int i = ice.getEntryKind();
			// We are ignoring the CPE_CONTAINER, CPE_SRC
			
			if ( i == IClasspathEntry.CPE_SOURCE )
			{
				continue;
			}
			if ( i == IClasspathEntry.CPE_CONTAINER)
			{
				loc = getRelativePath(srcIpr,JavaCore.getResolvedClasspathEntry(ice).getPath()).toString();
				// Skip User libraries.
				if (loc.contains("org.eclipse.jdt.USER_LIBRARY"))
					continue;
				try {
					
					IClasspathContainer icc = JavaCore.getClasspathContainer(ice.getPath(), JavaCore.create(srcIpr));
					if (icc.getKind() == IClasspathContainer.K_APPLICATION )
					{						
						
//						final String libraryPath = icc.getDescription()+ "." + pcs.getiProject().getName()+"."+LIBRARY_CLASSPATH_SUFFIX;
						final String libraryPath = icc.getDescription()+ "."+LIBRARY_CLASSPATH_SUFFIX;
						PathTask conPathTask = null;
						if(!classPathLibraries.contains(libraryPath))
						{
							conPathTask = new PathTask();
							conPathTask.setId(libraryPath);
							
							IClasspathEntry ies[] = icc.getClasspathEntries();
							if( ies !=null && ies.length >0 )
							{
								for( IClasspathEntry ie : ies)
								{
									PathElement p = new PathElement();
									IPath ip = ie.getPath();
									File f = new File(ip.toString());
									if (f.exists())
									{
										ip = BuildCore.getRelativePath(srcIpr, ip);
										p.setLocation(ip.toString());
									}
									else
									{
										p.setLocation(".."+ip);
									}
									conPathTask.addPathElement(p);
								}
							}
							else
							{
								conPathTask.setPathElement(null);
							}
							colPathTask.add(conPathTask);
							classPathLibraries.add(libraryPath);
						}
						conPathTask = new PathTask();
						conPathTask.setRefid(libraryPath);
						pt.addPathTask(conPathTask);
					}
					
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			loc = null;
//			PathTask var_pathtask = null;
			if (i == IClasspathEntry.CPE_VARIABLE)
			{
//				loc = JavaCore.getResolvedClasspathEntry(ice).getPath().toString();
				loc = getRelativePath(srcIpr,JavaCore.getResolvedClasspathEntry(ice).getPath()).toString();
				// Don't uncomment the following code. We might need this one later.
				
				// We will have to check whether this is a Directory or a File.
				// If it is a directory, then we have to generate the FileSet entry
				// If it is a file, then set up the location.
				
////				File f = new File(loc);
////				if (f.exists())
////				{
////					if (f.isDirectory())
////					{
////						var_pathtask = new PathTask();
////						FileSet fs = new FileSet();
////						fs.setDir(loc);
////						Include include = new Include();
////						include.setName("**/*.jar");
////						Collection<Include> colInclude = new ArrayList<Include>(1);
////						colInclude.add(include);
////						fs.setInclude(colInclude);
////						Collection<FileSet> colFileset = new ArrayList<FileSet>(1);
////						colFileset.add(fs);
////						var_pathtask.setFileset(colFileset);
////						Collection<PathTask> innerPaths = new ArrayList<PathTask>();
////						innerPaths.add(var_pathtask);
////						Collection<PathTask> colp = pt.getClasspath();
////						if (colp == null)
////							pt.setClasspath(innerPaths);
////						else
////						{
////							colp.addAll(innerPaths);
////							pt.setClasspath(colp);
////						}
////						
////					}
////					else
////					{
////						
////					}
//				}
				pe = new PathElement();
				//pe.setLocation(loc.startsWith("/")?".."+loc:loc);
				pe.setLocation("${"+ice.getPath()+"}");
								
			}
			else  if (i == IClasspathEntry.CPE_PROJECT)
			{
				IPath ip = ice.getPath();
				IProject ipr = ResourcesPlugin.getWorkspace().getRoot().getProject(ip.makeRelative().toString());
				
				if(ipr.isAccessible())
				{
					IJavaProject ijp = JavaCore.create(ipr);
					try {
						IPath ip2 = ijp.getOutputLocation();
//						IPath outputLocationAbsolutePath = new Path(ResourcesPlugin.getWorkspace().getRoot().getLocation() + ip2.toString());
						//loc = getRelativePath(srcIpr, outputLocationAbsolutePath).toString();
						loc ="../"+ip2.toString();
						pe = new PathElement();
						pe.setLocation(loc);
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			else
			{
				pe = new PathElement();
				loc =  ice.getPath().toString();
				//loc = ice.getPath().toString();
				pe.setLocation(loc.startsWith("/")?(".."+loc):loc);
			}
			
			pes.add(pe);
		}
		
		// Set RefId for the Path.
		Set<UserLibrary> set = pcs.getCompileTimeUserLibraries();
		if (set != null &&  !set.isEmpty())
		{
			Collection<PathTask> cpt = new ArrayList<PathTask>();
			for (UserLibrary ul : set )
			{
				PathTask p = new PathTask();
				p.setRefid(ul.getName()+"."+ LIBRARY_CLASSPATH_SUFFIX);
				cpt.add(p);
			}
			
			if (pt.getClasspath() == null)
				pt.setClasspath(cpt);
			else
			{
				Collection<PathTask> temp =  pt.getClasspath();
				temp.addAll(cpt);
				pt.setClasspath(temp);
			}
		}
		pt.setPathElement(pes);
		colPathTask.add(pt);
		return colPathTask;
	}
	
	
	
	
	
	/**
	 * This method is used to marshall the Project Structure.
	 * @param project - An instance of {@linkplain Project}
	 * @return - String output after the marshalling.
	 */
	public static String marshallAntStructure(Project project)
	{
		String xmlContent = null;
		try
		{
			JAXBContext context = JAXBContext.newInstance(Project.class);
			StringWriter  writer = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(project, writer);
			xmlContent = writer.toString();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return xmlContent;
	}
	
	/**
	 * This method is used to returns the relative paths.
	 * e.g. 
	 * relativeTo - /home/test/projectpath/location
	 * toBeConverted - /home/test/newPath/location
	 * The path converted would be ../../newPath/location
	 * @param relativeTo - Source path relative to which the path needs to be converted.
	 * @param toBeConverted - Project whose path is to be converted to.
	 * @return Returns and instance of IPath.
	 */
	public static IPath getRelativePath (final IPath relativeTo, final IProject toBeConverted)
	{
		if (relativeTo == null || toBeConverted == null 
				)
			throw new IllegalArgumentException("Either Path or Project Variable is not present");
		return toBeConverted.getLocation().makeRelativeTo(relativeTo);			
	}
	
	/**
	 * This method is used to returns the relative paths.
	 * e.g. 
	 * relativeTo - /home/test/projectpath/location
	 * toBeConverted - /home/test/newPath/location
	 * The path converted would be ../../newPath/location
	 * @param relativeTo - Source project relative to which the path needs to be converted.
	 * @param toBeConverted - Project Path to be converted to.
	 * @return Returns and instance of IPath.
	 */
	
	public static IPath getRelativePath (final IProject relativeTo, final IPath toBeConverted)
	{
		if (relativeTo == null || toBeConverted == null 
				)
			throw new IllegalArgumentException("Either Path or Project Variable is not present");
		return toBeConverted.makeRelativeTo(relativeTo.getLocation());
	}
	
	/**
	 * This method is used to returns the relative paths.
	 * e.g. 
	 * relativeTo - /home/test/projectpath/location
	 * toBeConverted - /home/test/newPath/location
	 * The path converted would be ../../newPath/location
	 * @param relativeTo - Source project path relative to which the path needs to be converted.
	 * @param toBeConverted - Project whose path is to be converted to.
	 * @return Returns and instance of IPath.
	 */
	public static IPath getRelativePath (final IProject relativeTo, final IProject toBeConverted)
	{
		if (relativeTo == null || toBeConverted == null)
			throw new IllegalArgumentException("Either Project Input variable is empty");
		
		return toBeConverted.getLocation().makeRelativeTo(relativeTo.getLocation());		
	}
	
	/**
	 * This method is used to returns the relative paths.
	 * e.g. 
	 * relativeTo - /home/test/projectpath/location
	 * toBeConverted - /home/test/newPath/location
	 * The path converted would be ../../newPath/location
	 * @param relativeTo - Source path relative to which the path needs to be converted.
	 * @param toBeConverted - Path to be converted to.
	 * @return Returns and instance of IPath.
	 */
	
	public static IPath getRelativePath(final IPath relativeTo, final IPath toBeConverted)
	{
		if (relativeTo == null || toBeConverted == null )
			throw new IllegalArgumentException("Either Input Variable is empty");
		return toBeConverted.makeRelativeTo(relativeTo);
	}

	/**
	 * This method returns absolute location of the workspace. this is a single threaded method.
	 * @return - Returns and instance of IPath.
	 */

	public static IPath getWorkspaceLocation()
	{
		if (workspaceLocation == null)
		{
			workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		}
		return workspaceLocation;
	}
}
