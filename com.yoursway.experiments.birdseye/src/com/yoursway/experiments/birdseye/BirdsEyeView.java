package com.yoursway.experiments.birdseye;

import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Maps.newHashMap;
import static com.yoursway.swt.additions.YsSwtUtils.applySmallSize;
import static com.yoursway.utils.YsCollections.addIfNotNull;
import static com.yoursway.utils.YsPathUtils.extension;
import static com.yoursway.utils.YsStrings.emptyToNull;
import static java.util.Collections.emptyList;
import static org.eclipse.jface.layout.GridDataFactory.swtDefaults;
import static org.eclipse.jface.layout.GridLayoutFactory.fillDefaults;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.experiments.birdseye.model.Container;
import com.yoursway.experiments.birdseye.model.Leaf;
import com.yoursway.experiments.birdseye.model.Node;
import com.yoursway.swt.animations.flip.Flipper;
import com.yoursway.swt.animations.flip.FlipperListener;
import com.yoursway.swt.animations.flip.StackLayoutFlipperListener;

public class BirdsEyeView extends ViewPart implements BirdsEyeListener {
    
    private BirdsEyeComposite birdsEyeDisplay;
    private Label selectionDescription;
    private Composite stack;
    private Composite settingsComposite;
    private Flipper flipper;
    private Composite birdsEyeContainer;
    
    private static class Factory {
        
        private final Display display;
        
        private Color defaultColor;
        
        private Map<String, Color> extToColor = newHashMap();
        
        public Factory(Display display) {
            this.display = display;
            defaultColor = display.getSystemColor(SWT.COLOR_CYAN);
        }
        
        public void associate(String ext, int color) {
            associate(ext, display.getSystemColor(color));
        }
        
        public void associate(String ext, Color color) {
            if (ext == null)
                throw new NullPointerException("ext is null");
            if (color == null)
                throw new NullPointerException("color is null");
            
            extToColor.put(ext.toLowerCase(), color);
        }
        
        public Node createLeaf(File file, String path) {
            return new Leaf(Math.log(1 + file.length()), colorFor(file), path);
        }
        
        private Color colorFor(File file) {
            String ext = emptyToNull(extension(file));
            Color color = (ext == null ? null : extToColor.get(ext.toLowerCase()));
            if (color == null)
                return defaultColor;
            else
                return color;
        }
        
    }
    
    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(fillDefaults().margins(5, 5).spacing(0, 0).create());
        
        stack = new Composite(parent, SWT.NO_BACKGROUND);
        stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack.setLayout(new StackLayout());
        createBirdsEyeComposite(stack);
        createSettingsComposite(stack);
        
        flipper = new Flipper(birdsEyeContainer, settingsComposite);
        new StackLayoutFlipperListener(flipper, stack);
    }

    private void createBirdsEyeComposite(Composite parent) {
        birdsEyeContainer = new Composite(parent, SWT.NONE); 
        birdsEyeContainer.setLayout(fillDefaults().margins(0, 0).spacing(0, 0).create());
        
        birdsEyeDisplay = new BirdsEyeComposite(birdsEyeContainer, SWT.NONE);
        birdsEyeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        selectionDescription = new Label(birdsEyeContainer, SWT.NONE);
        selectionDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Factory factory = new Factory(parent.getDisplay());
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
        
        File rootFile = new File("/Users/andreyvit/Projects/ujudge/ujudge");
        Node root = collect(rootFile, factory, "");
        
        birdsEyeDisplay.display(root);
        birdsEyeDisplay.addListener(this);
        
        applySmallSize(parent);
        
        birdsEyeDisplay.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                flipper.flip();
            }
            
        });
    }
    
    private void createSettingsComposite(Composite parent) {
        settingsComposite = new Composite(parent, SWT.NONE);
        settingsComposite.setLayout(new GridLayout(1, false));
        addBorder(settingsComposite);
        
        applySmallSize(settingsComposite);
        
        Label label = new Label(settingsComposite, SWT.NONE);
        label.setText("Working sets to show:");
        
        new Button(settingsComposite, SWT.CHECK).setText("YourSway IDE");
        new Button(settingsComposite, SWT.CHECK).setText("SADR");
        new Button(settingsComposite, SWT.CHECK).setText("EskoArtwork");
        
        Button settingsButton = new Button(settingsComposite, SWT.PUSH);
        settingsButton.setText("Done");
        settingsButton.setLayoutData(swtDefaults().indent(0, 8).create());
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
                //                gc.drawRectangle(clientArea.x+1, clientArea.y+1, clientArea.width - 3, clientArea.height - 3);
            }
            
        });
    }

    private Node collect(File entry, Factory factory, String prefix) {
        if (entry.isFile())
            return factory.createLeaf(entry, prefix + entry.getName());
        String name = entry.getName();
        if (Pattern.compile("^([._](git|svn|darcs)|CVS)$").matcher(name).find())
            return null;
        File[] files = entry.listFiles();
        Collection<Node> children;
        if (files == null)
            children = emptyList();
        else {
            children = newArrayListWithCapacity(files.length);
            String path = prefix + entry.getName() + "/";
            for (File child : files)
                addIfNotNull(children, collect(child, factory, path));
        }
        if (children.isEmpty())
            return null;
        else
            return new Container(children);
    }
    
    @Override
    public void setFocus() {
        birdsEyeDisplay.setFocus();
    }

    public void birdsEyeHovered(Leaf node, Event event) {
        if (node == null)
            selectionDescription.setText("");
        else
            selectionDescription.setText(node.label());
    }
    
}
