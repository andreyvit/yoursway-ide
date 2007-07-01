package com.yoursway.ide.ui.rubyeditor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IEditingSupport;
import org.eclipse.jface.text.IEditingSupportRegistry;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.link.ILinkedModeListener;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ruby.model.RubyFile;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.StringUtils;

public class RenameLinkedMode {
    
    private class FocusEditingSupport implements IEditingSupport {
        public boolean ownsFocusShell() {
            if (fInfoPopup == null)
                return false;
            // FIXME no ownsFocusShell()
            //            if (fInfoPopup.ownsFocusShell()) {
            //                return true;
            //            }
            
            Shell editorShell = fEditor.getSite().getShell();
            Shell activeShell = editorShell.getDisplay().getActiveShell();
            if (editorShell == activeShell)
                return true;
            return false;
        }
        
        public boolean isOriginator(DocumentEvent event, IRegion subjectRegion) {
            return false; //leave on external modification outside positions
        }
    }
    
    private class EditorSynchronizer implements ILinkedModeListener {
        public void left(LinkedModeModel model, int flags) {
            linkedModeLeft();
            if ((flags & ILinkedModeListener.UPDATE_CARET) != 0) {
                doRename(fShowPreview);
            } else {
                /////////
                Display.getDefault().asyncExec(new Runnable() {
                    
                    public void run() {
                        try {
                            fEditor.getSourceModule().delete(true, null);
                            fEditor.close(false);
                        } catch (ModelException e) {
                            Activator.unexpectedError(e);
                        }
                    }
                    
                });
            }
        }
        
        public void resume(LinkedModeModel model, int flags) {
        }
        
        public void suspend(LinkedModeModel model) {
        }
    }
    
    private class ExitPolicy extends DeleteBlockingExitPolicy {
        public ExitPolicy(IDocument document) {
            super(document);
        }
        
        @Override
        public ExitFlags doExit(LinkedModeModel model, VerifyEvent event, int offset, int length) {
            fShowPreview |= (event.stateMask & SWT.CTRL) != 0;
            return super.doExit(model, event, offset, length);
        }
    }
    
    private static RenameLinkedMode fgActiveLinkedMode;
    
    private final HumaneRubyEditor fEditor;
    
    private RenamePopup fInfoPopup;
    
    private boolean fOriginalSaved;
    private Point fOriginalSelection;
    private String fOriginalName;
    
    private LinkedPosition fNamePosition;
    private LinkedModeModel fLinkedModeModel;
    private LinkedPositionGroup fLinkedPositionGroup;
    private final FocusEditingSupport fFocusEditingSupport;
    private boolean fShowPreview;
    
    public RenameLinkedMode(HumaneRubyEditor editor) {
        Assert.isNotNull(editor);
        fEditor = editor;
        fFocusEditingSupport = new FocusEditingSupport();
    }
    
    public static RenameLinkedMode getActiveLinkedMode() {
        if (fgActiveLinkedMode != null) {
            ISourceViewer viewer = fgActiveLinkedMode.fEditor.getViewer();
            if (viewer != null) {
                StyledText textWidget = viewer.getTextWidget();
                if (textWidget != null && !textWidget.isDisposed()) {
                    return fgActiveLinkedMode;
                }
            }
            // make sure we don't hold onto the active linked mode if anything went wrong with canceling:
            fgActiveLinkedMode = null;
        }
        return null;
    }
    
