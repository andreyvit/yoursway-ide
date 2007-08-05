package com.yoursway.ide.projects.editor;

import java.util.regex.Pattern;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class IdentitySection extends ManagedFormSection {
    
    private final class ProjectNameValidator implements IValidator {
        public IStatus validate(Object value) {
            String name = (String) value;
            if (name != name.trim())
                return ValidationStatus.error("Leading and trailing whitespace not allowed");
            if (name.length() == 0)
                return ValidationStatus.error("Empty name is not allowed");
            if (!Pattern.matches("^[a-zA-Z0-9. _-]+$", name))
                return ValidationStatus.error("Illegal character");
            return Status.OK_STATUS;
            //            return ValidationStatus.info("Test");
        }
    }
    
    private final ViewModel model;
    private DataBindingContext bindingContext;
    private final boolean USE_SAVE_BUTTONS;
    
    public IdentitySection(ViewModel model, Composite parent, FormToolkit toolkit,
            IMessageManager messageManager, int style) {
        super(parent, toolkit, style, messageManager, true);
        this.model = model;
        final Section section = getSection();
        
        section.setText("Identity");
        section.setDescription("Sample section description");
        
        USE_SAVE_BUTTONS = false;
        createClient(section, new ExtendedFormToolkit(toolkit));
    }
    
    private void createClient(Section section, ExtendedFormToolkit toolkit) {
        section.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
        TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
        section.setLayoutData(data);
        
        Composite client = toolkit.createComposite(section);
        client.setLayout(FormLayoutFactory
                .createSectionClientTableWrapLayout(false, USE_SAVE_BUTTONS ? 4 : 3));
        section.setClient(client);
        
        Realm realm = SWTObservables.getRealm(section.getDisplay());
        bindingContext = new DataBindingContext(realm);
        
        createProjectNameEntry(client, toolkit);
        createProjectLocationEntry(client, toolkit);
        createRailsVersionEntry(client, toolkit);
        
        bindingContext.updateTargets();
        
        //        Listener listener = new Listener() {
        //
        //            public void handleEvent(Event event) {
        //                switch (event.type) {
        //                case SWT.FocusIn:
        //                    saveButton.getShell().setDefaultButton(saveButton);
        //                    break;
        //                case SWT.FocusOut:
        //                    saveButton.getShell().setDefaultButton(null);
        //                    break;
        //                }
        //            }
        //            
        //        };
        //        entryText.addListener(SWT.FocusIn, listener);
        //        entryText.addListener(SWT.FocusOut, listener);
        
    }
    
    private void createProjectNameEntry(Composite parent, ExtendedFormToolkit toolkit) {
        Label label = toolkit.createEntryLabel(parent, "Project name:");
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        
        final Text text = toolkit.createText(parent, null);
        text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 2));
        
        if (USE_SAVE_BUTTONS) {
            final Button saveButton = toolkit.createButton(parent, "Rename", SWT.PUSH);
            saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        }
        
        final ProjectNameValidator validator = new ProjectNameValidator();
        final Binding binding = bindingContext.bindValue(SWTObservables.observeText(text, SWT.Modify), model
                .getProjectName(), createModelUpdatePolicy(validator), null);
        
        hookValidationVisuals(binding, text);
    }
    
    private void createProjectLocationEntry(Composite parent, ExtendedFormToolkit toolkit) {
        Label label = toolkit.createEntryLabel(parent, "Project location:");
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        
        final Text text = toolkit.createText(parent, null);
        text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 1));
        
        final Button browseButton = toolkit.createButton(parent, "Browse...", SWT.PUSH);
        browseButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        
        if (USE_SAVE_BUTTONS) {
            final Button saveButton = toolkit.createButton(parent, "Move", SWT.PUSH);
            saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        }
        
        //        final ProjectNameValidator validator = new ProjectNameValidator();
        final Binding binding = bindingContext.bindValue(SWTObservables.observeText(text, SWT.Modify), model
                .getProjectName(), createModelUpdatePolicy(), null);
        
        hookValidationVisuals(binding, text);
    }
    
    private void createRailsVersionEntry(Composite parent, ExtendedFormToolkit toolkit) {
        Label label = toolkit.createEntryLabel(parent, "Project location:");
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        
        final Combo combo = new Combo(parent, SWT.NONE);
        combo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 2));
        combo.setText("Rails 1.2.3");
        toolkit.adapt(combo, true, false);
        
        if (USE_SAVE_BUTTONS) {
            final Button saveButton = toolkit.createButton(parent, "Set", SWT.PUSH);
            saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        }
        //        
        //        final ProjectNameValidator validator = new ProjectNameValidator();
        //        final Binding binding = bindingContext.bindValue(SWTObservables.observeText(text, SWT.Modify), model
        //                .getProjectName(), createModelUpdatePolicy(validator), null);
        
        //        hookValidationVisuals(binding, combo);
    }
    
    private UpdateValueStrategy createModelUpdatePolicy(final ProjectNameValidator validator) {
        return createModelUpdatePolicy().setAfterGetValidator(validator);
    }
    
    private UpdateValueStrategy createModelUpdatePolicy() {
        return new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
    }
    
    private void hookValidationVisuals(final Binding binding, final Control control) {
        final IObservableValue validationStatus = binding.getValidationStatus();
        validationStatus.addValueChangeListener(new IValueChangeListener() {
            
            public void handleValueChange(ValueChangeEvent event) {
                IStatus status = (IStatus) validationStatus.getValue();
                if (status.getSeverity() != IStatus.OK)
                    getMessageManager().addMessage(binding, status.getMessage(), null,
                            mapSeverity(status.getSeverity()), control);
                else {
                    getMessageManager().removeMessage(binding, control);
                }
            }
            
        });
    }
    
    protected int mapSeverity(int severity) {
        switch (severity) {
        case IStatus.ERROR:
            return IMessageProvider.ERROR;
        case IStatus.WARNING:
            return IMessageProvider.WARNING;
        case IStatus.INFO:
            return IMessageProvider.INFORMATION;
        default:
            throw new AssertionError("Improper severity for translation: " + severity);
        }
    }
}
