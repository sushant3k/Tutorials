/**
 * This is the abstract base class for the generation of build for different types of projects.
 * this class is used to generate all the different compilation tasks, archival tasks.
 *  
 */
package com.learning.build.generator.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.componentcore.J2EEModuleVirtualArchiveComponent;
import org.eclipse.jst.j2ee.componentcore.J2EEModuleVirtualComponent;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.GenericUtilitiesImpl;
import com.learning.ant.build.interfaces.IGenericUtilities;
import com.learning.ant.build.interfaces.IProjectUtilities;
import com.learning.ant.build.interfaces.ProjectUtilitiesImpl;
import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.ant.build.structure.AbstractPattern;
import com.learning.ant.build.structure.ExclusionPattern;
import com.learning.ant.build.structure.InclusionPattern;
import com.learning.ant.build.structure.ProjectBuildStructure;
import com.learning.ant.build.structure.ProjectCompilationStructure;
import com.learning.ant.build.structure.ProjectDeploymentStructure;
import com.learning.ant.build.structure.UserLibrary;
import com.learning.ant.build.structure.ProjectDeploymentStructure.Libraries;
import com.learning.ant.build.structure.ProjectDeploymentStructure.Projects;
import com.learning.ant.build.utils.BuildCore;
import com.learning.ant.build.xml.structure.BuildTarget;
import com.learning.ant.build.xml.structure.ClasspathElement;
import com.learning.ant.build.xml.structure.CopyTask;
import com.learning.ant.build.xml.structure.DeleteTask;
import com.learning.ant.build.xml.structure.Exclude;
import com.learning.ant.build.xml.structure.FileSet;
import com.learning.ant.build.xml.structure.Include;
import com.learning.ant.build.xml.structure.JarTask;
import com.learning.ant.build.xml.structure.JavaSource;
import com.learning.ant.build.xml.structure.JavacTask;
import com.learning.ant.build.xml.structure.Project;
import com.learning.build.constants.Constants;

/**
 * @author Sushant Jain
 * @since 1.0
 * @version 1.0
 *
 */
@SuppressWarnings("restriction")
public abstract class BaseBuildGeneratorEngine implements IBuildGeneratorEngine{

	/**
	 * This method is used for packaging of archives. This method is not utilized in the EAR packaging.
	 * 
	 */
	public abstract Project packageArchive(Projects proj, IProject relativeTo);
	
	/**
	 * This method is used for packaging of archives. This method is only used for packaging of EARs. 
	 * 
	 */
	public abstract Project packageArchive(IProject iproj, IProject relativeTo);
	protected abstract Project generateArchive(ProjectDeploymentStructure pds, IProject ipr, IProject relativeTo);
	
	
	public abstract ProjectBuildStructure generateBuild(IProject ipr);
	protected abstract List<BuildTarget> generateArchive(final ProjectBuildStructure pbs, final IProject srcIpr);
	
//	protected ProjectType prjType;
//	protected IProject ipr;
	
	protected static final IProjectUtilities ipu = new ProjectUtilitiesImpl();
	
//	public abstract BuildTarget getCompilationTargets(ProjectCompilationStructure pcs);
	
	protected Collection<BuildTarget> cbt = null;
	
	List<ProjectBuildStructure> lstPbs = null;

	protected ProjectBuildStructure pbs = null;


