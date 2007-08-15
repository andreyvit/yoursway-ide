package com.yoursway.ruby.wala;

import static com.ibm.wala.cast.ipa.callgraph.Util.dumpCG;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
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
import com.ibm.wala.util.io.Streams;
import com.yoursway.js.wala.Util3;
import com.yoursway.ruby.wala.classloader.RubyClassLoader;
import com.yoursway.ruby.wala.indentedwriter.IndentedPrintWriter;
import com.yoursway.ruby.wala.internal.Activator;

public class TestApplication implements IApplication {
    
    private static final IndentedPrintWriter o = new IndentedPrintWriter(new OutputStreamWriter(System.out), true);
    
    public Object start(IApplicationContext context) throws Exception {
        buildSomething();
//        testWriter();
        return null;
    }

    private void testWriter() {
        o.println("Something");
        IndentedPrintWriter a = o.indent();
        a.println("Child");
        o.print("Smt else");
        IndentedPrintWriter b = o.indent();
        b.println("Child 2");
        b.println("Child 3");
        b.print("x");
        b.print("y");
        a.print("z");
    }
    
    public void stop() {
    }
    
    public static IndentedPrintWriter add(IndentedPrintWriter w, String s) {
        w.println(s);
        return w.indent();
    }
    
    protected void buildSomething() throws Exception {
        add(o, "Starting work on "
                + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).format(
                        Calendar.getInstance().getTime()));
        File f = new File("/tmp/test.js");
        PrintWriter writer = new PrintWriter(new FileWriter(f));
//        addPrologue(writer);
        
//        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//        if (activeEditor == null)
//            return;
//        if (!(activeEditor instanceof AbstractTextEditor))
//            return;
//        AbstractTextEditor textEditor = (AbstractTextEditor) activeEditor;
//        Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
//        method.setAccessible(true);
//        ISourceViewer sourceViewer = (ISourceViewer) method.invoke(textEditor);
//        String text = sourceViewer.getTextWidget().getText();
        
        URL entry = Activator.getDefault().getBundle().getEntry("data/test.rb");
        
        InputStream is = entry.openStream();
        byte[] ba = Streams.inputStream2ByteArray(is);
        is.close();
        String text = new String(ba);
        
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
        IndentedPrintWriter fItem = add(o, "*** " + fname);
        Collection<CGNode> nodes = Util3.getNodes(CG, fname);
        for(CGNode node : nodes) {
            IndentedPrintWriter item = add(fItem, "Node: " + node);
        }
        
        
        final IClassHierarchy ch = CG.getClassHierarchy();
        IClass rootClass = ch.getRootClass();
        dumpClassHier(o, ch, rootClass);
        
        for (CGNode node : CG) {
            IndentedPrintWriter methodItem = add(o, "Method " + node.getMethod().getSignature());
            for (Iterator<CallSiteReference> iter = node.iterateCallSites(); iter.hasNext(); ) {
                CallSiteReference ref = iter.next();
                IndentedPrintWriter callItem = add(methodItem, "Calls " + ref.getDeclaredTarget().getSignature());
                Set<CGNode> possibleTargets = CG.getPossibleTargets(node, ref);
                for (CGNode target : possibleTargets) {
                    add(callItem, "Possible target: " + target.getMethod());
                }
            }
        }
        add(o, "EOF");
    }

    private void dumpClassHier(IndentedPrintWriter parent, final IClassHierarchy ch, IClass rootClass) {
        IndentedPrintWriter item = add(parent, "Root class: " + rootClass);
        Collection<IClass> subclasses = ch.getImmediateSubclasses(rootClass);
        for (IClass subclass : subclasses) {
            dumpClassHier(item, ch, subclass);
        }
    }

    public static void dumpEntity(IndentedPrintWriter parent, CAstEntity entity) {
        String kind = decypherConstant(CAstEntity.class, entity.getKind());
        IndentedPrintWriter root = add(parent, kind + " " + entity.getName());
        Map<CAstNode, Collection<CAstEntity>> subents = entity.getAllScopedEntities();
        if (subents.size() > 0) {
            IndentedPrintWriter subentItem = add(parent, "Subentities");
            for(Collection<CAstEntity> subsubents : subents.values()) {
                for (CAstEntity subent : subsubents)
                    dumpEntity(subentItem, subent);
            }
        }
        CAstNode ast = entity.getAST();
        if (ast != null) 
            dumpAST(root, ast);
    }
    
    private static void dumpAST(IndentedPrintWriter parent, CAstNode node) {
        String kind = decypherConstant(CAstNode.class, node.getKind());
        String value = (node.getValue() == null ? null : node.getValue().toString() + " (" + 
                node.getValue().getClass().getSimpleName() + ")");
        String desc = (value == null ? kind : kind + " " + value);
        IndentedPrintWriter item = add(parent, desc);
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) 
            dumpAST(item, node.getChild(i));
    }
    
    private static String decypherConstant(Class<?> klass, int value) {
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
        dumpEntity(o, entity);
    }
 
}
