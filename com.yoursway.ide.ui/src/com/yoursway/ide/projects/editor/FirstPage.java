package com.yoursway.ide.projects.editor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.yoursway.ide.projects.editor.aux.ExtendedFormToolkit;
import com.yoursway.ide.projects.editor.aux.FormLayoutFactory;
import com.yoursway.ide.projects.editor.aux.GridDataBuilder;
import com.yoursway.ide.projects.editor.aux.GridLayoutBuilder;
import com.yoursway.ide.projects.editor.aux.IObservableValueWithFeedback;
import com.yoursway.ide.projects.editor.feedback.MessageManagerFeedback;

public class FirstPage extends ProjectEditorPage {
    
    private final ViewModel viewModel;
    private IdentitySection leftSection;
    private RightSection rightSection;
    private DataBindingContext dbc;
    
    public FirstPage(FormEditor editor, ViewModel viewModel) {
        super(editor, "first", "General");
        this.viewModel = viewModel;
    }
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        ExtendedFormToolkit toolkit = new ExtendedFormToolkit(managedForm.getToolkit());
        ScrolledForm form = managedForm.getForm();
        form.setText("Project Editor");
        
        createSections(toolkit, form);
        
        bind(new MessageManagerFeedback(managedForm.getMessageManager()));
    }
    
    private void createSections(ExtendedFormToolkit toolkit, ScrolledForm form) {
        Composite body = form.getBody();
        body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
        
        leftSection = new IdentitySection(body, toolkit);
        leftSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
        rightSection = new RightSection(body, toolkit);
        rightSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
        
        Composite bottom = toolkit.createComposite(body);
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
    
    private void bind(MessageManagerFeedback feedbackManager) {
        dbc = new DataBindingContext(SWTObservables.getRealm(Display.getDefault()));
        bindValue(feedbackManager, viewModel.getProjectName(), leftSection.projectName(),
                new ProjectNameValidator());
        //        bindValue(feedbackManager, viewModel.getProjectLocation(), leftSection.projectLocation(),
        //                new ProjectNameValidator());
    }
    
    private void bindValue(MessageManagerFeedback feedbackManager, IObservableValue modelValue,
            IObservableValueWithFeedback targetValue, IValidator validator) {
        UpdateValueStrategy mup = createModelUpdatePolicy().setAfterGetValidator(validator);
        Binding binding = dbc.bindValue(targetValue.observe(), modelValue, mup, null);
        feedbackManager.hook(binding, targetValue.feedback());
    }
    
    private UpdateValueStrategy createModelUpdatePolicy() {
        return new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
    }
    
}
