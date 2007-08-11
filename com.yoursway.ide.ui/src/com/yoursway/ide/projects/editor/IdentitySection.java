package com.yoursway.ide.projects.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.yoursway.ide.projects.editor.aux.AbstractSection;
import com.yoursway.ide.projects.editor.aux.ExtendedFormToolkit;
import com.yoursway.ide.projects.editor.aux.FormLayoutFactory;
import com.yoursway.ide.projects.editor.aux.IObservableValueWithFeedback;
import com.yoursway.ide.projects.editor.aux.TextOVWF;

public class IdentitySection extends AbstractSection {
    
    private final boolean USE_SAVE_BUTTONS;
    private Text projectName;
    private Text projectLocation;
    
    public IdentitySection(Composite parent, ExtendedFormToolkit toolkit) {
        super(toolkit.createSectionTitleBar(parent, "Identity", "Sample section description"));
        USE_SAVE_BUTTONS = false;
        Control client = createClient(getSection(), toolkit);
        getSection().setClient(client);
    }
    
    private Control createClient(Composite parent, ExtendedFormToolkit toolkit) {
        Composite client = toolkit.createComposite(parent);
        client.setLayout(FormLayoutFactory
                .createSectionClientTableWrapLayout(false, USE_SAVE_BUTTONS ? 4 : 3));
        
        createProjectNameEntry(client, toolkit);
        createProjectLocationEntry(client, toolkit);
        createRailsVersionEntry(client, toolkit);
        
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
        return client;
    }
    
    private void createProjectNameEntry(Composite parent, ExtendedFormToolkit toolkit) {
        Label label = toolkit.createEntryLabel(parent, "Project name:");
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        
        projectName = toolkit.createText(parent, null);
        projectName.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 2));
        
        if (USE_SAVE_BUTTONS) {
            final Button saveButton = toolkit.createButton(parent, "Rename", SWT.PUSH);
            saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        }
    }
    
    public IObservableValueWithFeedback projectName() {
        return new TextOVWF(projectName);
    }
    
    public IObservableValueWithFeedback projectLocation() {
        return new TextOVWF(projectLocation);
    }
    
    private void createProjectLocationEntry(Composite parent, ExtendedFormToolkit toolkit) {
        Label label = toolkit.createEntryLabel(parent, "Project location:");
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        
        projectLocation = toolkit.createText(parent, null);
        projectLocation.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 1));
        
        final Button browseButton = toolkit.createButton(parent, "Browse...", SWT.PUSH);
        browseButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        
        if (USE_SAVE_BUTTONS) {
            final Button saveButton = toolkit.createButton(parent, "Move", SWT.PUSH);
            saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE));
        }
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
    }
    
}
