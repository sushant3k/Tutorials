
package com.learning.build.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.learning.ant.build.exception.ApplicationException;
import com.learning.ant.build.interfaces.IProjectUtilities;
import com.learning.ant.build.interfaces.ProjectUtilitiesImpl;
import com.learning.build.constants.UIConstants;

/**
 * 
 * This class is used to create the Package Script UI Page for the native code i.e. CMP, CEMS etc.
 */

public class UIBuildGenerationPage extends WizardPage {

	public static boolean isSchemeGenerated = false;
	private Button earProjectCheckBox = null;
	private Button ejbProjectCheckBox = null;
	private Button webProjectCheckBox = null;
	private Button utilityProjectCheckBox = null;
	private CheckboxTableViewer tableViewer;
	private List<IJavaProject> selectedProjectsList = new ArrayList<IJavaProject>();

	private Group projectGroup;
	private GridData gd;
	private static final IProjectUtilities ipu = new ProjectUtilitiesImpl(); // Runtime exception here would screw the loading of this class.
	
	protected UIBuildGenerationPage(ImageDescriptor idesc) {

		super("Page1");
		setTitle(UIConstants.PACKAGE_SCRIPT_WIZARD_TITLE);
		setDescription(UIConstants.PACKAGE_SCRIPT_WIZARD_DESC);
		setImageDescriptor(idesc);
	}

	public  List<IJavaProject> getSelectedProjectsList() {
		return selectedProjectsList;
	}

