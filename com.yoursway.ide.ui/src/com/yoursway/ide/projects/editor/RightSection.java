package com.yoursway.ide.projects.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.yoursway.ide.projects.editor.aux.AbstractSection;
import com.yoursway.ide.projects.editor.aux.ExtendedFormToolkit;
import com.yoursway.ide.projects.editor.aux.FormLayoutFactory;

public class RightSection extends AbstractSection {
    
    public RightSection(Composite parent, ExtendedFormToolkit toolkit) {
        super(toolkit.createSectionTitleBar(parent, "Something useful", "I dunno what should be here..."));
        getSection().setClient(createClient(getSection(), toolkit));
    }
    
    private Control createClient(Composite parent, ExtendedFormToolkit toolkit) {
        Composite client = toolkit.createComposite(parent);
        client.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(false, 3));
        return client;
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