    public void start() {
        fOriginalSaved = !fEditor.isDirty();
        
        ISourceViewer viewer = fEditor.getViewer();
        IDocument document = viewer.getDocument();
        fOriginalSelection = viewer.getSelectedRange();
        
        try {
            ModuleDeclaration module = RubyFile.parseModule(fEditor.getSourceModule());
            TypeDeclaration[] types = module.getTypes();
            if (types.length == 0)
                return;
            TypeDeclaration selectedNode = types[0];
            int nameStart = selectedNode.getNameStart();
            int nameEnd = selectedNode.getNameEnd();
            
            fLinkedPositionGroup = new LinkedPositionGroup();
            final int offset = nameStart;
            final int length = nameEnd - offset;
            fNamePosition = new LinkedPosition(viewer.getDocument(), offset, length,
                    LinkedPositionGroup.NO_STOP);
            fLinkedPositionGroup.addPosition(fNamePosition);
            
            fOriginalName = selectedNode.getName();
            
            int selectionLength = length;
            if (fOriginalName.endsWith("Controller"))
                selectionLength -= "Controller".length();
            
            fLinkedModeModel = new LinkedModeModel();
            fLinkedModeModel.addGroup(fLinkedPositionGroup);
            fLinkedModeModel.forceInstall();
            // TODO: enable when "Mark occurrences" in Ruby Editor is implemented
            //            fLinkedModeModel.addLinkingListener(new EditorHighlightingSynchronizer(fEditor));
            fLinkedModeModel.addLinkingListener(new EditorSynchronizer());
            
            LinkedModeUI ui = new EditorLinkedModeUI(fLinkedModeModel, viewer);
            ui.setExitPosition(viewer, offset, 0, Integer.MAX_VALUE);
            ui.setExitPolicy(new ExitPolicy(document));
            ui.enter();
            
            viewer.setSelectedRange(offset, selectionLength);
            
            if (viewer instanceof IEditingSupportRegistry) {
                IEditingSupportRegistry registry = (IEditingSupportRegistry) viewer;
                registry.register(fFocusEditingSupport);
            }
            
            openSecondaryPopup();
            //			startAnimation();
            fgActiveLinkedMode = this;
            
        } catch (BadLocationException e) {
            Activator.unexpectedError(e);
        }
    }
    
    //	private void startAnimation() {
    //		//TODO:
    //		// - switch off if animations disabled 
    //		// - show rectangle around target for 500ms after animation
    //		Shell shell= fEditor.getSite().getShell();
    //		StyledText textWidget= fEditor.getViewer().getTextWidget();
    //		
    //		// from popup:
    //		Rectangle startRect= fPopup.getBounds();
    //		
    //		// from editor:
    ////		Point startLoc= textWidget.getParent().toDisplay(textWidget.getLocation());
    ////		Point startSize= textWidget.getSize();
    ////		Rectangle startRect= new Rectangle(startLoc.x, startLoc.y, startSize.x, startSize.y);
    //		
    //		// from hell:
    ////		Rectangle startRect= shell.getClientArea();
    //		
    //		Point caretLocation= textWidget.getLocationAtOffset(textWidget.getCaretOffset());
    //		Point displayLocation= textWidget.toDisplay(caretLocation);
    //		Rectangle targetRect= new Rectangle(displayLocation.x, displayLocation.y, 0, 0);
    //		
    //		RectangleAnimation anim= new RectangleAnimation(shell, startRect, targetRect);
    //		anim.schedule();
    //	}
    
