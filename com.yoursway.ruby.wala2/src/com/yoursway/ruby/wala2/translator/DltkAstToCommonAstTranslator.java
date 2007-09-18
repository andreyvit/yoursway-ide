package com.yoursway.ruby.wala2.translator;

import java.io.InputStream;
import java.util.Collection;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ruby.internal.parser.JRubySourceParser;

import com.ibm.wala.cast.tree.CAst;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.classLoader.ModuleEntry;
import com.ibm.wala.util.debug.Trace;
import com.ibm.wala.util.io.Streams;
import com.yoursway.rails.commons.RubyAstTraverser;

/**
 * Produces a WALA common AST (CAst) from a DLTK AST. (Also temporarily produces
 * a DLTK AST from file source using {@link JRubySourceParser}. As soon as this
 * work will be integrated into the IDE should switch to obtaining a cached AST
 * from DLTK, and probably not in this class.)
 * 
 * @author Andrey Tarantsov
 */
public class DltkAstToCommonAstTranslator {
    
    static final boolean DEBUG = true;
    
    public CAstEntity translate() throws java.io.IOException {
        if (DEBUG)
            Trace.println("translating " + scriptName + " with JRuby");
        
        final InputStream inputStream = sourceModule.getInputStream();
        byte[] inputData = Streams.inputStream2ByteArray(inputStream);
        String inputString = new String(inputData, "utf-8");
        JRubySourceParser parser = new JRubySourceParser();
        ModuleDeclaration module = parser.parse(inputString);
        
        RubyAstTraverser traverser = new RubyAstTraverser();
        RootState state = new RootState(scriptName, new Context(Ast));
        traverser.traverse(module, state);
        return state.getModuleEntity();
    }
    
    private final CAst Ast;
    
    final String scriptName;
    
    private final ModuleEntry sourceModule;
    
    public DltkAstToCommonAstTranslator(CAst Ast, ModuleEntry M, String scriptName) {
        this.Ast = Ast;
        this.scriptName = scriptName;
        this.sourceModule = M;
    }
    
    static CAstNode[] toArray(final Collection<CAstNode> nodes) {
        return nodes.toArray(new CAstNode[nodes.size()]);
    }
    
}