	protected JarTask getJarTask(IProject ipr, IProject relativeTo, final ProjectCompilationStructure pcs)
	{
		JarTask jt = new JarTask();
		jt.setBasedir("${basedir}");
		jt.setDestfile("${ant.project.name}.ear");
		
		FileSet fs = new FileSet();
		String fileSetLocation = BuildCore.getRelativePath(relativeTo, ipr).toString();
		
		
		if(fileSetLocation == null || fileSetLocation.isEmpty())
		{
			fs.setDir(".."+ ipr.getFullPath().toString());
		}
		else
			fs.setDir(fileSetLocation);
		
		
		
		Collection<Exclude> cexcl = new ArrayList<Exclude>();			
		Exclude exc = new Exclude();
		exc.setName("**/.project");
		cexcl.add(exc);
		
		exc = new Exclude();
		exc.setName("**/.classpath");
		cexcl.add(exc);
		
		exc = new Exclude();
		exc.setName("build.xml");
		cexcl.add(exc);
		
		List<IPackageFragmentRoot> lip = pcs.getSourceFolders();
		
		if(lip !=null && !lip.isEmpty())
		{			
			for(IPackageFragmentRoot ipfr: lip)
			{
				exc = new Exclude();
				exc.setName(ipfr.getElementName()+"/**");
				cexcl.add(exc);				
			}
		}
		exc = new Exclude();
		exc.setName(".settings/**");
		cexcl.add(exc);
		
		exc = new Exclude();
		exc.setName("build/**");
		cexcl.add(exc);
		
		exc = new Exclude();
		exc.setName(BuildCore.getRelativePath(ipr.getLocation(),getAbsoluteLocationWithProject(ipr, pcs.getOutputLocation())).toString()+"/**");
		cexcl.add(exc);
		fs.setExclude(cexcl);
		
		Collection<FileSet> fss = new ArrayList<FileSet>(1);
		fss.add(fs);
		jt.setFileset(fss);			
		return jt;
		
	}
	
	protected Set<IProject> getAllAccessibleEARProjectsInWorkspace() throws ApplicationException
	{		
		return ipu.getAllAccessibleEARProjectsInWorkspace();
	}

	protected IPath getAbsoluteLocationWithProject(IProject ipr, IPath ip)
	{
		String projectName = ipr.getName();
		String projectLocation = ipr.getLocation().toString();
		
		int index = projectLocation.lastIndexOf(projectName);
		int indexofProjectinpath = ip.toString().indexOf(projectName);
		if (indexofProjectinpath != -1 )
		{
			projectLocation = projectLocation.substring(0,index -1);
		}		
		
		IPath outputLocation = new Path( projectLocation + ip.toString());
		return outputLocation;
	}
	/**
	 * Method to retrieve clean targets.
	 */	
	protected BuildTarget getCleanTarget(final ProjectCompilationStructure pcs, 
			final IProject relativeTo, final Projects pds)
	{
		
		BuildTarget bt = new BuildTarget();
		
		String projectName = pcs.getiProject().getName();
		String projectLocation = pcs.getiProject().getLocation().toString();
		projectLocation = projectLocation.substring(0,projectLocation.lastIndexOf(projectName)-1);
		
//		IPath outputLocation = new Path( projectLocation + pcs.getOutputLocation().toString());
		IPath outputLocation = getAbsoluteLocationWithProject(pcs.getiProject(),pcs.getOutputLocation());
//		System.out.println(outputLocation2.toString());
		IPath ip2 = BuildCore.getRelativePath(relativeTo, outputLocation);
		bt.setName(Constants.CLEAN_TARGET_PREFIX+pcs.getiProject().getName());
		DeleteTask dt = new DeleteTask();
		final String dirName = ip2.toString();
		
		Collection<FileSet> cfs = new ArrayList<FileSet>(1);
		FileSet fs = new FileSet();
		fs.setDir(dirName);
		Include include = new Include();
		include.setName("**/*");
		fs.addIncludeToCollection(include);
		cfs.add(fs);
		dt.setFileSet(cfs);
		dt.setIncludeEmptyDirectories(true);
		bt.addDeleteTaskToCollection(dt);
		
		if (pds !=null)
		{
			dt = new DeleteTask();
			dt.setFile(pds.getArchiveName());
			bt.addDeleteTaskToCollection(dt);
		}
		else
		{
			IProjectUtilities ipu = new ProjectUtilitiesImpl();
			IProject ipr = pcs.getiProject();
			try {
				ProjectType pt = ipu.getProjectType(ipr);
				String extension = "";
				if(pt.equals(ProjectType.EAR))
				{
					extension=".ear";
				}
				if(pt.equals(ProjectType.EJB) || pt.equals(ProjectType.JAVA))
				{
					extension=".jar";
				}
				if(pt.equals(ProjectType.WEB))
				{
					extension=".war";
				}
				if(pt.equals(ProjectType.SAR))
				{
					extension=".sar";
				}
				dt = new DeleteTask();
				dt.setFile(ipr.getName()+extension);
				bt.addDeleteTaskToCollection(dt);
				
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}
			
		}
		
		return bt;
	}
	
	
	
