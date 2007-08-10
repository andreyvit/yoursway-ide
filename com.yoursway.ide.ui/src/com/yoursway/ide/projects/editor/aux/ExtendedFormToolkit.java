package com.yoursway.ide.projects.editor.aux;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ExtendedFormToolkit extends FormToolkitDecorator {
    
    public ExtendedFormToolkit(FormToolkit parent) {
        super(parent);
    }
    
    public Label createEntryLabel(Composite parent, String text) {
        Label labelControl = createLabel(parent, text);
        labelControl.setForeground(getColors().getColor(IFormColors.TITLE));
        return labelControl;
    }
    
}
