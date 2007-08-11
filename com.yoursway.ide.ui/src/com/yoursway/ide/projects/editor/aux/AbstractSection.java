package com.yoursway.ide.projects.editor.aux;

import org.eclipse.ui.forms.widgets.Section;

public class AbstractSection {
    
    private final Section section;
    
    public AbstractSection(Section section) {
        this.section = section;
    }
    
    protected Section getSection() {
        return section;
    }
    
    public void setLayoutData(Object layoutData) {
        section.setLayoutData(layoutData);
    }
    
}
