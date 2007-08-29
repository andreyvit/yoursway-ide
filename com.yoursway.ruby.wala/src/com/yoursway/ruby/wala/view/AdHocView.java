package com.yoursway.ruby.wala.view;

import static com.ibm.wala.cast.ipa.callgraph.Util.dumpCG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.ibm.wala.cast.ipa.callgraph.CAstAnalysisScope;
import com.ibm.wala.cast.ipa.callgraph.StandardFunctionTargetSelector;
import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.sun.tools.javac.tree.Tree.Modifiers;
import com.yoursway.js.wala.JSCFABuilder;
import com.yoursway.js.wala.JSZeroXCFABuilder;
import com.yoursway.js.wala.JavaScriptEntryPoints;
import com.yoursway.js.wala.JavaScriptLoader;
import com.yoursway.js.wala.JavaScriptLoaderFactory;
import com.yoursway.js.wala.JavaScriptTranslatorFactory;
import com.yoursway.js.wala.JavaScriptTypes;
import com.yoursway.js.wala.Util3;
import com.yoursway.ruby.wala.RubyCFABuilder;
import com.yoursway.ruby.wala.RubyClassLoaderFactory;
import com.yoursway.ruby.wala.RubyEntryPoints;
import com.yoursway.ruby.wala.RubyTypes;
import com.yoursway.ruby.wala.RubyZeroXCFABuilder;
import com.yoursway.ruby.wala.classloader.RubyClassLoader;
import com.yoursway.ruby.wala.internal.Activator;

public class AdHocView extends ViewPart {

    private Text text;
    private TreeViewer treeViewer;
    private Tree tree;
    
    private static AdHocView instance;
    
    public AdHocView() {
        instance = this;
    }

