package com.yoursway.experiments.birdseye.component;

import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.yoursway.swt.additions.YsSwtUtils.applyMiniSize;
import static com.yoursway.utils.YsCollections.addIfNotNull;
import static java.util.Collections.emptyList;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.experiments.birdseye.BirdsEyeComposite;
import com.yoursway.experiments.birdseye.BirdsEyeListener;
import com.yoursway.experiments.birdseye.model.Container;
import com.yoursway.experiments.birdseye.model.Leaf;
import com.yoursway.experiments.birdseye.model.Node;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.ViewPresentation;
import com.yoursway.swt.animations.flip.Flipper;
import com.yoursway.swt.animations.flip.StackLayoutFlipperListener;
import com.yoursway.utils.YsFileUtils;

public class BirdsEyeViewImpl implements BirdsEyeView, BirdsEyeListener {
    
    private BirdsEyeComposite birdsEyeDisplay;
    private Label selectionDescription;
    private Composite stack;
    private Composite settingsComposite;
    private Flipper flipper;
    private Composite birdsEyeContainer;
 
    private Composite parentComposite;
    private final BirdsEyeViewCallback callback;

    public BirdsEyeViewImpl(ViewPresentation presentation, BirdsEyeViewCallback callback) {
        if (callback == null)
            throw new NullPointerException("callback is null");
        
        this.parentComposite = presentation.composite();
        this.callback = callback;
        
        createWidgets(parentComposite);
    }
    
    public void createWidgets(Composite parent) {
        parent.setLayout(GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).create());
        
        parent.setBackground(new Color(null, 0, 0, 255));
        stack = new Composite(parent, SWT.BORDER);
        stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack.setLayout(new StackLayout());
        createBirdsEyeComposite(stack);
        createSettingsComposite(stack);
        
        flipper = new Flipper(birdsEyeContainer, settingsComposite, 480);
        new StackLayoutFlipperListener(flipper, stack);
        parent.layout();
    }

    private void createBirdsEyeComposite(Composite parent) {
        birdsEyeContainer = new Composite(parent, SWT.NONE); 
        birdsEyeContainer.setLayout(GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).create());
        
        birdsEyeDisplay = new BirdsEyeComposite(birdsEyeContainer, SWT.NONE);
        birdsEyeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        selectionDescription = new Label(birdsEyeContainer, SWT.NONE);
        selectionDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        birdsEyeDisplay.addListener(this);
        
        applyMiniSize(parent);
        
        birdsEyeDisplay.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                flipper.flip();
            }
            
        });
    }
    
    private void createSettingsComposite(Composite parent) {
        settingsComposite = new Composite(parent, SWT.NONE);
        settingsComposite.setLayout(new GridLayout(1, false));
        
        applyMiniSize(settingsComposite);
        
        Label label = new Label(settingsComposite, SWT.NONE);
        label.setText("Working sets to show:");
        
        new Button(settingsComposite, SWT.CHECK).setText("YourSway IDE");
        new Button(settingsComposite, SWT.CHECK).setText("SADR");
        new Button(settingsComposite, SWT.CHECK).setText("EskoArtwork");
        
        Button settingsButton = new Button(settingsComposite, SWT.PUSH);
        settingsButton.setText("Done");
        settingsButton.setLayoutData(GridDataFactory.swtDefaults().indent(0, 8).create());
        settingsButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                flipper.flip();
            }
        });
        
        for (Control control : settingsComposite.getChildren())
            control.setFont(settingsComposite.getFont());
    }

    private void addBorder(final Composite composite) {
        composite.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                Rectangle clientArea = composite.getClientArea();
                gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
                gc.drawRectangle(clientArea.x, clientArea.y, clientArea.width - 1, clientArea.height - 1);
            }
            
        });
    }

    private Node collect(File entry, Factory factory, String prefix) {
        if (YsFileUtils.isBogusFile(entry.getName()))
            return null;
        if (entry.isFile())
            return factory.createLeaf(entry, prefix + entry.getName());
        return collectChildren(entry, factory, prefix + entry.getName() + "/");
    }

    private Node collectChildren(File entry, Factory factory, String path) {
        File[] files = entry.listFiles();
        Collection<Node> children;
        if (files == null)
            children = emptyList();
        else {
            children = newArrayListWithCapacity(files.length);
            for (File child : files)
                addIfNotNull(children, collect(child, factory, path));
        }
        if (children.isEmpty())
            return null;
        else
            return new Container(children);
    }

    public void birdsEyeHovered(Leaf node, Event event) {
        if (node == null)
            selectionDescription.setText("");
        else
            selectionDescription.setText(node.label());
    }

    public void show(Project project) {
        Factory factory = new Factory(parentComposite.getDisplay());
        factory.associate("rb", SWT.COLOR_RED);
        factory.associate("rhtml", SWT.COLOR_MAGENTA);
        factory.associate("rjs", SWT.COLOR_DARK_MAGENTA);
        
        factory.associate("yml", SWT.COLOR_BLUE);
        factory.associate("txt", SWT.COLOR_BLUE);
        
        factory.associate("css", SWT.COLOR_DARK_CYAN);
        factory.associate("js", SWT.COLOR_DARK_BLUE);
        
        factory.associate("log", SWT.COLOR_YELLOW);
        factory.associate("tmp", SWT.COLOR_YELLOW);
        
        factory.associate("gif", SWT.COLOR_GREEN);
        factory.associate("png", SWT.COLOR_GREEN);
        factory.associate("jpg", SWT.COLOR_GREEN);
        
        File rootFile = project.getLocation();
        Node root = collectChildren(rootFile, factory, "");
        birdsEyeDisplay.display(root);
    }

}
