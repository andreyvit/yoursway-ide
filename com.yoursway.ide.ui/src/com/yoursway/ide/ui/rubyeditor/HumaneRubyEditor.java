package com.yoursway.ide.ui.rubyeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceReference;
import org.eclipse.dltk.ruby.internal.ui.editor.RubyEditor;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.analysis.model.AdvisoryManager;
import com.yoursway.ide.analysis.model.IAdvice;
import com.yoursway.ide.analysis.model.IAdvicesChangeEvent;
import com.yoursway.ide.analysis.model.IAdvisingEditor;
import com.yoursway.ide.analysis.model.IIteratesAdvices;
import com.yoursway.ide.ui.Activator;

public class HumaneRubyEditor extends RubyEditor implements IAdvisingEditor {
    
    public static final String EDITOR_ID = "com.yoursway.ide.ui.rubyeditor";
    
    private final Color assistTextColor = new Color(null, 127, 127, 127);
    
    private IIteratesAdvices advicesProvider;
    
    private IAdvice[] advices;
    
    @Override
    public String getEditorId() {
        return EDITOR_ID;
    }
    
    @Override
    public void createPartControl(final Composite parent) {
        super.createPartControl(parent);
        final StyledText textWidget = getSourceViewer().getTextWidget();
        textWidget.addPaintObjectListener(new PaintObjectListener() {
            
            public void paintObject(final PaintObjectEvent event) {
                System.out.println(".paintObject()");
            }
            
        });
        final PaintListener paintListener = new PaintListener() {
            
            public void paintControl(PaintEvent event) {
                try {
                    System.out.println(".paintControl()");
                    int startLine = textWidget.getLineIndex(event.y);
                    int y = textWidget.getLinePixel(startLine);
                    int endY = event.y + event.height;
                    int lineHeight = textWidget.getLineHeight();
                    event.gc.setForeground(assistTextColor);
                    FontData[] fontData = event.gc.getFont().getFontData();
                    fontData[0].setHeight(fontData[0].getHeight() - 1);
                    Font font = new Font(null, fontData[0]);
                    event.gc.setFont(font);
                    if (endY > 0) {
                        int lineCount = textWidget.getContent().getLineCount();
                        // int x = 0 /* leftMargin */-
                        // textWidget.getHorizontalPixel();
                        int charCount = textWidget.getCharCount();
                        int adviceIndex = 0;
                        final int advicesCount = advices.length;
                        for (int i = startLine; y < endY && i < lineCount; i++) {
                            while (adviceIndex < advicesCount && advices[adviceIndex].getLineNumber() < i)
                                adviceIndex++;
                            int startOffset = textWidget.getOffsetAtLine(i);
                            if (startOffset >= charCount)
                                continue;
                            int endOffset;
                            if (i == lineCount - 1)
                                endOffset = charCount - 1;
                            else
                                endOffset = textWidget.getOffsetAtLine(i + 1) - 1;
                            Rectangle lineBounds = textWidget.getTextBounds(startOffset, endOffset);
                            int drawX = lineBounds.x + lineBounds.width;
                            int drawY = y;
                            if (adviceIndex < advicesCount && advices[adviceIndex].getLineNumber() == i)
                                advices[adviceIndex].paint(event.gc, drawX + 50, drawY);
                            // else
                            // event.gc.drawText(", аднака!", drawX, drawY,
                            // true);
                            y += lineHeight;
                        }
                    }
                } catch (Throwable e) {
                    Activator.log(e);
                }
            }
            
        };
        textWidget.addPaintListener(paintListener);
        final IPostSelectionProvider selectionProvider = (IPostSelectionProvider) getSelectionProvider();
        final ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
            
            public void selectionChanged(SelectionChangedEvent event) {
                handleSelectionChanged();
            }
            
        };
        selectionProvider.addPostSelectionChangedListener(selectionChangedListener);
        advicesProvider = AdvisoryManager.instance().registerEditor(this);
        refreshAdvices();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        AdvisoryManager.instance().unregisterEditor(this);
    }
    
    void handleSelectionChanged() {
        final ISourceReference reference = computeHighlightRangeSourceReference();
        if (reference instanceof IMethod) {
            final IMethod method = (IMethod) reference;
            System.out.println();
        }
    }
    
    public void advicesChanged(final IAdvicesChangeEvent event) {
        refreshAdvices();
        getSite().getShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
                getSourceViewer().getTextWidget().redraw();
            }
        });
    }
    
    public ISourceModule getSourceModule() {
        return (ISourceModule) getInputModelElement();
    }
    
    void refreshAdvices() {
        final List<IAdvice> advicesCollection = new ArrayList<IAdvice>();
        for (final Iterator<IAdvice> iterator = advicesProvider.iterateAdvices(); iterator.hasNext();) {
            final IAdvice advice = iterator.next();
            advicesCollection.add(advice);
        }
        Collections.sort(advicesCollection, new Comparator<IAdvice>() {
            
            public int compare(final IAdvice a, final IAdvice b) {
                final int aval = a.getLineNumber();
                final int bval = b.getLineNumber();
                if (aval < bval)
                    return -1;
                if (aval > bval)
                    return 1;
                return 0;
            }
            
        });
        advices = advicesCollection.toArray(new IAdvice[advicesCollection.size()]);
    }
    
    public void enterLinkedMode() {
        //        ModuleDeclaration module = RubyFile.parseModule(getSourceModule());
        //        TypeDeclaration[] types = module.getTypes();
        //        if (types.length == 0)
        //            return;
        //        TypeDeclaration decl = types[0];
        //        int nameStart = decl.getNameStart();
        //        int nameEnd = decl.getNameEnd();
        //        
        //        LinkedPositionGroup group = new LinkedPositionGroup();
        //        try {
        //            final int offset = nameStart;
        //            final int length = nameEnd - offset;
        //            final ISourceViewer viewer = getSourceViewer();
        //            group.addPosition(new LinkedPosition(viewer.getDocument(), offset, length,
        //                    LinkedPositionGroup.NO_STOP));
        //            
        //            LinkedModeModel model = new LinkedModeModel();
        //            model.addLinkingListener(new ILinkedModeListener() {
        //                
        //                public void left(LinkedModeModel model, int flags) {
        //                }
        //                
        //                public void resume(LinkedModeModel model, int flags) {
        //                }
        //                
        //                public void suspend(LinkedModeModel model) {
        //                }
        //                
        //            });
        //            model.addGroup(group);
        //            model.forceInstall();
        //            LinkedModeUI ui = new LinkedModeUI(model, viewer);
        //            ui.setExitPosition(viewer, offset, 0, Integer.MAX_VALUE);
        //            ui.enter();
        //            viewer.setSelectedRange(offset, 0);
        //        } catch (BadLocationException e) {
        //            Activator.unexpectedError(e);
        //        }
        new RenameLinkedMode(this).start();
    }
    
}
