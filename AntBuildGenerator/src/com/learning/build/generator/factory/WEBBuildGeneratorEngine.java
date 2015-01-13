/**
 * 
 */
package com.learning.build.generator.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.IProjectUtilities;
import com.learning.ant.build.interfaces.ProjectUtilitiesImpl;
import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.ant.build.structure.ProjectBuildStructure;
import com.learning.ant.build.structure.ProjectCompilationStructure;
import com.learning.ant.build.structure.ProjectDeploymentStructure;
import com.learning.ant.build.structure.ProjectDeploymentStructure.Projects;
import com.learning.ant.build.utils.BuildCore;
import com.learning.ant.build.xml.structure.BuildTarget;
import com.learning.ant.build.xml.structure.PathTask;
import com.learning.ant.build.xml.structure.Project;
import com.learning.ant.build.xml.structure.Property;
import com.learning.ant.build.xml.structure.WarTask;
import com.learning.ant.build.xml.structure.WarTask.ClassesDir;
import com.learning.ant.build.xml.structure.WarTask.Lib;
import com.learning.build.constants.Constants;

/**
 * @author Sushant
 *
 */
public class WEBBuildGeneratorEngine extends BaseBuildGeneratorEngine {
	Projects projects = null;

	public WEBBuildGeneratorEngine()
	{
		
		
	}
	@Override
	public ProjectBuildStructure generateBuild(IProject ipr) {
		
		ProjectCompilationStructure pcs= generateCompilationOptions(ipr);
		ProjectDeploymentStructure pds = readDeploymentAssembly(ipr);
		
		pbs = new ProjectBuildStructure(pcs, pds);
		
		return pbs;
		
	}
	

	@Override
	protected List<BuildTarget> generateArchive(ProjectBuildStructure pbs,
			IProject srcIpr) {
		throw new RuntimeException("This method is not supported in the Web build generation configuration");
	}
	@Override
	public Project packageArchive(Projects projs, IProject relativeTo) {
		Project proj = null;
		this.projects = projs;
		/**
		 * If the ProjectDeploymentStructure instance is null. Then it is clear that this archive doesn't need packaging of other archives.
		 * Hence, Only archive this Project.
		 * 
		 */
		ProjectDeploymentStructure pds = pbs.getPds();
		proj = generateArchive(pds, projs.getiProject(), relativeTo);
		
		return proj;
	}
	
