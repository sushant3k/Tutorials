/**
 * This is the Build Generation Wizard. It triggers the build process.
 */
package com.learning.build.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;

import com.learning.ant.activator.Activator;
import com.learning.ant.build.action.BuildTriggerEngineImpl;
import com.learning.ant.build.action.IBuildTriggerEngine;
import com.learning.build.constants.UIConstants;
import com.learning.management.IBuildGenerator;
import com.learning.management.ManagementAgent;

/**
 * 
 * @author Sushant
 * @version 1.0
 * @since 1.0
 *
 */

public class UIBuildGenerationWizard extends Wizard {

	UIBuildGenerationPage uiBGPage;
	private List<String> lstBuildSuccessful;
	private List<String> lstBuildFailed;
	private static String windowTitle = "Generate Build Scripts";
	/**
	 * Constructor
	 */
	public UIBuildGenerationWizard() {
		super();
		setWindowTitle(windowTitle);
		setNeedsProgressMonitor(true);
	}

	/**
	 * This method add the package script page in Wizard.
	 */
	public void addPages() {		
		ImageDescriptor idesc = new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				URL resourceUrl = Activator.getDefault()
						.getBundle().getEntry(UIConstants.LOGO_59x58);
						
				String iconFile = null;
				try {
					iconFile = FileLocator.toFileURL(resourceUrl).getFile();
				} catch (IOException e) {				
				}

				return new ImageData(iconFile);
			}
		};
		uiBGPage = new UIBuildGenerationPage(idesc);
		addPage(uiBGPage);

	}

	
	/**
	 * This method is used to generate the package script for the EAR project exists in the workspace.
	 */
	@Override
	public boolean performFinish() {
		
		boolean isBuildGenerated = true;
		try {
			triggerBuild(uiBGPage.getShell());
		}catch (Exception e) {
			isBuildGenerated = false;	
		
		} 
		
		if(lstBuildSuccessful !=null && lstBuildFailed !=null)
		{
			MessageDialog.openInformation(new Shell(), "Build Generation Status",
					"Build script generation successful for: "+ lstBuildSuccessful + "\n" +
					"Build script failed for: " + lstBuildFailed);
			
		}
		else if (lstBuildSuccessful !=null && lstBuildFailed ==null)
		{
			MessageDialog.openInformation(new Shell(), "Build Generation Status",
					"Build script generation successful for: "+ lstBuildSuccessful);
		}
		else if (lstBuildSuccessful ==null && lstBuildFailed !=null)
		{
			MessageDialog.openInformation(new Shell(), "Build Generation Status",
					"Build script generation failed for: "+ lstBuildFailed);
		}
		else
		{
			MessageDialog.openInformation(new Shell(), "Build Generation Status",
					"Nothing to generate");
		}					
		return isBuildGenerated;
	}

	public boolean canFinish() {
		return true;
	}

	public boolean performCancel() {
		return true;
	}
	
	private  void triggerBuild(final Shell shell) throws Exception {
		
		final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(null);
		final ManagementAgent ma = Activator.getManagementAgent();
		final IBuildGenerator ibg = ma.getMBeanInterface();
		  try {
			  // False parameter specifies that we don't need a cancel button.
			  // because we don't support rollback.
			progressMonitorDialog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Generating Build Script ....", IProgressMonitor.UNKNOWN);
					try {					
						List<IJavaProject> l = uiBGPage.getSelectedProjectsList();
						if (l == null || l.isEmpty())
						{
							monitor.done();
							return;
						}												
						IBuildTriggerEngine ibte = new BuildTriggerEngineImpl();
						String projectName = null;
						for(IJavaProject ijp: l)
						{
							boolean buildFailed = false;
							projectName = ijp.getProject().getName();
							monitor.setTaskName("Generating build for " +projectName);
							try
							{
								ibte.triggerBuild(ijp);
								if (ibg !=null)
								{
									ibg.incrementSuccessFulProjectCount();
									
								}
								
							}
							catch(Throwable e)
							{
								buildFailed = true;
								if (lstBuildFailed == null)
								{
									lstBuildFailed = new ArrayList<String>();
								}
								lstBuildFailed.add(projectName);
								
								if (ibg !=null)
								{
									ibg.incrementApplicationExceptionsCounter();
									ibg.setApplicationException(projectName+"::"+e.getMessage());
								}
								
								//monitor.setTaskName("Build Generation for "+ projectName + " failed because:"+e.getMessage());
								
							}
							if(!buildFailed)
							{
								if (lstBuildSuccessful == null)
								{
									lstBuildSuccessful = new ArrayList<String>();
								}
								lstBuildSuccessful.add(projectName);
								monitor.setTaskName("Build Generation successful for " + projectName);
							}
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
					monitor.done();
				}
			});
		} catch (InvocationTargetException e1) {
			
		} catch (InterruptedException e1) {
			
		}
		
	}
}
