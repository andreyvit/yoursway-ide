package com.yoursway.ide.ui.advisor;

import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class AdvisorContentBuilder {
    
    private final FormToolkit toolkit;
    private final ScrolledForm form;
    
    public AdvisorContentBuilder(FormToolkit toolkit, ScrolledForm form) {
        this.toolkit = toolkit;
        this.form = form;
    }
    
    public void addText(String text) {
        FormText control = toolkit.createFormText(form.getBody(), true);
        control.setText(text, true, true);
        control.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
    }
    
}
