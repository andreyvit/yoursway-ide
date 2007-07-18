package com.yoursway.ide.projects.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class RightSection extends ManagedFormSection {
    
    public RightSection(Composite parent, FormToolkit toolkit, IMessageManager messageManager, int style) {
        super(parent, toolkit, style, messageManager, true);
        final Section section = getSection();
        
        section.setText("Something useful");
        section.setDescription("I dunno what should be here...");
        
        createClient(section, new ExtendedFormToolkit(toolkit));
    }
    
    private void createClient(Section section, ExtendedFormToolkit toolkit) {
        section.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
        TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
        section.setLayoutData(data);
        
        Composite client = toolkit.createComposite(section);
        client.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(false, 3));
        section.setClient(client);
        
        //        Realm realm = SWTObservables.getRealm(section.getDisplay());
        //        bindingContext = new DataBindingContext(realm);
        
        //        Label entryLabel = toolkit.createEntryLabel(client, "Project name:");
        //        entryLabel.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        //        
        //        final Text entryText = toolkit.createText(client, null);
        //        entryText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 1));
        //        
        //        final Button saveButton = toolkit.createButton(client, "Save", SWT.PUSH);
        //        saveButton.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.MIDDLE, 1, 1));
        
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
