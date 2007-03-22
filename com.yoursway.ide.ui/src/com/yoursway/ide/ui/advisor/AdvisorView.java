package com.yoursway.ide.ui.advisor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.ide.ui.Activator;

public class AdvisorView extends ViewPart {
    
    public static final String ID = "com.yoursway.ide.ui.AdvisorView";
    
    private ScrolledForm form;
    
    private FormToolkit toolkit;
    
    public void createPartControl(Composite parent) {
        IViewSite site = (IViewSite) getSite();
        IActionBars actionBars = site.getActionBars();
        IToolBarManager toolBarManager = actionBars.getToolBarManager();
        toolBarManager.add(new Action() {
            
            {
                setText("Refresh");
                setImageDescriptor(Activator.getImageDescriptor("icons/rails_perspective.gif"));
            }
            
            public void runWithEvent(Event event) {
                Control[] children = form.getBody().getChildren();
                for (int i = 0; i < children.length; i++) {
                    Control control = children[i];
                    control.dispose();
                }
                createContent();
                form.getBody().layout(true);
                form.layout(true);
                form.reflow(true);
            }
            
        });
        
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        Font font = form.getFont();
        // FontData[] fontData = font.getFontData();
        // fontData[0].setHeight(fontData[0].getHeight() - 2);
        // fontData[0].setStyle(0);
        Font theFont = font; // new Font(null, fontData[0]);
        form.setFont(theFont);
        form.getBody().setLayout(createTableLayout());
        // ExpandableComposite foo = toolkit.createSection(form.getBody(),
        // ExpandableComposite.TWISTIE
        // | ExpandableComposite.CLIENT_INDENT);
        // foo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB,
        // TableWrapData.FILL_GRAB));
        // foo.setText("Project");
        // foo.setLayout(createTableLayout());
        // foo.setLayout(FormLayoutFactory.createClearGridLayout(true, 1));
        
        // Composite composite = toolkit.createComposite(foo);
        // foo.setClient(composite);
        //        
        // composite.setLayout(FormLayoutFactory.createSectionClientGridLayout(false,
        // 1));
        //        
        // FormText text2 = toolkit.createFormText(composite, true);
        // text2.setText("<form><p>This is <b>mega</b></p></form>", true,
        // false);
        //        
        // createContent();
        
        // text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        // Hyperlink link = toolkit.createHyperlink(foo, "Create a new project",
        // SWT.NONE);
        // link.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        // link.addHyperlinkListener(new IHyperlinkListener() {
        //            
        // public void linkActivated(HyperlinkEvent e) {
        // MessageDialog.openInformation(form.getShell(), "Test", "Action
        // executed");
        // }
        //            
        // public void linkEntered(HyperlinkEvent e) {
        // }
        //            
        // public void linkExited(HyperlinkEvent e) {
        // }
        //            
        // });
    }
    
    private void createContent() {
        AdvisorContentBuilder builder = new AdvisorContentBuilder(toolkit, form);
        
        builder
                .addText("<form><p>Your workspace is empty. You probably want to <a href=\"x\">create a new project</a>.</p></form>");
        builder.addText("<form><p></p></form>");
        builder.addText("<form><p></p></form>");
        builder.addText("<form><p></p></form>");
        
        builder.addText("<form><p><b>Creating a new project</b></p></form>");
        builder
                .addText("<form><p>To create a project, you need to choose <b>New Project</b> from the <b>File</b> menu.</p></form>");
        builder.addText("<form><p><a href=\"back\">Back</a></p></form>");
        builder.addText("<form><p></p></form>");
        builder.addText("<form><p></p></form>");
        builder.addText("<form><p></p></form>");
        
        builder.addText("<form><p>Your workspace is empty. What do you want?</p></form>");
        builder.addText("<form><li><a href=\"new_proj\">Create a new project</a></li></form>");
        builder.addText("<form><li><a href=\"new_proj\">Import an existing Rails project</a></li></form>");
        builder.addText("<form><p>Before you start, you need either to:</p>"
                + "<li><a href=\"new_proj\">Create a new project</a></li>"
                + "<li><a href=\"import_proj\">Import an existing Rails project</a></li>" + " " + "</form>");
        form.layout();
    }
    
    private TableWrapLayout createTableLayout() {
        TableWrapLayout layout = new TableWrapLayout();
        layout.verticalSpacing = 0;
        layout.topMargin = 10;
        layout.bottomMargin = 10;
        layout.leftMargin = 10;
        layout.rightMargin = 10;
        return layout;
    }
    
    public void setFocus() {
        form.setFocus();
    }
    
}