    @Override
    public void createPartControl(Composite parent) {
        getViewSite().getActionBars().getToolBarManager().add(new Action("Build Ruby") {
            
            @Override
            public void run() {
                try {
                    buildSomething2();
                } catch (Exception e) {
                    final Status status = new Status(Status.ERROR, "com.yoursway.ruby.wala", e.getMessage(), e);
                    Activator.getDefault().getLog().log(status);
                    ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            "Analysis fucked up", e.getMessage(), status);
                }
            }
            
        });
        getViewSite().getActionBars().getToolBarManager().add(new Action("Build JS") {
            
            @Override
            public void run() {
                try {
                    buildSomething2();
                } catch (Exception e) {
                    final Status status = new Status(Status.ERROR, "com.yoursway.ruby.wala", e.getMessage(), e);
                    Activator.getDefault().getLog().log(status);
                    ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            "Analysis fucked up", e.getMessage(), status);
                }
            }
            
        });
        //Root class: function Lsuperapp.js/f
        tree = new Tree(parent, SWT.V_SCROLL);
        final MenuManager menu = new MenuManager();
        menu.setRemoveAllWhenShown(true);
        tree.setMenu(menu.createContextMenu(tree));
        menu.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                menu.add(new Action("Copy to clipboard") {
                   @Override
                public void run() {
                       TreeItem[] items = tree.getSelection();
                       if (items.length < 1)
                           return;
                       String t = items[0].getText();
                       Clipboard clipboard = new Clipboard(Display.getDefault());
                       clipboard.setContents(new Object[]{t}, new Transfer[] { TextTransfer.getInstance()});
                       clipboard.dispose();
                } 
                });
            }
            
        });
    }
    
    TreeItem add(Object parent, String text) {
        TreeItem item;
        if (parent instanceof Tree)
            item = new TreeItem((Tree) parent, SWT.NONE);
        else
            item = new TreeItem((TreeItem) parent, SWT.NONE);
        item.setText(text);
        return item;
    }
    
    void clear() {
        tree.removeAll();
    }
    
    protected void buildSomething() throws Exception {
        clear();
        add(tree, "Starting work on "
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
        
        SourceFileModule sfm = new SourceFileModule(f, "tests/hello.rb");

        // can be static
        RubyClassLoaderFactory loaderFactory = new RubyClassLoaderFactory();
        CAstAnalysisScope scope = new CAstAnalysisScope(new SourceFileModule[] {sfm}, loaderFactory);
        IClassHierarchy classHierarchy = ClassHierarchy.make(scope, loaderFactory, RubyClassLoader.RUBY_LANGUAGE);
        Iterable<Entrypoint> roots = new RubyEntryPoints(classHierarchy, classHierarchy.getLoader(RubyTypes.rubyLoader));
        final boolean keepIRs = true;
        final AnalysisOptions options = new AnalysisOptions(scope, AstIRFactory.makeDefaultFactory(keepIRs), roots);
        com.ibm.wala.ipa.callgraph.impl.Util.addDefaultSelectors(options, classHierarchy);
        options.setSelector(new StandardFunctionTargetSelector(classHierarchy, options.getMethodTargetSelector()));
        options.setUseConstantSpecificKeys(true);
        options.setUseStacksForLexicalScoping(true);
        options.getSSAOptions().setPreserveNames(true);
        CallGraph CG;
        
        RubyCFABuilder builder = new RubyZeroXCFABuilder(classHierarchy, options, null, null, null, ZeroXInstanceKeys.ALLOCATIONS);
        CG = builder.makeCallGraph(builder.getOptions());

        // this seems to be for Java only, and does not work
//        ZeroOneCFABuilderFactory zof = new ZeroOneCFABuilderFactory();
//        CallGraphBuilder builder2 = zof.make(options, classHierarchy, scope, true);
//        CG = builder2.makeCallGraph(options);
        
        dumpCG(builder, CG);
        
        String fname = "tests/hello.rb/f";
        TreeItem fItem = add(tree, "*** " + fname);
        Collection<CGNode> nodes = Util3.getNodes(CG, fname);
        for(CGNode node : nodes) {
            TreeItem item = add(fItem, "Node: " + node);
        }
        
        
        final IClassHierarchy ch = CG.getClassHierarchy();
        IClass rootClass = ch.getRootClass();
        dumpClassHier(tree, ch, rootClass);
        
        for (CGNode node : CG) {
            TreeItem methodItem = add(tree, "Method " + node.getMethod().getSignature());
            for (Iterator<CallSiteReference> iter = node.iterateCallSites(); iter.hasNext(); ) {
                CallSiteReference ref = iter.next();
                TreeItem callItem = add(methodItem, "Calls " + ref.getDeclaredTarget().getSignature());
                Set<CGNode> possibleTargets = CG.getPossibleTargets(node, ref);
                for (CGNode target : possibleTargets) {
                    add(callItem, "Possible target: " + target.getMethod());
                }
            }
        }
        add(tree, "EOF");
    }
    
    protected void buildSomething2() throws Exception {
        clear();
        add(tree, "Starting work on "
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
        
        SourceFileModule sfm = new SourceFileModule(f, "tests/suki.js");
        
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
//        ZeroOneCFABuilderFactory zof = new ZeroOneCFABuilderFactory();
//        CallGraphBuilder builder2 = zof.make(options, classHierarchy, scope, true);
//        CG = builder2.makeCallGraph(options);
        
        dumpCG(builder, CG);
        
        String fname = "test.js/f";
        TreeItem fItem = add(tree, "*** " + fname);
        Collection<CGNode> nodes = Util3.getNodes(CG, fname);
        for(CGNode node : nodes) {
            TreeItem item = add(fItem, "Node: " + node);
        }
        
        
        final IClassHierarchy ch = CG.getClassHierarchy();
        IClass rootClass = ch.getRootClass();
        dumpClassHier(tree, ch, rootClass);
        
        for (CGNode node : CG) {
            TreeItem methodItem = add(tree, "Method " + node.getMethod().getSignature());
            for (Iterator<CallSiteReference> iter = node.iterateCallSites(); iter.hasNext(); ) {
                CallSiteReference ref = iter.next();
                TreeItem callItem = add(methodItem, "Calls " + ref.getDeclaredTarget().getSignature());
                Set<CGNode> possibleTargets = CG.getPossibleTargets(node, ref);
                for (CGNode target : possibleTargets) {
                    add(callItem, "Possible target: " + target.getMethod());
                }
            }
        }
        add(tree, "EOF");
    }

    private void dumpClassHier(Object parent, final IClassHierarchy ch, IClass rootClass) {
        TreeItem item = add(parent, "Root class: " + rootClass);
        Collection<IClass> subclasses = ch.getImmediateSubclasses(rootClass);
        for (IClass subclass : subclasses) {
            dumpClassHier(item, ch, subclass);
        }
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
    
    public void dumpEntity(Object parent, CAstEntity entity) {
        String kind = decypherConstant(CAstEntity.class, entity.getKind());
        TreeItem root = add(tree, kind + " " + entity.getName());
        Map<CAstNode, Collection<CAstEntity>> subents = entity.getAllScopedEntities();
        if (subents.size() > 0) {
            TreeItem subentItem = add(root, "Subentities");
            for(Collection<CAstEntity> subsubents : subents.values()) {
                for (CAstEntity subent : subsubents)
                    dumpEntity(subentItem, subent);
            }
        }
        CAstNode ast = entity.getAST();
        if (ast != null) 
            dumpAST(root, ast);
    }
    
    private void dumpAST(Object parent, CAstNode node) {
        String kind = decypherConstant(CAstNode.class, node.getKind());
        String value = (node.getValue() == null ? null : node.getValue().toString() + " (" + 
                node.getValue().getClass().getSimpleName() + ")");
        String desc = (value == null ? kind : kind + " " + value);
        TreeItem item = add(parent, desc);
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) 
            dumpAST(item, node.getChild(i));
    }
    
    private String decypherConstant(Class<?> klass, int value) {
        final int mods = Modifier.STATIC;
        for (Field field : klass.getDeclaredFields()) {
            if ((field.getModifiers() & mods) == mods) {
                if (field.getType() == int.class) {
                    field.setAccessible(true);
                    try {
                        int fvalue = (Integer) field.get(null);
                        if (fvalue == value)
                            return field.getName();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "UNK:" + value;
    }
    
    public static void translatingEntity(CAstEntity entity, String module) {
        instance.dumpEntity(instance.tree, entity);
    }
    
}