    void doRename(boolean showPreview) {
        cancel();
        
        Image image = null;
        Label label = null;
        
        fShowPreview |= showPreview;
        try {
            ISourceViewer viewer = fEditor.getViewer();
            if (viewer instanceof SourceViewer) {
                SourceViewer sourceViewer = (SourceViewer) viewer;
                Control viewerControl = sourceViewer.getControl();
                if (viewerControl instanceof Composite) {
                    Composite composite = (Composite) viewerControl;
                    Display display = composite.getDisplay();
                    
                    // Flush pending redraw requests:
                    while (!display.isDisposed() && display.readAndDispatch()) {
                    }
                    
                    // Copy editor area:
                    GC gc = new GC(composite);
                    Point size;
                    try {
                        size = composite.getSize();
                        image = new Image(gc.getDevice(), size.x, size.y);
                        gc.copyArea(image, 0, 0);
                    } finally {
                        gc.dispose();
                        gc = null;
                    }
                    
                    // Persist editor area while executing refactoring:
                    label = new Label(composite, SWT.NONE);
                    label.setImage(image);
                    label.setBounds(0, 0, size.x, size.y);
                    label.moveAbove(null);
                }
            }
            
            String newName = fNamePosition.getContent();
            if (fOriginalName.equals(newName))
                return;
            
            /////////
            
            String className = RailsNamingConventions.joinNamespaces(RailsNamingConventions
                    .camelize(RailsNamingConventions.splitPath(newName)));
            if (!className.endsWith("Controller"))
                className += "Controller";
            
            final String fileName = StringUtils.join(RailsNamingConventions.underscore(RailsNamingConventions
                    .splitNamespaces(className)), "/")
                    + ".rb";
            
            // TODO FIXME handle names with "::"
            
            fEditor.getSourceModule().rename(fileName, false, null);
            
            /////////
            
            Shell shell = fEditor.getSite().getShell();
            restoreFullSelection();
            Display.getDefault().asyncExec(new Runnable() {
                
                public void run() {
                    fEditor.doSave(null);
                }
                
            });
        } catch (CoreException ex) {
            Activator.unexpectedError(ex);
        } catch (BadLocationException e) {
            Activator.unexpectedError(e);
        } finally {
            if (label != null)
                label.dispose();
            if (image != null)
                image.dispose();
        }
    }
    
    public void cancel() {
        if (fLinkedModeModel != null) {
            fLinkedModeModel.exit(ILinkedModeListener.NONE);
        }
        linkedModeLeft();
    }
    
    private void restoreFullSelection() {
        if (fOriginalSelection.y != 0) {
            int originalOffset = fOriginalSelection.x;
            LinkedPosition[] positions = fLinkedPositionGroup.getPositions();
            for (int i = 0; i < positions.length; i++) {
                LinkedPosition position = positions[i];
                if (!position.isDeleted() && position.includes(originalOffset)) {
                    fEditor.getViewer().setSelectedRange(position.offset, position.length);
                    return;
                }
            }
        }
    }
    
    private void linkedModeLeft() {
        fgActiveLinkedMode = null;
        if (fInfoPopup != null) {
            fInfoPopup.close();
        }
        
        ISourceViewer viewer = fEditor.getViewer();
        if (viewer instanceof IEditingSupportRegistry) {
            IEditingSupportRegistry registry = (IEditingSupportRegistry) viewer;
            registry.unregister(fFocusEditingSupport);
        }
    }
    
    private void openSecondaryPopup() {
        //        fInfoPopup = new RenamePopup(fEditor, this);
        //        fInfoPopup.open();
    }
    
    public boolean isCaretInLinkedPosition() {
        return getCurrentLinkedPosition() != null;
    }
    
    public LinkedPosition getCurrentLinkedPosition() {
        Point selection = fEditor.getViewer().getSelectedRange();
        int start = selection.x;
        int end = start + selection.y;
        LinkedPosition[] positions = fLinkedPositionGroup.getPositions();
        for (int i = 0; i < positions.length; i++) {
            LinkedPosition position = positions[i];
            if (position.includes(start) && position.includes(end))
                return position;
        }
        return null;
    }
    
    public boolean isEnabled() {
        try {
            String newName = fNamePosition.getContent();
            if (fOriginalName.equals(newName))
                return false;
            /*
             * TODO: use JavaRenameProcessor#checkNewElementName(String) but
             * make sure implementations don't access outdated Java Model (cache
             * all necessary information before starting linked mode).
             */
            return true;
        } catch (BadLocationException e) {
            return false;
        }
        
    }
    
    public boolean isOriginalName() {
        try {
            String newName = fNamePosition.getContent();
            return fOriginalName.equals(newName);
        } catch (BadLocationException e) {
            return false;
        }
    }
    
}
