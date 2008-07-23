package com.yoursway.ide.worksheet;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.controller.WorksheetController;
import com.yoursway.ide.worksheet.internal.view.WorksheetView;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewCallback;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewFactory;

public class Worksheet {
    
    /**
     * Please treat this method as an example only. Consider using proper outer
     * MVC rather than calling this method.
     */
    public static void create(final Composite parent, WorksheetCommandExecutor executor,
            WorksheetShortcuts shortcuts, final WorksheetStyle style) {
        new WorksheetController(new WorksheetViewFactory() {
            
            public WorksheetView createView(WorksheetViewCallback callback) {
                return new WorksheetView(parent, callback, style);
            }
            
        }, executor, shortcuts);
    }
    
}
