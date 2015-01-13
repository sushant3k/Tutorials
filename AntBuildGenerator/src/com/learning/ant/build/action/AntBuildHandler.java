/**
 * 
 */
package com.learning.ant.build.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.learning.build.wizard.UIBuildGenerationWizard;

/**
 * @author Sushant Jain
 * @since 1.0
 */
public class AntBuildHandler extends AbstractHandler{

	public AntBuildHandler()
	{
		
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		UIBuildGenerationWizard uibg = new UIBuildGenerationWizard();
		WizardDialog.setDialogHelpAvailable(false);
		Shell shell = HandlerUtil.getActiveShell(arg0);
		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(shell, uibg);
		dialog.create();
		dialog.open(); 
		return null;
	}
	
	
}
