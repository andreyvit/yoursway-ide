package com.yoursway.ide.projects.editor.aux;

import org.eclipse.swt.layout.GridLayout;

public class GridLayoutBuilder {
    
    private final GridLayout layout = new GridLayout();
    
    public GridLayoutBuilder columns(int columns, boolean equal) {
        return columns(columns).equalColumn(equal);
    }
    
    public GridLayoutBuilder columns(int columns) {
        layout.numColumns = columns;
        return this;
    }
    
    public GridLayoutBuilder equalColumn(boolean equalColumns) {
        layout.makeColumnsEqualWidth = equalColumns;
        return this;
    }
    
    public GridLayoutBuilder margins(int margin) {
        return margins(margin, margin);
    }
    
    public GridLayoutBuilder margins(int vertical, int horizontal) {
        return margins(vertical, horizontal, vertical, horizontal);
    }
    
    public GridLayoutBuilder margins(int top, int right, int bottom, int left) {
        layout.marginHeight = layout.marginWidth = 0;
        layout.marginTop = top;
        layout.marginRight = right;
        layout.marginBottom = bottom;
        layout.marginLeft = left;
        return this;
    }
    
    public GridLayoutBuilder spacing(int horizontalSpacing, int verticalSpacing) {
        return horizontalSpacing(horizontalSpacing).verticalSpacing(verticalSpacing);
    }
    
    public GridLayoutBuilder horizontalSpacing(int spacing) {
        layout.horizontalSpacing = spacing;
        return this;
    }
    
    public GridLayoutBuilder verticalSpacing(int spacing) {
        layout.verticalSpacing = spacing;
        return this;
    }
    
    public GridLayout build() {
        return layout;
    }
    
}
