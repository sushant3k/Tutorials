/**
 * This class is used to generate EAR builds.
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
import com.learning.ant.build.structure.ProjectDeploymentStructure;
import com.learning.ant.build.structure.ProjectDeploymentStructure.Projects;
import com.learning.ant.build.utils.BuildCore;
import com.learning.ant.build.xml.structure.BuildTarget;
import com.learning.ant.build.xml.structure.EarTask;
import com.learning.ant.build.xml.structure.Exclude;
import com.learning.ant.build.xml.structure.FileSet;
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
public class EARBuildGeneratorEngine extends BaseBuildGeneratorEngine {

	
	/**
	 * this method is used for packaging of archive.
	 * @param ipr - Project instance to be archived.
	 * @param relativeTo - Relative path to be generated corresponding to this project.
	 */
	@Override
	public Project packageArchive(IProject ipr, IProject relativeTo)
	{
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
	
	private EarTask getEarTask(final ProjectDeploymentStructure pds, final IProject relativeTo)
	{
		EarTask et = new EarTask();
		if (pds == null)
			et.setApplicationXml("./EarContent/META-INF/application.xml");
		else
		et.setApplicationXml(".."+ pds.getRootFolder().toString() +"/META-INF/application.xml");
		
		et.setDestfile("${ant.project.name}.ear");
		
		return et;
		
	}
	protected Project generateArchive(ProjectDeploymentStructure pds, IProject ipr, IProject relativeTo)
	{
		Collection<BuildTarget> cbts =  null;
		
		Project proj = new Project();
		proj.setBasedir(".");
		proj.setDefaultTarget("build");
		proj.setProjectName(ipr.getName());
		
		if (pds == null)
		{
			// TODO: Archive the Project.
			BuildTarget bt = new BuildTarget();
			bt.setName(ipr.getName() + Constants.ARCHIVE_SUFFIX);
			bt.setEt(getEarTask(null, null));
			cbts =  new ArrayList<BuildTarget>(1);
			cbts.add(bt);
		}
		else
		{
			cbts = new ArrayList<BuildTarget>();
			BuildTarget bt = new BuildTarget();
			List<Projects> projs = pds.getProjects();
			if (projs != null && !projs.isEmpty())
			{
				bt.setName(ipr.getName() + Constants.ARCHIVE_SUFFIX);
				StringBuilder sb = new StringBuilder(512);
				Project tmp = null;
				IProjectUtilities ipu = new ProjectUtilitiesImpl();
				IBuildGeneratorFactory ibf = new BuildGeneratorFactoryImpl();
				for (Projects pro : projs)
				{
					IProject ip = pro.getiProject();
					if( BuildCore.projectsWorkedOn.containsKey(ip.getName()) )
					{
//						System.out.println("already worked on the project: "+ip.getName());
						continue;
					}
					
					BuildCore.projectsWorkedOn.put(ip.getName(), 1);
					
					ProjectType pt;
					try {
						pt = ipu.getProjectType(ip);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
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
				bt.setDepends(sb.toString().substring(0,sb.toString().length()-1));
				
			}
			EarTask earTask = getEarTask(pds,relativeTo);
			FileSet fs = null;
//			fs.setDir("./");
//			Collection<Include> colInclude = new ArrayList<Include>();
//			Include inc = new Include();
//			inc.setName("*.jar");
//			colInclude.add(inc);
//			inc = new Include();
//			inc.setName("*.war");
//			
//			colInclude.add(inc);
//			inc = new Include();
//			inc.setName("*.sar");
//			colInclude.add(inc);
//			
//			fs.setInclude(colInclude);
//			
//			earTask.addFileSet(fs);
			
			Collection<Exclude> exc = new ArrayList<Exclude>();
			Exclude ex = new Exclude();
			ex.setName("**/application.xml");
			exc.add(ex);
			
			
			// Copy contents of the EarContent folder.
			fs = new FileSet();
			fs.setDir(".."+pds.getRootFolder());
			fs.setExclude(exc);
			earTask.addFileSet(fs);
			bt.setEt(earTask);
			
			bt.setCopyTask(copyLibrariesFromDeploymentAssemblyToRunTimePath(pds,ipr));		
			bt.setDeleteTask(deleteTaskForCopiedLibraries(pds,ipr));
			cbts.add(bt);			
		}
		
		
		proj.addBuildTargets(cbts);
		
		return proj;
	}

	
	public ProjectBuildStructure generateBuild(IProject ipr)
	{
		throw new RuntimeException("This method is not supported in EAR builds");
	}
	
	
	@Override
	public Project packageArchive(Projects proj, IProject relativeTo) {
		return new Project();
	}

	@Override
	protected List<BuildTarget> generateArchive(ProjectBuildStructure pbs,
			IProject srcIpr) {
		throw new RuntimeException("This method is not supported in EAR configurations.");
	}

	@Override
	public Project getPostProcessCompilationTasks(IProject relativeTo,
			IProject pcs) {
		throw new RuntimeException("This method is not supported for EAR project configuration");
	}
}