	protected BuildTarget getCommonCompilationTargets(ProjectCompilationStructure pcs, IProject relativeTo)
	{
			
		IProject ipr = pcs.getiProject();
		
		// Get All the Referenced Projects here
		Set <IProject> set = pcs.getReferencedProjects();
		
		BuildTarget bt = new BuildTarget();
		bt.setName(Constants.COMPILE_TARGET_PREFIX+ipr.getName());
		
		
		if (set !=null && !set.isEmpty())
		{
			
			StringBuilder sbTemp = new StringBuilder();
			
			for (IProject i : set)
			{
				sbTemp.append(Constants.COMPILE_TARGET_PREFIX).append(i.getName()).append(",");
				
			}
			bt.setDepends(sbTemp.toString() + Constants.CLEAN_TARGET_PREFIX+ipr.getName());						
		}
		else
		{
			bt.setDepends(Constants.CLEAN_TARGET_PREFIX+ipr.getName());
		}
		// Get Source Folders to Compile
		List<IPackageFragmentRoot> lst = pcs.getSourceFolders();
		if (lst !=null && !lst.isEmpty())
		{
			
			
			String outputLocation = "../"+BuildCore.getRelativePath(relativeTo,ipr).toString() + "/" + "bin";
			if (pcs.getOutputLocation() != null)
			{
				outputLocation = ".."+pcs.getOutputLocation().toString();
			}
			
			ClasspathElement cpe = new ClasspathElement();
			cpe.setRefid(ipr.getName()+"." + BuildCore.PROJECT_CLASSPATH_SUFFIX);
			Collection<ClasspathElement> cpes = new ArrayList<ClasspathElement>();
			cpes.add(cpe);
			
			// Setting Javac Task
			Collection<JavacTask> ct = null;
			for (IPackageFragmentRoot ipfr: lst)
			{
				JavaSource j = new JavaSource();				
				String srcPath = ipfr.getPath().toString();
				j.setPath(  ".."+ srcPath );
				
				JavacTask jt = new JavacTask();
				addExclusionInclusionPatterns(jt, srcPath,  pcs);
				
				jt.setClasspath(cpes);
				jt.setSrc(j);			
				jt.setDestdir(outputLocation);
				if (ct == null)
				{
					ct = new ArrayList<JavacTask>();
				}
				ct.add(jt);
				
			}
			
			bt.setCompilationTask(ct);
		}
		
		
		// Also, get the build target
		
		
		return bt;
	}
	