	/**
	 * This is an undocumented method. this method is not supported.
	 */
	@Override
	public Project packageArchive(IProject ipr, IProject relativeTo) {
		//throw new RuntimeException("This method is not supported for Web based build configurations");
		Project proj = null;
		
		
		ProjectDeploymentStructure pds = readDeploymentAssembly(ipr);
		
		proj = generateArchive(pds, ipr, relativeTo);
		proj.setProperties(BuildCore.getProjectProperties(relativeTo.getLocation()));
		/**
		 * If the ProjectDeploymentStructure instance is null. Then it is clear that this archive doesn't need packaging of other archives.
		 * Hence, Only archive this Project.
		 * 
		 */
		
		return proj;
	}
	@Override
	protected Project generateArchive(ProjectDeploymentStructure pds,
			IProject ipr, IProject relativeTo) {
		Collection<BuildTarget> cbts =  null;
		
		Project proj = new Project();
		proj.setBasedir(".");
		proj.setProjectName(ipr.getName());
		
		StringBuilder sb = new StringBuilder(512);
		ProjectCompilationStructure pcs= generateCompilationOptions(ipr);
		final String compileTarget = Constants.COMPILE_TARGET_PREFIX + ipr.getName();
		final Collection<PathTask> cpt = BuildCore.getCompilationAntPathElements(pcs,relativeTo);
			
			List<Projects> projs = pds.getProjects();
			cbts = new ArrayList<BuildTarget>();
			BuildTarget bt = new BuildTarget();
			bt.setName(ipr.getName() + Constants.ARCHIVE_SUFFIX);
			/** Set up War task here **/
			WarTask wt = new WarTask();
			if (pcs !=null && pcs.getSourceFolders() !=null)
			{
				// Get compilation targets
				if( !BuildCore.postProcessCompilationTasks.contains(pcs.getiProject().getName()))
				{
					cbts.add(getCommonCompilationTargets(pcs, relativeTo));
					// Get Clean targets
					cbts.add(getCleanTarget(pcs, relativeTo,projects));
					BuildCore.postProcessCompilationTasks.add(pcs.getiProject().getName());
				}
				
				ClassesDir cd = new ClassesDir();
				cd.setDir(BuildCore.getRelativePath(relativeTo, getAbsoluteLocationWithProject(ipr, pcs.getOutputLocation())).toString());				
				wt.setClasses(cd);		
			}
			
			if (projects == null || projects.getArchiveName() == null || projects.getArchiveName().isEmpty())
			{
				wt.setDestfile(ipr.getName().toString()+".war");
			}
			else
			{
				wt.setDestfile(projects.getArchiveName());
			}
			final String rootFolder = BuildCore.getRelativePath(relativeTo, 
					getAbsoluteLocationWithProject(ipr, pds.getRootFolder())).toString();
			wt.setWebxml(rootFolder +"/WEB-INF/web.xml"); // TODO, get deployment descriptor location
			String libPath = getAbsoluteLocationWithProject(ipr, pds.getRootFolder()).toString()+"/WEB-INF/lib";
			File f = new File(libPath);
			if(f.exists() && f.isDirectory())
			{
				Lib lib = new Lib();
				lib.setDir(rootFolder+"/WEB-INF/lib");
				wt.setLib(lib); // TODO, we may not want to package everything here.
			}
			
			bt.setWt(wt);
			
			if (projs != null && !projs.isEmpty())
			{
				
				Project tmp = null;
				IProjectUtilities ipu = new ProjectUtilitiesImpl();
				IBuildGeneratorFactory ibf = new BuildGeneratorFactoryImpl();
				for (Projects pro : projs)
				{
					IProject ip = pro.getiProject();
					// check if already worked on the project or not.
					if (BuildCore.projectsWorkedOn.containsKey(ip.getName()))
					{
						continue;
					}
					
					BuildCore.projectsWorkedOn.put(ip.getName(), 1);
					ProjectType pt;
					try {
						pt = ipu.getProjectType(ip);
					} catch (ApplicationException e) {
						e.printStackTrace();
						continue;
					}
					BaseBuildGeneratorEngine be = ibf.getInstance(pt);	
					be.generateBuild(pro.getiProject());
					
					tmp = be.packageArchive(pro,relativeTo);// (pro.getiProject(),relativeTo);	
					
					if (tmp != null)
					{
						Collection<PathTask> cpts = tmp.getClasspath();
						Collection<Property> cprops = tmp.getProperties();
						Collection<BuildTarget> cbtrgts = tmp.getTarget();
						
						proj.addBuildTargets(cbtrgts);
						proj.addProperties(cprops);
						proj.addClasspath(cpts);
					}
					sb.append(pro.getiProject().getName() + Constants.ARCHIVE_SUFFIX + ",");
				}
			}
						
			// Chop the String.
			if (sb.toString() !=null && !sb.toString().isEmpty())
			{
				bt.setDepends(compileTarget + ", " + sb.toString().substring(0,sb.toString().length()-1));
			}
			else
			{
				bt.setDepends(compileTarget);
			}
			
			bt.setCopyTask(copyLibrariesFromDeploymentAssemblyToRunTimePath(pds,relativeTo));
			bt.setDeleteTask(deleteTaskForCopiedLibraries(pds,ipr));
			cbts.add(bt);			
		
		if (cpt != null && !cpt.isEmpty())
		{
			proj.addClasspath(cpt);
		}
		
		Project t = getPostProcessCompilationTasks(relativeTo);
		
		if(t !=null)
		{
			Collection<BuildTarget> cb = t.getTarget();
			if(cb != null && !cb.isEmpty())
			{
				cbts.addAll(cb);
			}
			Collection<PathTask> cpt2 = t.getClasspath();
			if (cpt2 !=null && !cpt2.isEmpty())
			{
				proj.addClasspath(cpt2);
			}
			
		}
		proj.addBuildTargets(cbts);
		
		return proj;
	}
	@Override
	public Project getPostProcessCompilationTasks(IProject relativeTo,
			IProject ipr) {
		Project project = new Project();
		ProjectCompilationStructure pc= generateCompilationOptions(ipr);
		Collection<BuildTarget> cbt = new ArrayList<BuildTarget>();
		Collection<PathTask> cpt2 = null;	
		BuildTarget bt = getCommonCompilationTargets(pc, relativeTo);
		cbt.add(bt);
		cbt.add(getCleanTarget(pc, relativeTo, null));	
		cpt2 = BuildCore.getCompilationAntPathElements(pc,relativeTo);
		project.addClasspath(cpt2);
		project.addBuildTargets(cbt);
		
		return project;
	}
	


}
