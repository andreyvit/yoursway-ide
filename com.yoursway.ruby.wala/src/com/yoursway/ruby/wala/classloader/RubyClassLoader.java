package com.yoursway.ruby.wala.classloader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ibm.wala.cast.loader.AstMethod.DebuggingInformation;
import com.ibm.wala.cast.loader.AstMethod.LexicalInformation;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstQualifier;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.impl.CAstImpl;
import com.ibm.wala.cfg.AbstractCFG;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;
import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.debug.Assertions;
import com.yoursway.ruby.wala.RubyCAst2IRTranslator;
import com.yoursway.ruby.wala.RubyTypes;
import com.yoursway.ruby.wala.ScriptEntity;

public class RubyClassLoader implements IClassLoader {
    
    public final static Language RUBY_LANGUAGE = new RubyLanguage();
    
    final Map<TypeName, IClass> types = HashMapFactory.make();
    
    static final Map<Selector, IMethod> emptyMap1 = Collections.emptyMap();
    static final Map<Atom, IField> emptyMap2 = Collections.emptyMap();
    
    final IClassHierarchy cha;
    
    public RubyClassLoader(IClassHierarchy cha) {
        this.cha = cha;
    }
    
    final Set<CAstQualifier> functionQualifiers;
    
    {
        functionQualifiers = HashSetFactory.make();
        functionQualifiers.add(CAstQualifier.PUBLIC);
        functionQualifiers.add(CAstQualifier.FINAL);
    }
    
    public IClass defineCodeBodyType(String name, TypeReference P,
            CAstSourcePositionMap.Position sourcePosition) {
        return new RubyCodeBody(this, TypeReference.findOrCreate(RubyTypes.rubyLoader, TypeName
                .string2TypeName(name)), P, this, sourcePosition);
    }
    
    public IClass defineFunctionType(String name, CAstSourcePositionMap.Position pos) {
        return defineCodeBodyType(name, RubyTypes.Function, pos);
    }
    
    public IClass defineScriptType(String name, CAstSourcePositionMap.Position pos) {
        return defineCodeBodyType(name, RubyTypes.Script, pos);
    }
    
    public IMethod defineCodeBodyCode(String clsName, AbstractCFG cfg, SymbolTable symtab,
            boolean hasCatchBlock, TypeReference[][] caughtTypes, LexicalInformation lexicalInfo,
            DebuggingInformation debugInfo) {
        RubyCodeBody C = (RubyCodeBody) lookupClass(clsName, cha);
        Assertions._assert(C != null, clsName);
        return C.setCodeBody(new RubyMethodObject(this, C, cfg, symtab, hasCatchBlock, caughtTypes,
                lexicalInfo, debugInfo));
    }
    
    final RubyRootClass ROOT = new RubyRootClass(this, this, null);
    
    final RubyClass UNDEFINED = new RubyClass(this, RubyTypes.Undefined, RubyTypes.Root,
            null);
    
    final RubyClass PRIMITIVES = new RubyClass(this, RubyTypes.Primitives, RubyTypes.Root,
            null);
    
    final RubyClass FAKEROOT = new RubyClass(this, RubyTypes.FakeRoot, RubyTypes.Root,
            null);
    
    final RubyClass STRING = new RubyClass(this, RubyTypes.String, RubyTypes.Root, null);
    
    final RubyClass NULL = new RubyClass(this, RubyTypes.Null, RubyTypes.Root, null);
    
//    final JavaScriptClass ARRAY = new JavaScriptClass(this, JavaScriptTypes.Array, JavaScriptTypes.Root, null);
    
    final RubyClass OBJECT = new RubyClass(this, RubyTypes.Object, RubyTypes.Root, null);
    
    final RubyClass TYPE_ERROR = new RubyClass(this, RubyTypes.TypeError, RubyTypes.Root,
            null);
    
    final RubyClass CODE_BODY = new RubyClass(this, RubyTypes.CodeBody, RubyTypes.Root,
            null);
    
    final RubyClass FUNCTION = new RubyClass(this, RubyTypes.Function, RubyTypes.CodeBody,
            null);
    
    final RubyClass SCRIPT = new RubyClass(this, RubyTypes.Script, RubyTypes.CodeBody,
            null);
    
//    final JavaScriptClass BOOLEAN = new JavaScriptClass(this, JavaScriptTypes.Boolean, JavaScriptTypes.Root,
//            null);
//    
//    final JavaScriptClass NUMBER = new JavaScriptClass(this, JavaScriptTypes.Number, JavaScriptTypes.Root,
//            null);
//    
//    final JavaScriptClass DATE = new JavaScriptClass(this, JavaScriptTypes.Date, JavaScriptTypes.Root, null);
//    
//    final JavaScriptClass REGEXP = new JavaScriptClass(this, JavaScriptTypes.RegExp, JavaScriptTypes.Root,
//            null);
//    
//    final JavaScriptClass BOOLEAN_OBJECT = new JavaScriptClass(this, JavaScriptTypes.BooleanObject,
//            JavaScriptTypes.Object, null);
//    
//    final JavaScriptClass NUMBER_OBJECT = new JavaScriptClass(this, JavaScriptTypes.NumberObject,
//            JavaScriptTypes.Object, null);
//    
//    final JavaScriptClass DATE_OBJECT = new JavaScriptClass(this, JavaScriptTypes.DateObject,
//            JavaScriptTypes.Object, null);
//    
//    final JavaScriptClass REGEXP_OBJECT = new JavaScriptClass(this, JavaScriptTypes.RegExpObject,
//            JavaScriptTypes.Object, null);
//    
    final RubyClass STRING_OBJECT = new RubyClass(this, RubyTypes.StringObject, RubyTypes.Object,
            null);
    
    public IClass lookupClass(String className, IClassHierarchy cha) {
        Assertions._assert(this.cha == cha);
        return types.get(TypeName.string2TypeName(className));
    }
    
    public IClass lookupClass(TypeName className) {
        return types.get(className);
    }
    
    public ClassLoaderReference getReference() {
        return RubyTypes.rubyLoader;
    }
    
    public Iterator<IClass> iterateAllClasses() {
        return types.values().iterator();
    }
    
    public int getNumberOfClasses() {
        return 0;
    }
    
    public Atom getName() {
        return getReference().getName();
    }
    
    public Language getLanguage() {
        return RUBY_LANGUAGE;
    }
    
    public int getNumberOfMethods() {
        return types.size();
    }
    
    public String getSourceFileName(IClass klass) {
        return klass.getSourceFileName();
    }
    
    public IClassLoader getParent() {
        // currently, JavaScript land does not interact with any other loaders
        Assertions.UNREACHABLE("RubyClassLoader.getParent() called?!?");
        return null;
    }
    
    public void init(Set<Module> modules) throws IOException {
        System.out.println(Arrays.toString(modules.toArray()));
        RubyCAst2IRTranslator translator = new RubyCAst2IRTranslator(this);
        CAstImpl a = new CAstImpl();
        ScriptEntity se = new ScriptEntity("foo", a.makeNode(CAstNode.BLOCK_STMT));
        translator.translate(se, "myscript.rb");
    }
    
    public void removeAll(Collection<IClass> toRemove) {
        Set<TypeName> keys = HashSetFactory.make();
        
        for (Map.Entry<TypeName, IClass> E : types.entrySet())
            if (toRemove.contains(E.getValue()))
                keys.add(E.getKey());
        
        for (Iterator<TypeName> KK = keys.iterator(); KK.hasNext();) {
            types.remove(KK.next());
        }
    }
    
}
