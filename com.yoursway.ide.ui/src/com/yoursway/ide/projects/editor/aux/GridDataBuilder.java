package com.yoursway.ide.projects.editor.aux;

import org.eclipse.swt.layout.GridData;

public class GridDataBuilder {
    
    private final GridData data = new GridData();
    
    public GridDataBuilder() {
    }
    
    public GridDataBuilder(int horizontalAlignment, int verticalAlignment, boolean grabExcessHorizontalSpace,
            boolean grabExcessVerticalSpace, int horizontalSpan, int verticalSpan) {
        data.horizontalAlignment = horizontalAlignment;
        data.verticalAlignment = verticalAlignment;
        data.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
        data.grabExcessVerticalSpace = grabExcessVerticalSpace;
        data.horizontalSpan = horizontalSpan;
        data.verticalSpan = verticalSpan;
    }
    
    public GridDataBuilder(int horizontalAlignment, int verticalAlignment, boolean grabExcessHorizontalSpace,
            boolean grabExcessVerticalSpace) {
        this(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace, grabExcessVerticalSpace, 1, 1);
    }
    
    public GridDataBuilder minimumWidth(int minimumWidth) {
        data.minimumWidth = minimumWidth;
        return this;
    }
    
    public GridDataBuilder minimumHeight(int minimumHeight) {
        data.minimumHeight = minimumHeight;
        return this;
    }
    
    public GridDataBuilder horizontalIndent(int horizontalIndent) {
        data.horizontalIndent = horizontalIndent;
        return this;
    }
    
    public GridDataBuilder verticalIndent(int verticalIndent) {
        data.verticalIndent = verticalIndent;
        return this;
    }
    
    public GridData build() {
        return data;
    }
    
}
