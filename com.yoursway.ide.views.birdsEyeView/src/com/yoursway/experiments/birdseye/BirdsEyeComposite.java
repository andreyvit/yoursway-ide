package com.yoursway.experiments.birdseye;

import static com.yoursway.swt.additions.YsSwtGeometry.divideIntoHorizontalParts;
import static com.yoursway.swt.additions.YsSwtGeometry.divideIntoVerticalParts;
import static com.yoursway.swt.additions.YsSwtGeometry.emptyRectangle;
import static com.yoursway.swt.additions.YsSwtGeometry.isSameSize;
import static com.yoursway.utils.Listeners.newListenersByIdentity;
import static java.lang.Math.max;
import static java.lang.Math.min;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.experiments.birdseye.model.Compartment;
import com.yoursway.experiments.birdseye.model.Container;
import com.yoursway.experiments.birdseye.model.Leaf;
import com.yoursway.experiments.birdseye.model.Node;
import com.yoursway.utils.Listeners;

public class BirdsEyeComposite extends Composite {
    
    private BirdsEyeCompositePrivates privates = new BirdsEyeCompositePrivates();
    
    private Image buffer;
    
    private Node root;
    
    private Leaf hovered;
    
    private transient Listeners<BirdsEyeListener> listeners = newListenersByIdentity();
    
    public synchronized void addListener(BirdsEyeListener listener) {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(BirdsEyeListener listener) {
        listeners.remove(listener);
    }
    
    private class BirdsEyeCompositePrivates implements Listener {
        
        public void handleEvent(Event event) {
            switch (event.type) {
            case SWT.Paint:
                paint(event);
                break;
            case SWT.Resize:
                regenerate();
                break;
            case SWT.MouseMove:
                handleHovering(event);
                break;
            case SWT.MouseExit:
                handleHoveringEnd(event);
                break;
            }
        }
        
    }
    
    public BirdsEyeComposite(Composite parent, int style) {
        super(parent, style | SWT.NO_BACKGROUND);
        addListener(SWT.Paint, privates);
        addListener(SWT.Resize, privates);
        addListener(SWT.MouseMove, privates);
        addListener(SWT.MouseExit, privates);
    }
    
    void handleHovering(Event event) {
        Point point = new Point(event.x, event.y);
        Leaf hovered = root.pick(point);
        changeHovered(hovered, event);
    }
    
    void handleHoveringEnd(Event event) {
        changeHovered(null, event);
    }
    
    private void changeHovered(Leaf hovered, Event event) {
        if (hovered == this.hovered)
            return;
        this.hovered = hovered;
        for (BirdsEyeListener listener : listeners)
            listener.birdsEyeHovered(hovered, event);
    }
    
    void paint(Event event) {
        if (buffer == null)
            generate();
        if (buffer != null)
            event.gc.drawImage(buffer, 0, 0);
    }
    
    public void display(Node root) {
        if (root == null)
            throw new NullPointerException("root is null");
        this.root = root;
        regenerate();
    }
    
    private void regenerate() {
        if (root == null)
            return;
        generate();
        redraw();
    }
    
    private void generate() {
        Rectangle clientArea = getClientArea();
        if (clientArea.width < 2 || clientArea.height < 2) {
            if (buffer != null)
                buffer.dispose();
            buffer = null;
            return;
        }
        if (buffer == null || !isSameSize(clientArea, buffer.getBounds())) {
            if (buffer != null)
                buffer.dispose();
            buffer = new Image(getDisplay(), clientArea.width, clientArea.height);
        }
        GC gc = new GC(buffer);
        try {
            gc.setBackground(getBackground());
            gc.fillRectangle(clientArea);
            drawNode(gc, root, clientArea);
        } finally {
            gc.dispose();
        }
    }
    
    private void drawNode(GC gc, Node node, Rectangle area) {
        node.setRectangle(area);
        if (node instanceof Leaf)
            drawLeaf(gc, (Leaf) node, area);
        else if (node instanceof Compartment)
            drawContainer(gc, (Compartment) node, area);
        else
            drawNode(gc, ((Container) node).child(), area);
    }
    
    private void drawContainer(GC gc, Compartment compartment, Rectangle area) {
        Node first = compartment.first();
        Node second = compartment.second();
        double totalSize = compartment.size();
        double ratio = first.size() / totalSize;
            
        Rectangle firstArea = emptyRectangle();
        Rectangle secondArea = emptyRectangle();
        if (area.width > area.height)
            divideIntoVerticalParts(area, ratio, firstArea, secondArea);
        else
            divideIntoHorizontalParts(area, ratio, firstArea, secondArea);
        
        drawNode(gc, first, firstArea);
        drawNode(gc, second, secondArea);
    }
    
    private void drawLeaf(GC gc, Leaf leaf, Rectangle area) {
        gc.setBackground(leaf.color());
        gc.fillRectangle(area);
        gc.drawRectangle(area);
        drawGradient(gc, area, leaf.color());
    }
    
    public static void drawGradient(GC gc, Rectangle area, Color color) {
        if (area.width == 0 || area.height == 0)
            return;
        
        if (area.width == 1 && area.height == 1) {
            gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
            gc.drawPoint(area.x, area.y);
            return;
        }
        
        float[] hsb = color.getRGB().getHSB();
        PaletteData paletteData = new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);
        
        int colors[] = new int[256];
        // darker colors
        for (int i = 0; i < 128; i++) {
            float adjust = 0.5f * (128 - i) / 128.0f;
            RGB rgb = new RGB(hsb[0], hsb[1], hsb[2] * (1 - adjust));
            colors[i] = paletteData.getPixel(rgb);
        }
        
        // lighter colors
        for (int i = 0; i < 128; i++) {
            float adjust = 0.5f * i / 128.0f;
            
            // first ramp up brightness, then decrease saturation
            float dif = 1 - hsb[2];
            float absAdjust = (dif + hsb[1]) * adjust;
            
            RGB rgb;
            if (absAdjust < dif)
                rgb = new RGB(hsb[0], hsb[1], hsb[2] + absAdjust);
            else
                rgb = new RGB(hsb[0], hsb[1] + dif - absAdjust, 1.0f);
            colors[128 + i] = paletteData.getPixel(rgb);
        }
        
        int width = area.width;
        int height = area.height;
        ImageData data = new ImageData(width, height, 24, paletteData);
        
        int x0 = 0, y0 = 0;
        
        // Horizontal lines
        for (int y = 0; y < height; y++) {
            int gradient = (int) (256 * (y + 0.5f) / area.height + 0.5);
            int intColor = colors[min(255, max(0, gradient))];
            
            int xend = (height - y - 1) * width / height; // Maximum x. 
            for (int x = 0; x < xend; x++)
                data.setPixel(x, y, intColor);
        }
        
        // Vertical lines
        for (int x = 0; x < width; x++) {
            int gradient = (int) (256 * (1 - (x + 0.5f) / area.width) + 0.5);
            int intColor = colors[min(255, max(0, gradient))];
            
            int ystart = (width - x - 1) * height / width; // Minimum y.
            for (int y = ystart; y < height; y++)
                data.setPixel(x, y, intColor);
        }
        
        Image image = new Image(gc.getDevice(), data);
        gc.drawImage(image, area.x, area.y);
        image.dispose();
    }
    
}