	private void addExclusionInclusionPatterns(JavacTask jt, String javaSource, ProjectCompilationStructure pcs)
	{

		Hashtable<String, List<Set<AbstractPattern>>> ht = pcs.getPattern();
		
		if (null == ht || ht.isEmpty())
			return;
		
		List<Set<AbstractPattern>> ls = ht.get(".."+javaSource);
		if (null == ls || ls.isEmpty())
			return;
		Collection<Exclude> colEx = null;
		Collection<Include> colInc = null;
		for(Set<AbstractPattern> s: ls)
		{
			for(AbstractPattern ap : s)
			{
				if (ap instanceof ExclusionPattern)
				{
					if (colEx == null)
					{
						colEx = new ArrayList<Exclude>();
					}
					Exclude exc = new Exclude();
					exc.setName(ap.getPattern());
					colEx.add(exc);
				}
				if (ap instanceof InclusionPattern)
				{

					if (colInc == null)
					{
						colInc = new ArrayList<Include>();
					}
					Include inc = new Include();
					inc.setName(ap.getPattern());
					colInc.add(inc);
				}
			}
		}
		jt.addToExcludeList(colEx);
		jt.addToIncludeList(colInc);
					
					
	}
	protected Map <String, IPath> getClasspathVariables()
	{
		//////////////////GOT CLASSPATH Variables with values //////////////////
		IGenericUtilities igu = new GenericUtilitiesImpl();
		Map <String, IPath> mpCPVars = igu.getRelevantClassPathVariables();
		return mpCPVars;
	}
	protected ProjectCompilationStructure generateCompilationOptions(final IProject ipr)
	{
		// Set up Project Compilation Structure
		ProjectCompilationStructure pcs = new ProjectCompilationStructure();
		
		////////////////////// GET All referenced Projects ///////////////////////
		Set<IProject> set = null;
		
		try {
			 set = ipu.getAllReferencedProjects(ipr);
		} catch (ApplicationException e) {

			System.err.println(e.getMessage());
		}

		
		/////////// GET User Libraries //////////////////

		Set <UserLibrary> setUL = null;
		try {
			setUL = ipu.getUserLibraries(ipr);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		
		// ///////////// GET ClassPath Entries ////////////
		try {
			IClasspathEntry ices[] = ipu.getProjectClassPath(ipr);
			pcs.setCompileTimeLibraries(ices);
			getExclusionAndInclusionPatterns(ices, pcs); //TODO:
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		
		////////////// Find Project compilation Physical output location. ////////////
		IPath ip =  null;
		try {
			 ip = ipu.getProjectCompilationOutputLocation(ipr);
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		
		pcs.setReferencedProjects(set);
		pcs.setiProject(ipr);

		pcs.setCompileTimeUserLibraries(setUL);
		if (ip !=null)
		{
			pcs.setOutputLocation(ip.makeAbsolute());
		}
		
		// GET Source Paths
		
		IJavaProject ijp = JavaCore.create(ipr);
				
		List<IPackageFragmentRoot> lst = null;
		
		try {
			IPackageFragmentRoot ipfr[] = ijp.getPackageFragmentRoots();
			for (IPackageFragmentRoot ipf : ipfr)
			{
				if ( IPackageFragmentRoot.K_SOURCE == ipf.getKind() )
				{
					if (lst == null)
						lst = new ArrayList<IPackageFragmentRoot>();
					lst.add(ipf);					
				}				
				pcs.setSourceFolders(lst);
			}
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return pcs;
	}

	/**
	 * Method to copy libraries from Deployment Assembly to the Runtime path.
	 * @param pds {@linkplain ProjectDeploymentStructure}
	 * @param ipr {@linkplain IProject}
	 * @return - Returns a collection of CopyTasks {@link CopyTask}
	 */
	
	protected Collection<CopyTask> copyLibrariesFromDeploymentAssemblyToRunTimePath(ProjectDeploymentStructure pds, IProject ipr)
	{
		List<Libraries> libs = pds.getLibs();
		Collection<CopyTask> copyTasks = null;
		if (libs != null && !libs.isEmpty())
		{
			 copyTasks = new ArrayList<CopyTask>();
			for (Libraries lib : libs)
			{
				CopyTask ct = new CopyTask();
				
				IPath ip = lib.getArchivePath();
				
				ct.setFile(ip.toString());
				String dirPath = pds.getRootFolder().toString()+lib.getIruntimePath().toString();
				ct.setToDir(dirPath.startsWith("/")?".."+dirPath:dirPath);
				copyTasks.add(ct);
			}			
		}
		List<Projects> lst = pds.getProjects();
		if(lst !=null )
		{
			for (Projects project : lst)
			{
				CopyTask ct = new CopyTask();
				ct.setFile(BuildCore.getRelativePath(
						new Path(BuildCore.getWorkspaceLocation().toString()+"/"+ project.getDeployedName()), ipr.getLocation()).toString()
						+
						new Path(project.getArchiveName()).makeAbsolute());
				String dirPath = BuildCore.getRelativePath(new Path(BuildCore.getWorkspaceLocation().toString() + "/" + project.getDeployedName()),
						new Path(BuildCore.getWorkspaceLocation().toString() +pds.getRootFolder().toString()  +project.getIruntimePath().toString())).toString();
				ct.setToDir(dirPath);
				copyTasks.add(ct);
			}
		}
		return copyTasks;
	}

	/**
	 * The idea here is to read the deployment assembly to find all the Projects and 
	 * Libraries. 
	 * 
	 */

	protected ProjectDeploymentStructure readDeploymentAssembly(final IProject ipr) {
		
		IVirtualComponent iVirtualComponent = 
				ComponentCore.createComponent(ipr);
		
		if (iVirtualComponent == null || !iVirtualComponent.exists())
			return null;
		
		ProjectDeploymentStructure pds = new ProjectDeploymentStructure();
		
		IPath ipc = iVirtualComponent.getRootFolder().getWorkspaceRelativePath();
		IVirtualReference[]  ivrs = iVirtualComponent.getReferences();
		
		pds.setMetaProperties(iVirtualComponent.getMetaProperties());
		pds.setMetaProperties(iVirtualComponent.getMetaProperties());
		pds.setRootFolder(ipc);
		pds.setRootFolderRuntimePath(iVirtualComponent.getRootFolder().getRuntimePath());
		
		for(IVirtualReference ivr : ivrs)
		{
			
			pds.setArchiveName(ivr.getArchiveName());
			IVirtualComponent iv = ivr.getReferencedComponent();
			
			
			if (iv instanceof J2EEModuleVirtualArchiveComponent)
			{
				J2EEModuleVirtualArchiveComponent j = (J2EEModuleVirtualArchiveComponent)iv;
				Libraries libs =  pds.getLibrariesNewInstance();
				libs.setRuntimePath(ivr.getRuntimePath().toString());
				libs.setIruntimePath(ivr.getRuntimePath());
				libs.setName(iv.getName());
				libs.setIvc(j);
				
				if (j.getArchiveType().equalsIgnoreCase("var"))
				{
					libs.setArchivePath(JavaCore.getResolvedVariablePath(j.getArchivePath()));
					
				}
				else
				{
					String filePath = j.getArchivePath().toString();
					File f = new File(filePath);
					if(f.exists())
					{
						libs.setArchivePath(j.getArchivePath());
					}
					else
					{
						
//						IPath ip = new Path(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()
//								+j.getArchivePath().makeAbsolute());
						IPath ip = new Path(".." + j.getArchivePath().makeAbsolute());
						libs.setArchivePath(ip);
					}
				}
				
				pds.addLibrary(libs);
			}
			else if (iv instanceof J2EEModuleVirtualComponent )
			{
				// then its a Project
				J2EEModuleVirtualComponent j = (J2EEModuleVirtualComponent)iv;
				IProject i = iv.getProject();
				if (!i.isAccessible())
				{
					continue;
				}
				Projects projs = pds.getProjectsNewInstance();
				
				projs.setRuntimePath(ivr.getRuntimePath().toString());
				projs.setIruntimePath(ivr.getRuntimePath());
				projs.setName(iv.getName());
				projs.setDeployedName(iv.getDeployedName());
				projs.setIvc(j);
				projs.setiProject(i);
				projs.setArchiveName(ivr.getArchiveName());
				pds.addProject(projs);
			}
			else if (iv instanceof VirtualComponent)
			{
				VirtualComponent j = (VirtualComponent)iv;
				IProject i = iv.getProject();
				if (!i.isAccessible())
				{
					continue;
				}
				Projects projs = pds.getProjectsNewInstance();
				
				projs.setRuntimePath(ivr.getRuntimePath().toString());
				projs.setIruntimePath(ivr.getRuntimePath());
				projs.setName(iv.getName());
				projs.setDeployedName(iv.getDeployedName());
				projs.setIvc(j);
				projs.setiProject(i);
				projs.setArchiveName(ivr.getArchiveName());
				pds.addProject(projs);
			}
			else
			{
				throw new RuntimeException("Only jars and the Project References are supported in the Deployment assembly");
			}
			
		}
		return pds;
	}


	private boolean isReferencedProjectPresentInDeploymentAssembly(IProject ipr, List<Projects> projects)
	{
		
		if(projects == null || projects.isEmpty())
			return false;
			 
		for (Projects proj : projects)
		{
			IProject ipr2 = proj.getiProject();
			if(ipr2.getName().equals(ipr.getName()))
			{
				return true;
			}
		}
		return false;
			
	}
	protected Project getPostProcessCompilationTasks(IProject relativeTo)
	{
		Project project = null;
		ProjectCompilationStructure pcs = this.pbs.getPcs();
		ProjectDeploymentStructure pds = this.pbs.getPds();
		Set<IProject> setReferencedProjects = pcs.getReferencedProjects();
			
		if (setReferencedProjects == null )
			return null;
		
		
		List<Projects> lstDeploymentAssemblyProjects = pds.getProjects();
		
		for(IProject ipr: setReferencedProjects)
		{
			
			if(!isReferencedProjectPresentInDeploymentAssembly(ipr,lstDeploymentAssemblyProjects))
			{
				if (!BuildCore.postProcessCompilationTasks.contains(ipr.getName()))
				{
					if (project == null)
					{
						project = new Project();
					}
					IProjectUtilities ipu = new ProjectUtilitiesImpl();
					IBuildGeneratorFactory ibf = new BuildGeneratorFactoryImpl();
					BaseBuildGeneratorEngine ibe = null;
					try {
						ProjectType pt= ipu.getProjectType(ipr);
						ibe = ibf.getInstance(pt);
						Project proj = ibe.getPostProcessCompilationTasks(relativeTo, ipr);
						if (proj !=null)
						{
							project.addClasspath(proj.getClasspath());
							project.addBuildTargets(proj.getTarget());
						}
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					BuildCore.postProcessCompilationTasks.add(ipr.getName());
				}
			}
		}
		
		return project;
	}


	protected Collection<DeleteTask> deleteTaskForCopiedLibraries(ProjectDeploymentStructure pds, IProject ipr)
	{
		List<Libraries> libs = pds.getLibs();
		Collection<DeleteTask> tasks = null;
		if (libs != null && !libs.isEmpty())
		{
			 tasks = new ArrayList<DeleteTask>();
			for (Libraries lib : libs)
			{
				DeleteTask ct = new DeleteTask();
				
				IPath ip = BuildCore.getRelativePath(ipr, lib.getArchivePath());
				String fileName = ip.toString().substring(ip.toString().lastIndexOf("/")+1);
				String dirPath = ".."+ pds.getRootFolder().toString() 
								+ lib.getIruntimePath().toString();					
				ct.setFile(dirPath.endsWith("/")?dirPath + fileName:(dirPath +"/" +fileName));
				tasks.add(ct);
			}			
		}
		
		List<Projects> lst = pds.getProjects();
		if(lst !=null )
		{
			for (Projects project : lst)
			{
				DeleteTask ct = new DeleteTask();
				ct.setFile(BuildCore.getRelativePath(
						new Path(BuildCore.getWorkspaceLocation().toString()+"/"+ project.getDeployedName()), ipr.getLocation()).toString()
						+
						new Path(project.getArchiveName()).makeAbsolute());
				tasks.add(ct);
			}
		}
		
		return tasks;
	}
	
	private void getExclusionAndInclusionPatterns(IClasspathEntry ices[], ProjectCompilationStructure pcs)
	{
		if(ices == null || ices.length == 0)
		{
			return;
		}
		for(IClasspathEntry ice : ices)
		{
			// Optimizing it for Source only.
			if (ice.getEntryKind() == IClasspathEntry.CPE_SOURCE)
			{
				Set<AbstractPattern> setExclusion = null;
				Set<AbstractPattern> setInclusion = null;
				
				IPath[] ipaths = ice.getExclusionPatterns();
				if(ipaths !=null && ipaths.length > 0)
				{
					 setExclusion = new HashSet<AbstractPattern>(ipaths.length);
					for(IPath ip :ipaths)
					{
						AbstractPattern ep = new ExclusionPattern(ip.toString());
						setExclusion.add(ep);
					}
				}
				// Get the inclusion patterns.
				ipaths = ice.getInclusionPatterns();
				if(ipaths !=null && ipaths.length > 0)
				{
					setInclusion = new HashSet<AbstractPattern>(ipaths.length);
					for(IPath ip :ipaths)
					{
						AbstractPattern ep = new ExclusionPattern(ip.toString());
						setInclusion.add(ep);
					}
				}
				String s = ".."+ice.getPath().toString();
				List<Set<AbstractPattern>> lst = null;
				if (setExclusion !=null && !setExclusion.isEmpty())
				{
					lst = new ArrayList<Set<AbstractPattern>>();
					lst.add(setExclusion);
				}
				if(setInclusion !=null && !setInclusion.isEmpty())
				{
					if (lst == null)
					{
						lst = new ArrayList<Set<AbstractPattern>>();
					}
					lst.add(setInclusion);
				}
				 
				pcs.addToExclusionInclusionPattern(s, lst);
			}
		}
	}
}
