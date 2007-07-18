package com.yoursway.ide.projects.editor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;

public class FormToolkitDecorator {
    
    private final FormToolkit toolkit;
    
    public FormToolkitDecorator(FormToolkit toolkit) {
        this.toolkit = toolkit;
    }
    
    public void adapt(Composite composite) {
        toolkit.adapt(composite);
    }
    
    public void adapt(Control control, boolean trackFocus, boolean trackKeyboard) {
        toolkit.adapt(control, trackFocus, trackKeyboard);
    }
    
    public Button createButton(Composite parent, String text, int style) {
        return toolkit.createButton(parent, text, style);
    }
    
    public Composite createComposite(Composite parent, int style) {
        return toolkit.createComposite(parent, style);
    }
    
    public Composite createComposite(Composite parent) {
        return toolkit.createComposite(parent);
    }
    
    public Composite createCompositeSeparator(Composite parent) {
        return toolkit.createCompositeSeparator(parent);
    }
    
    public ExpandableComposite createExpandableComposite(Composite parent, int expansionStyle) {
        return toolkit.createExpandableComposite(parent, expansionStyle);
    }
    
    public Form createForm(Composite parent) {
        return toolkit.createForm(parent);
    }
    
    public FormText createFormText(Composite parent, boolean trackFocus) {
        return toolkit.createFormText(parent, trackFocus);
    }
    
    public Hyperlink createHyperlink(Composite parent, String text, int style) {
        return toolkit.createHyperlink(parent, text, style);
    }
    
    public ImageHyperlink createImageHyperlink(Composite parent, int style) {
        return toolkit.createImageHyperlink(parent, style);
    }
    
    public Label createLabel(Composite parent, String text, int style) {
        return toolkit.createLabel(parent, text, style);
    }
    
    public Label createLabel(Composite parent, String text) {
        return toolkit.createLabel(parent, text);
    }
    
    public ScrolledPageBook createPageBook(Composite parent, int style) {
        return toolkit.createPageBook(parent, style);
    }
    
    public ScrolledForm createScrolledForm(Composite parent) {
        return toolkit.createScrolledForm(parent);
    }
    
    public Section createSection(Composite parent, int sectionStyle) {
        return toolkit.createSection(parent, sectionStyle);
    }
    
    public Label createSeparator(Composite parent, int style) {
        return toolkit.createSeparator(parent, style);
    }
    
    public Table createTable(Composite parent, int style) {
        return toolkit.createTable(parent, style);
    }
    
    public Text createText(Composite parent, String value, int style) {
        return toolkit.createText(parent, value, style);
    }
    
    public Text createText(Composite parent, String value) {
        return toolkit.createText(parent, value);
    }
    
    public Tree createTree(Composite parent, int style) {
        return toolkit.createTree(parent, style);
    }
    
    public void decorateFormHeading(Form form) {
        toolkit.decorateFormHeading(form);
    }
    
    public void dispose() {
        toolkit.dispose();
    }
    
    public int getBorderMargin() {
        return toolkit.getBorderMargin();
    }
    
    public int getBorderStyle() {
        return toolkit.getBorderStyle();
    }
    
    public FormColors getColors() {
        return toolkit.getColors();
    }
    
    public HyperlinkGroup getHyperlinkGroup() {
        return toolkit.getHyperlinkGroup();
    }
    
    public int getOrientation() {
        return toolkit.getOrientation();
    }
    
    public void paintBordersFor(Composite parent) {
        toolkit.paintBordersFor(parent);
    }
    
    public void refreshHyperlinkColors() {
        toolkit.refreshHyperlinkColors();
    }
    
    public void setBackground(Color bg) {
        toolkit.setBackground(bg);
    }
    
    public void setBorderStyle(int style) {
        toolkit.setBorderStyle(style);
    }
    
    public void setOrientation(int orientation) {
        toolkit.setOrientation(orientation);
    }
    
}
