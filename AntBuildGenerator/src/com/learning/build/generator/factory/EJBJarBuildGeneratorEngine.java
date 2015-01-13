/**
 * 
 */
package com.learning.build.generator.factory;

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
import com.learning.ant.build.xml.structure.Exclude;
import com.learning.ant.build.xml.structure.FileSet;
import com.learning.ant.build.xml.structure.JarTask;
import com.learning.ant.build.xml.structure.PathTask;
import com.learning.ant.build.xml.structure.Project;
import com.learning.ant.build.xml.structure.Property;
import com.learning.build.constants.Constants;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
public class EJBJarBuildGeneratorEngine extends BaseBuildGeneratorEngine {

	Projects projects = null;
	public Project packageArchive(Projects projs, IProject relativeTo)
	{
		Project proj = null;
		this.projects = projs;
		
		ProjectDeploymentStructure pds = readDeploymentAssembly(projs.getiProject());
		proj = generateArchive(pds, projs.getiProject(), relativeTo);
		/**
		 * If the ProjectDeploymentStructure instance is null. Then it is clear that this archive doesn't need packaging of other archives.
		 * Hence, Only archive this Project.
		 * 
		 */
		
		return proj;
		
	}
	
	public Project packageArchive(IProject ipr, IProject relativeTo)
	{
		Project proj = null;
		
		
		ProjectDeploymentStructure pds = readDeploymentAssembly(ipr);
		proj = generateArchive(pds, ipr, relativeTo);
		/**
		 * If the ProjectDeploymentStructure instance is null. Then it is clear that this archive doesn't need packaging of other archives.
		 * Hence, Only archive this Project.
		 * 
		 */
		
		return proj;
		
	}
	
	
	protected Project generateArchive(ProjectDeploymentStructure pds, IProject ipr, IProject relativeTo)
	{
		Collection<BuildTarget> cbts =  null;
		
		Project proj = new Project();
		proj.setBasedir(".");
		proj.setDefaultTarget("build");
		proj.setProjectName(ipr.getName());
		
		StringBuilder sb = new StringBuilder(512);
		ProjectCompilationStructure pcs= generateCompilationOptions(ipr);
		final String compileTarget = "compile."+ipr.getName();
		Collection<PathTask> cpt = BuildCore.getCompilationAntPathElements(pcs,relativeTo);
			
		List<Projects> projs = pds.getProjects();
		cbts = new ArrayList<BuildTarget>();
		BuildTarget bt = new BuildTarget();
		bt.setName(ipr.getName() + Constants.ARCHIVE_SUFFIX);
		
		/** Getting Jar Task here **/
		JarTask jt = getJarTask(ipr, relativeTo,pcs);
		if (pcs != null && pcs.getSourceFolders() != null)
		{
			
			if( !BuildCore.postProcessCompilationTasks.contains(pcs.getiProject().getName()))
			{
				cbts.add(getCommonCompilationTargets(pcs, relativeTo));
				// Get Clean targets
				cbts.add(getCleanTarget(pcs, relativeTo,projects));
				BuildCore.postProcessCompilationTasks.add(pcs.getiProject().getName());
			}
			
			FileSet fs = new FileSet();
			fs.setDir(".."+pcs.getOutputLocation().toString());
			
			jt.addFileSet(fs);
		}
		
		// Set up ejbModule directory structure here
		FileSet fs2 = new FileSet();
		fs2.setDir(".."+ pds.getRootFolder().toString());
		Collection<Exclude> ces = new ArrayList<Exclude>();
		Exclude ex = new Exclude();
		ex.setName("**/*.java");
		ces.add(ex);
		fs2.setExclude(ces);
		
		jt.addFileSet(fs2);
		
		
		// Set up output location structure here.
		jt.setBasedir(".."+ pcs.getOutputLocation().toString());
		if (projects == null || projects.getArchiveName() == null || projects.getArchiveName().isEmpty())
		{
			jt.setDestfile(ipr.getName().toString()+".jar");
		}
		else
		{
			
			jt.setDestfile(projects.getArchiveName());
		}
		
		bt.setJt(jt);	
		// End of Jar Task code
		if (projs != null && !projs.isEmpty())
		{
			
			Project tmp = null;
			IProjectUtilities ipu = new ProjectUtilitiesImpl();
			IBuildGeneratorFactory ibf = new BuildGeneratorFactoryImpl();
			for (Projects pro : projs)
			{
				IProject ip = pro.getiProject();
				if (BuildCore.projectsWorkedOn.containsKey(ip.getName()))
				{
//					System.out.println("already worked on the project: "+ip.getName());
					continue;
				}
				
				BuildCore.projectsWorkedOn.put(ip.getName(), 1);
				ProjectType pt;
				try {
					pt = ipu.getProjectType(pro.getiProject());
				} catch (ApplicationException e) {
					e.printStackTrace();
					continue;
				}
				BaseBuildGeneratorEngine be = ibf.getInstance(pt);
				be.generateBuild(pro.getiProject()); // recently added
				tmp = be.packageArchive(pro.getiProject(),relativeTo);// (pro.getiProject(),relativeTo);	
				
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
		final String temp = sb.toString();
			if (temp !=null && !temp.isEmpty())
			{
				bt.setDepends(compileTarget + ", " +temp.substring(0,temp.length()-1));
			}
			else
			{
				bt.setDepends(compileTarget );
			}
		
		bt.setCopyTask(copyLibrariesFromDeploymentAssemblyToRunTimePath(pds,ipr));
		
		cbts.add(bt);			
	
		if (cpt != null && !cpt.isEmpty())
			proj.addClasspath(cpt);
		
		
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
	public ProjectBuildStructure generateBuild(IProject ipr) {
		ProjectCompilationStructure pcs= generateCompilationOptions(ipr);
		ProjectDeploymentStructure pds = readDeploymentAssembly(ipr);
		pbs = new ProjectBuildStructure(pcs, pds);
		
		return pbs;
	}


	@Override
	protected List<BuildTarget> generateArchive(ProjectBuildStructure pbs,
			IProject srcIpr) {
		throw new RuntimeException("This method is not supported in EJB jar build configuration");
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