	public void setSelectedProjectsList(List<IJavaProject> sp) {
		selectedProjectsList = sp;
	}


	
	private void createProjectCheckBoxes(final Composite workArea)
	{
		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		
		projectGroup = new Group(workArea, SWT.NONE);
		projectGroup.setText(UIConstants.MODULE_TYPE);
		
		GridLayout projectGroupLayout = new GridLayout(4, false);		
			
		projectGroup.setLayout(projectGroupLayout);
		projectGroup.setLayoutData(gd);

		earProjectCheckBox = new Button(projectGroup, SWT.CHECK);
		earProjectCheckBox.setSelection(false);
		earProjectCheckBox.setText(UIConstants.EAR_PROJECTS);
		
		ejbProjectCheckBox = new Button(projectGroup, SWT.CHECK);
		ejbProjectCheckBox.setSelection(false);
		ejbProjectCheckBox.setText(UIConstants.EJB_PROJECTS);
		
		webProjectCheckBox = new Button(projectGroup, SWT.CHECK);
		webProjectCheckBox.setSelection(false);
		webProjectCheckBox.setText(UIConstants.DYNAMIC_WEB_MODULE);
		
		utilityProjectCheckBox = new Button(projectGroup, SWT.CHECK);
		utilityProjectCheckBox.setSelection(false);
		utilityProjectCheckBox.setText(UIConstants.JAVA_PROJECTS);
		
		
	}
	@Override
	public void createControl(Composite parent) {


		Composite workArea = new Composite(parent, SWT.NONE);
		setControl(workArea);

		workArea.setLayout(new GridLayout());
		createProjectCheckBoxes(workArea);
				
		Composite listComposite = new Composite(workArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		listComposite.setLayout(layout);
		
		listComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		final Table table = new Table(listComposite, SWT.CHECK | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer = new CheckboxTableViewer(table);
		table.setLayout(new TableLayout());
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		tableViewer.setContentProvider(new WorkbenchContentProvider() {
			public Object[] getElements(Object element) {
				if (element instanceof IJavaProject[]) {
					return (IJavaProject[]) element;
				}
				return null;
			}
		});
		
		tableViewer.setLabelProvider(new WorkbenchLabelProvider());
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					selectedProjectsList.add((IJavaProject)event.getElement());
				} else {
					selectedProjectsList.remove(event.getElement());
				}
			}
		});
		
		createSelectionButtons(listComposite);
		table.setEnabled(true);

		earProjectCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {				
				repaintTable();
				if (!earProjectCheckBox.getSelection())
				{
					Set<IJavaProject> ijp = initializeEARProjects();
					removeProjectsFromList(ijp);
				}
				
				
			}
		});
		
		ejbProjectCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				
				repaintTable();
				if (!ejbProjectCheckBox.getSelection())
				{
					Set<IJavaProject> ijp = initializeEJBProjects();
					removeProjectsFromList(ijp);
				}
			}
		});
		
		webProjectCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				repaintTable();
				if (!webProjectCheckBox.getSelection())
				{
					Set<IJavaProject> ijp = initializeWebProjects();
					removeProjectsFromList(ijp);
				}
			}
		});
		
		utilityProjectCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				repaintTable();
				if (!utilityProjectCheckBox.getSelection())
				{
					Set<IJavaProject> ijp = initializeUtilityProjects();
					removeProjectsFromList(ijp);
				}
			}
		});
		setControl(workArea);
		setPageComplete(false);
		
	}
	
	
	private void removeProjectsFromList(Set<IJavaProject> ijp)
	{
		if (ijp !=null)
		{
			for (IJavaProject ij : ijp)
			{
				selectedProjectsList.remove(ij);
			}
				
		}
	}
	private void repaintTable()
	{
		Set<IJavaProject> set = new HashSet<IJavaProject>();;
		if (earProjectCheckBox.getSelection()) {
			set.addAll(initializeEARProjects());
			
		} 
		if(ejbProjectCheckBox.getSelection())
		{
			set.addAll(initializeEJBProjects());
		}
		if(webProjectCheckBox.getSelection())
		{
			set.addAll(initializeWebProjects());
		
		}
		if(utilityProjectCheckBox.getSelection())
		{
			set.addAll(initializeUtilityProjects());
		
		}
		if( set !=null && set.size() > 0)
		{
			IJavaProject ijp[] = new IJavaProject[set.size()];
			tableViewer.setInput(set.toArray(ijp));
		}
		else
		{
			IJavaProject ijp[] = new IJavaProject[0];
			tableViewer.setInput(ijp);
		}
		
	}
	
	private Set<IJavaProject> initializeWebProjects()
	{
		Set<IJavaProject> set = null;
		try {
			 set = ipu.getAllAccessibleWebProjectsInWorkspace();			
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return set;
	}
	
	private Set<IJavaProject> initializeEJBProjects()
	{
		Set<IJavaProject> set = null;
		try {
			 set = ipu.getAllAccessibleEJBProjectsInWorkspace();			
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return set;
	}
	
	private Set<IJavaProject> initializeUtilityProjects()
	{
		Set<IJavaProject> set = null;
		try {
			 set = ipu.getAllAccessibleUtilityProjectsInWorkspace();			
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return set;
	}
	
	private Set<IJavaProject> initializeEARProjects()
	{
		
		Set<IJavaProject> set = null;
		try {
			 set = ipu.getAllAccessibleEARJavaProjectsInWorkspace();
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return set;
	}
	 
	 private void createSelectionButtons(Composite composite) {
		 
		 Composite buttonsComposite = new Composite(composite, SWT.NONE);
		 GridLayout layout = new GridLayout();
		 layout.marginWidth = 0;
		 layout.marginHeight = 0;
		 buttonsComposite.setLayout(layout);
		 buttonsComposite.setLayoutData(new GridData(
		 GridData.VERTICAL_ALIGN_BEGINNING));
		 
		 Button selectAll = new Button(buttonsComposite, SWT.PUSH);
		 selectAll.setText("Select All");
		 selectAll.addSelectionListener(new SelectionAdapter() {
			 public void widgetSelected(SelectionEvent e) {
				 for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
					 selectedProjectsList.add((IJavaProject)tableViewer.getElementAt(i));
		         }
		         tableViewer.setAllChecked(true);
		         }
		     });
		     setButtonLayoutData(selectAll);
		         
		     Button deselectAll = new Button(buttonsComposite, SWT.PUSH);
		     deselectAll.setText("Deselect All");
		     deselectAll.addSelectionListener(new SelectionAdapter() {
		     public void widgetSelected(SelectionEvent e) {
		          selectedProjectsList.clear();
		          tableViewer.setAllChecked(false);
		     }
		 });
		 setButtonLayoutData(deselectAll); 
	 }
	
}

