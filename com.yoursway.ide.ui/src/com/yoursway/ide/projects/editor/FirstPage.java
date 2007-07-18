package com.yoursway.ide.projects.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class FirstPage extends ProjectEditorPage {
    
    private final IdentityModel identityModel;
    
    public FirstPage(FormEditor editor, IdentityModel identityModel) {
        super(editor, "first", "General");
        this.identityModel = identityModel;
    }
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Project Editor");
        
        Composite body = form.getBody();
        body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
        
        final IdentitySection leftSection = new IdentitySection(identityModel, form.getBody(), toolkit,
                managedForm.getMessageManager(), SWT.NONE);
        leftSection.getSection().setLayoutData(
                new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
        final RightSection rightSection = new RightSection(form.getBody(), toolkit, managedForm
                .getMessageManager(), SWT.NONE);
        rightSection.getSection().setLayoutData(
                new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
        addSection(managedForm, leftSection);
        addSection(managedForm, rightSection);
        
        Composite bottom = toolkit.createComposite(form.getBody());
        bottom.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL, 1, 2));
        bottom.setBackground(form.getForm().getHeadColor(IFormColors.H_BOTTOM_KEYLINE1));
        bottom.setLayout(new GridLayoutBuilder().margins(5).build());
        new Label(bottom, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Button button = new Button(bottom, SWT.NONE);
        button.setText("Apply changes");
        button.setLayoutData(new GridDataBuilder(SWT.FILL, SWT.FILL, false, false).minimumWidth(
                button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x * 2).build());
        new Label(bottom, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }
    
    private void addSection(IManagedForm managedForm, ManagedFormSection section) {
        managedForm.addPart(section);
    }
    
}
