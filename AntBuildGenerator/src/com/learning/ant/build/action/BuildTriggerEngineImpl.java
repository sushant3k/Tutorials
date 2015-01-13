/**
 * 
 */
package com.learning.ant.build.action;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import com.learning.ant.build.exception.AccessibilityException;
import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.IProjectUtilities;
import com.learning.ant.build.interfaces.ProjectUtilitiesImpl;
import com.learning.ant.build.interfaces.CProjectType.ProjectType;
import com.learning.ant.build.utils.BuildCore;
import com.learning.ant.build.xml.structure.BuildTarget;
import com.learning.ant.build.xml.structure.Project;
import com.learning.build.constants.Constants;
import com.learning.build.generator.factory.BaseBuildGeneratorEngine;
import com.learning.build.generator.factory.BuildGeneratorFactoryImpl;
import com.learning.build.generator.factory.IBuildGeneratorFactory;

/**
 * @author Sushant
 * @version 1.0
 * @since 1.0
 *
 */
public class BuildTriggerEngineImpl implements IBuildTriggerEngine {


	@Override
	public void triggerBuild(List<IJavaProject> ijp) throws AccessibilityException, ApplicationException{
		if (ijp !=null)
		{
			for (IJavaProject ip : ijp)			
			{
				triggerBuild(ip);
			}
		}
	
	}

	@Override
	public void triggerBuild(IJavaProject ijp) throws AccessibilityException, ApplicationException{

		IProject i = ijp.getProject();
		// Check if the Project is not accessibile, then skip it.
		if (!i.isAccessible() || i.getName().equalsIgnoreCase("RemoteSystemsTempFiles"))
		{
			throw new AccessibilityException("Project with name="+i.getName()+" is not accessibile.");
		}

		IProjectUtilities ipu = new ProjectUtilitiesImpl();
		try {
			IBuildGeneratorFactory ibf = new BuildGeneratorFactoryImpl();
			BaseBuildGeneratorEngine ibe = null;
			ProjectType pt = ipu.getProjectType(i);
			if( pt == null)
			{
				throw new IllegalArgumentException("Invalid Project Type");
			}
			if (pt.equals(ProjectType.EAR))
			{											
				
				ibe = ibf.getInstance(ProjectType.EAR);																		
			}
			else if (pt.equals(ProjectType.EJB) || pt.equals(ProjectType.WEB) || pt.equals(ProjectType.JAVA))
			{
				ibe = ibf.getInstance(pt);	
				ibe.generateBuild(i);
			}										
			
			else
				throw new ApplicationException("Project with name="+i.getName()+" is not supported.");
			
			Project proj = ibe.packageArchive(i,i);
			/**
			 * Add Default Build Target here.
			 */
			BuildTarget bt = new BuildTarget();
			bt.setName("build");
			bt.setDepends(i.getName()+Constants.ARCHIVE_SUFFIX);
			proj.addBuildTargetToCollection(bt);
			
			String str = BuildCore.marshallAntStructure(proj);						
			try 
			{
				File f = new File(i.getLocation().toString()+File.separator+"build.xml");
				FileWriter fw = new FileWriter(f);
				fw.write(str);							
				fw.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}
		catch(Exception t)
		{
			t.printStackTrace();
			if (t instanceof RuntimeException)
				throw new RuntimeException(t);
		}
		finally
		{
			// make sure to clear all the static variables, otherwise you will be screwed up.
			BuildCore.projectsWorkedOn.clear();
			BuildCore.postProcessCompilationTasks.clear();
			BuildCore.classPathLibraries.clear();
		}
		
	
		
	}

}
