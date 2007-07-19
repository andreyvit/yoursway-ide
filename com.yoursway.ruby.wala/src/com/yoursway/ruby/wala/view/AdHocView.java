package com.yoursway.ruby.wala.view;

import static com.ibm.wala.cast.ipa.callgraph.Util.dumpCG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.ibm.wala.analysis.typeInference.TypeInference;
import com.ibm.wala.cast.ipa.callgraph.CAstAnalysisScope;
import com.ibm.wala.cast.ipa.callgraph.StandardFunctionTargetSelector;
import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.client.impl.ZeroOneCFABuilderFactory;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.yoursway.ruby.wala.JSCFABuilder;
import com.yoursway.ruby.wala.JSZeroXCFABuilder;
import com.yoursway.ruby.wala.JavaScriptEntryPoints;
import com.yoursway.ruby.wala.JavaScriptLoader;
import com.yoursway.ruby.wala.JavaScriptLoaderFactory;
import com.yoursway.ruby.wala.JavaScriptTranslatorFactory;
import com.yoursway.ruby.wala.JavaScriptTypes;
import com.yoursway.ruby.wala.internal.Activator;

public class AdHocView extends ViewPart {

    private Text text;

    @Override
    public void createPartControl(Composite parent) {
        getViewSite().getActionBars().getToolBarManager().add(new Action("Build") {
            
            @Override
            public void run() {
                try {
                    buildSomething();
                } catch (Exception e) {
                    final Status status = new Status(Status.ERROR, "com.yoursway.ruby.wala", e.getMessage(), e);
                    Activator.getDefault().getLog().log(status);
                    ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            "Analysis fucked up", e.getMessage(), status);
                }
            }
            
        });
        text = new Text(parent, SWT.MULTI | SWT.V_SCROLL);
    }
    
    void clear() {
        text.setText("");
    }
    
    void putline(String line) {
        final String newText = text.getText() + line + "\n";
        text.setText(newText);
        text.setSelection(newText.length());
    }

    protected void buildSomething() throws Exception {
        clear();
        putline("Starting work on "
                + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).format(
                        Calendar.getInstance().getTime()));
        File f = new File("/tmp/test.js");
        PrintWriter writer = new PrintWriter(new FileWriter(f));
//        addPrologue(writer);
        
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (activeEditor == null)
            return;
        if (!(activeEditor instanceof AbstractTextEditor))
            return;
        AbstractTextEditor textEditor = (AbstractTextEditor) activeEditor;
        Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
        method.setAccessible(true);
        ISourceViewer sourceViewer = (ISourceViewer) method.invoke(textEditor);
        String text = sourceViewer.getTextWidget().getText();
        
        writer.println(text);
        writer.close();
        
        SourceFileModule sfm = new SourceFileModule(f, "superapp.js");

        // can be static
        JavaScriptTranslatorFactory translatorFactory = new JavaScriptTranslatorFactory.CAstRhinoFactory();
        JavaScriptLoaderFactory loaderFactory = new JavaScriptLoaderFactory(translatorFactory);
        CAstAnalysisScope scope = new CAstAnalysisScope(new SourceFileModule[] {sfm}, loaderFactory);
        IClassHierarchy classHierarchy = ClassHierarchy.make(scope, loaderFactory, JavaScriptLoader.JS);
        Iterable<Entrypoint> roots = new JavaScriptEntryPoints(classHierarchy, classHierarchy.getLoader(JavaScriptTypes.jsLoader));
        final boolean keepIRs = true;
        final AnalysisOptions options = new AnalysisOptions(scope, AstIRFactory.makeDefaultFactory(keepIRs), roots);
        com.ibm.wala.ipa.callgraph.impl.Util.addDefaultSelectors(options, classHierarchy);
        options.setSelector(new StandardFunctionTargetSelector(classHierarchy, options.getMethodTargetSelector()));
        options.setUseConstantSpecificKeys(true);
        options.setUseStacksForLexicalScoping(true);
        options.getSSAOptions().setPreserveNames(true);
        CallGraph CG;
        
        JSCFABuilder builder = new JSZeroXCFABuilder(classHierarchy, options, null, null, null, ZeroXInstanceKeys.ALLOCATIONS);
        CG = builder.makeCallGraph(builder.getOptions());

        // this seems to be for Java only, and does not work
        ZeroOneCFABuilderFactory zof = new ZeroOneCFABuilderFactory();
        CallGraphBuilder builder2 = zof.make(options, classHierarchy, scope, true);
        CG = builder2.makeCallGraph(options);
        
//        dumpCG(builder, CG);
        
        final IClassHierarchy ch = CG.getClassHierarchy();
        IClass rootClass = ch.getRootClass();
        putline("Root class: " + rootClass);
        Collection<IClass> subclasses = ch.getImmediateSubclasses(rootClass);
        for (IClass subclass : subclasses) {
            putline("  Subclass: " + subclass); 
            Collection<IClass> subsubclasses = ch.getImmediateSubclasses(subclass);
            for (IClass subsubclass : subsubclasses) {
                putline("    Subsubclass: " + subsubclass);
                Collection<IClass> subsubsubclasses = ch.getImmediateSubclasses(subsubclass);
                for (IClass subsubsubclass : subsubsubclasses) {
                    putline("      Subsubsubclass: " + subsubsubclass);
                }
            }
        }
        
        putline("Graph built!");
        putline("Max number is " + CG.getMaxNumber());
        for (CGNode node : CG) {
            putline("Method " + node.getMethod().getSignature());
            for (Iterator<CallSiteReference> iter = node.iterateCallSites(); iter.hasNext(); ) {
                CallSiteReference ref = iter.next();
                putline("  Calls " + ref.getDeclaredTarget().getSignature());
                Set<CGNode> possibleTargets = CG.getPossibleTargets(node, ref);
                for (CGNode target : possibleTargets) {
                    putline("    Possible target: " + target.getMethod());
                }
            }
        }
        putline("EOF");
    }

    private void addPrologue(PrintWriter writer) throws IOException {
        URL entry = Activator.getDefault().getBundle().getEntry("data/miniprologue.js");
        BufferedReader reader = new BufferedReader(new InputStreamReader(entry.openStream()));
        String line = reader.readLine();
        while (line != null) {
            writer.println(line);
            line = reader.readLine();
        }
        reader.close();
    }

    @Override
    public void setFocus() {
    }
    
}
