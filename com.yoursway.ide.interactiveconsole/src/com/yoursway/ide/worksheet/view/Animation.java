package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.graphics.Point;

public class Animation {
    
    private volatile int targetWidth = 0;
    private volatile int targetHeight = 0;
    private volatile int targetAlpha = 0;
    
    public void start(final AnimationUpdater updater) {
        
        Thread animationThread = new Thread() {
            @Override
            public void run() {
                try {
                    int width = 0;
                    int height = 0;
                    int alpha = 0;
                    final int d = 2;
                    
                    while (true) {
                        int x = targetWidth - width;
                        int y = targetHeight - height;
                        int a = targetAlpha - alpha;
                        
                        x = f(x, d);
                        y = f(y, d);
                        a = f(a, d * 3);
                        
                        //> delay before decrease
                        
                        if (x != 0 || y != 0) {
                            width += x;
                            height += y;
                            
                            updater.updateSize(width, height);
                        }
                        
                        if (a != 0) {
                            alpha += a;
                            
                            updater.updateAlpha(alpha);
                        }
                        
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err); //!
                }
            }
        };
        
        animationThread.start();
        
    }
    
    public void targetSize(int width, int height) {
        targetWidth = width;
        targetHeight = height;
    }
    
    public void targetAlpha(int alpha) {
        targetAlpha = alpha;
    }
    
    private int f(int x, int d) {
        if (x == 0)
            return 0;
        int f = x / d;
        if (f == 0)
            return x > 0 ? 1 : -1;
        return f;
    }
    
    public void updateCurrentSize(Point size) {
        // TODO Auto-generated method stub
        
    }
    
}
