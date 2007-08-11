package com.yoursway.ide.projects.editor.aux;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class ExtendedFormToolkit extends FormToolkitDecorator {
    
    public ExtendedFormToolkit(FormToolkit parent) {
        super(parent);
    }
    
    public Label createEntryLabel(Composite parent, String text) {
        Label labelControl = createLabel(parent, text);
        labelControl.setForeground(getColors().getColor(IFormColors.TITLE));
        return labelControl;
    }
    
    public Section createSectionTitleBar(Composite parent) {
        Section section = createSection(parent, ExpandableComposite.TITLE_BAR);
        section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
        return section;
    }
    
    public Section createSectionTitleBar(Composite parent, String text, String description) {
        Section section = createSectionTitleBar(parent);
        section.setText(text);
        section.setDescription(description);
        return section;
    }
    
}
