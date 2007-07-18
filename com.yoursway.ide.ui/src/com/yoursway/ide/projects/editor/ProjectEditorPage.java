package com.yoursway.ide.projects.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class ProjectEditorPage extends FormPage {
    
    public ProjectEditorPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        final ScrolledForm form = managedForm.getForm();
        final FormToolkit toolkit = managedForm.getToolkit();
        final Form rform = form.getForm();
        toolkit.decorateFormHeading(rform);
        
        //        Composite headComposite = new Composite(rform.getHead(), SWT.NONE);
        //        headComposite.setLayout(new TableWrapLayout());
        //        Link link = new Link(headComposite, SWT.NONE);
        //        link.setText("<a>Apply changes</a>");
        //        rform.setHeadClient(headComposite);
        
        Action applyAction = new Action("Apply changes") {
            
        };
        
        rform.getToolBarManager().add(new ControlContribution("test") {
            
            @Override
            protected Control createControl(Composite parent) {
                Hyperlink link = new Hyperlink(parent, SWT.NONE);
                //                FormLayoutFactory.visualizeLayoutArea(parent, SWT.COLOR_RED);
                link.setText("Apply changes");
                return link;
            }
            
        });
        rform.updateToolBar();
        
        //        final String href = getHelpResource();
        //        if (href != null) {
        //            IToolBarManager manager = form.getToolBarManager();
        //            Action helpAction = new Action("help") { //$NON-NLS-1$
        //                public void run() {
        //                    BusyIndicator.showWhile(form.getDisplay(), new Runnable() {
        //                        public void run() {
        //                            PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(href);
        //                        }
        //                    });
        //                }
        //            };
        //            helpAction.setToolTipText(PDEUIMessages.PDEFormPage_help); 
        //            helpAction.setImageDescriptor(PDEPluginImages.DESC_HELP);
        //            manager.add(helpAction);
        //        }
        //check to see if our form parts are contributing actions
        //        IFormPart[] parts = managedForm.getParts();
        //        for(int i = 0; i < parts.length; i++) {
        //            if(parts[i] instanceof IAdaptable) {
        //                IAdaptable adapter = (IAdaptable) parts[i];
        //                IAction[] actions = 
        //                    (IAction[]) adapter.getAdapter(IAction[].class);
        //                if(actions != null) {
        //                    for(int j = 0; j < actions.length; j++) {
        //                        form.getToolBarManager().add(actions[j]);
        //                    }
        //                }
        //            }
        //        }
        //        form.updateToolBar();
    }
    
}
