package com.yoursway.ui.popup.anchors;

public class Insets {
    
    private final int topInset;
    private final int rightInset;
    private final int bottomInset;
    private final int leftInset;
    
    public Insets(int insets) {
        this.topInset = insets;
        this.rightInset = insets;
        this.bottomInset = insets;
        this.leftInset = insets;
    }
    
    public Insets(int topInset, int rightInset, int bottomInset, int leftInset) {
        this.topInset = topInset;
        this.rightInset = rightInset;
        this.bottomInset = bottomInset;
        this.leftInset = leftInset;
    }
    
    public Insets(int verticalInset, int horizontalInset) {
        this.topInset = verticalInset;
        this.rightInset = horizontalInset;
        this.bottomInset = verticalInset;
        this.leftInset = horizontalInset;
    }
    
    public int getTopInset() {
        return topInset;
    }
    
    public int getRightInset() {
        return rightInset;
    }
    
    public int getBottomInset() {
        return bottomInset;
    }
    
    public int getLeftInset() {
        return leftInset;
    }
    
}
