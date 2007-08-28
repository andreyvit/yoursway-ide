//package com.yoursway.ruby.wala.translator;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.mozilla.javascript.FunctionNode;
//import org.mozilla.javascript.Node;
//import org.mozilla.javascript.ScriptOrFnNode;
//import org.mozilla.javascript.Token;
//
//import com.ibm.wala.cast.tree.CAstControlFlowMap;
//import com.ibm.wala.cast.tree.CAstEntity;
//import com.ibm.wala.cast.tree.CAstNode;
//import com.ibm.wala.cast.tree.CAstSourcePositionMap;
//import com.ibm.wala.cast.tree.impl.CAstControlFlowRecorder;
//import com.ibm.wala.cast.tree.impl.CAstOperator;
//import com.ibm.wala.cast.tree.impl.CAstSourcePositionRecorder;
//import com.ibm.wala.cast.tree.impl.CAstSymbolImpl;
//import com.ibm.wala.cast.tree.impl.LineNumberPosition;
//import com.ibm.wala.classLoader.SourceFileModule;
//import com.ibm.wala.classLoader.SourceURLModule;
//import com.ibm.wala.util.collections.HashMapFactory;
//import com.ibm.wala.util.collections.HashSetFactory;
//import com.ibm.wala.util.debug.Assertions;
//import com.yoursway.ruby.wala.entities.ScriptOrFuncEntity;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.BaseCollectingContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.CatchBlockContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.DelegatingContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.ExpressionContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.FunctionContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.LoopContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.ScriptContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.TryBlockContext;
//import com.yoursway.ruby.wala.translator.DltkAstToCommonAstTranslator.WalkContext;
//
//public class DltkAstToCommon_JsCodeStorage {
//   private final boolean DEBUG = true;
//    
//    private interface WalkContext {
//        
//        String script();
//        
//        ScriptOrFnNode top();
//        
//        void addScopedEntity(CAstNode construct, CAstEntity e);
//        
//        Map<CAstNode, HashSet<CAstEntity>> getScopedEntities();
//        
//        boolean expressionContext();
//        
//        CAstControlFlowRecorder cfg();
//        
//        CAstSourcePositionRecorder pos();
//        
//        CAstNode getCatchTarget();
//        
//        CAstNode setBase(Node node);
//        
//        boolean foundBase(Node node);
//        
//        void copyBase(Node from, Node to);
//        
//        String getCatchVar();
//        
//        void setCatchVar(String name);
//        
//        String getForInVar(String loopVarName, Node initExpr);
//        
//        String getForInInitVar();
//        
//        String getForInLoopVar();
//        
//        void addInitializer(CAstNode n);
//    }
//    
//    private static class RootContext implements WalkContext {
//        
//        public String script() {
//            return null;
//        }
//        
//        public ScriptOrFnNode top() {
//            return null;
//        }
//        
//        public void addScopedEntity(CAstNode construct, CAstEntity e) {
//        }
//        
//        public Map<CAstNode, HashSet<CAstEntity>> getScopedEntities() {
//            return null;
//        }
//        
//        public boolean expressionContext() {
//            return false;
//        }
//        
//        public CAstControlFlowRecorder cfg() {
//            return null;
//        }
//        
//        public CAstSourcePositionRecorder pos() {
//            return null;
//        }
//        
//        public CAstNode getCatchTarget() {
//            return null;
//        }
//        
//        public CAstNode setBase(Node node) {
//            return null;
//        }
//        
//        public boolean foundBase(Node node) {
//            return false;
//        }
//        
//        public void copyBase(Node from, Node to) {
//        }
//        
//        public String getCatchVar() {
//            return null;
//        }
//        
//        public void setCatchVar(String name) {
//        }
//        
//        public String getForInVar(String loopVarName, Node initExpr) {
//            return null;
//        }
//        
//        public String getForInInitVar() {
//            return null;
//        }
//        
//        public String getForInLoopVar() {
//            return null;
//        }
//        
//        public void addInitializer(CAstNode n) {
//        }
//    }
//    
//    private static abstract class DelegatingContext implements WalkContext {
//        private final WalkContext parent;
//        
//        DelegatingContext(WalkContext parent) {
//            this.parent = parent;
//        }
//        
//        public String script() {
//            return parent.script();
//        }
//        
//        public ScriptOrFnNode top() {
//            return parent.top();
//        }
//        
//        public void addScopedEntity(CAstNode construct, CAstEntity e) {
//            parent.addScopedEntity(construct, e);
//        }
//        
//        public Map<CAstNode, HashSet<CAstEntity>> getScopedEntities() {
//            return parent.getScopedEntities();
//        }
//        
//        public boolean expressionContext() {
//            return parent.expressionContext();
//        }
//        
//        public CAstControlFlowRecorder cfg() {
//            return parent.cfg();
//        }
//        
//        public CAstSourcePositionRecorder pos() {
//            return parent.pos();
//        }
//        
//        public CAstNode getCatchTarget() {
//            return parent.getCatchTarget();
//        }
//        
//        public CAstNode setBase(Node node) {
//            return parent.setBase(node);
//        }
//        
//        public boolean foundBase(Node node) {
//            return parent.foundBase(node);
//        }
//        
//        public void copyBase(Node from, Node to) {
//            parent.copyBase(from, to);
//        }
//        
//        public String getCatchVar() {
//            return parent.getCatchVar();
//        }
//        
//        public void setCatchVar(String name) {
//            parent.setCatchVar(name);
//        }
//        
//        public String getForInVar(String loopVarName, Node initExpr) {
//            return parent.getForInVar(loopVarName, initExpr);
//        }
//        
//        public String getForInInitVar() {
//            return parent.getForInInitVar();
//        }
//        
//        public String getForInLoopVar() {
//            return parent.getForInLoopVar();
//        }
//        
//        public void addInitializer(CAstNode n) {
//            parent.addInitializer(n);
//        }
//    }
//    
//    private static class FunctionContext extends DelegatingContext {
//        private final ScriptOrFnNode topNode;
//        
//        private final Map<CAstNode, HashSet<CAstEntity>> scopedEntities = HashMapFactory.make();
//        
//        private final List<CAstNode> initializers = new ArrayList<CAstNode>();
//        
//        private final CAstSourcePositionRecorder pos = new CAstSourcePositionRecorder();
//        
//        private final CAstControlFlowRecorder cfg = new CAstControlFlowRecorder(pos);
//        
//        FunctionContext(WalkContext parent, ScriptOrFnNode s) {
//            super(parent);
//            this.topNode = s;
//        }
//        
//        public ScriptOrFnNode top() {
//            return topNode;
//        }
//        
//        public void addScopedEntity(CAstNode construct, CAstEntity e) {
//            if (!scopedEntities.containsKey(construct)) {
//                HashSet<CAstEntity> s = HashSetFactory.make();
//                scopedEntities.put(construct, s);
//            }
//            
//            scopedEntities.get(construct).add(e);
//        }
//        
//        public Map<CAstNode, HashSet<CAstEntity>> getScopedEntities() {
//            return scopedEntities;
//        }
//        
//        public boolean expressionContext() {
//            return false;
//        }
//        
//        public CAstControlFlowRecorder cfg() {
//            return cfg;
//        }
//        
//        public CAstSourcePositionRecorder pos() {
//            return pos;
//        }
//        
//        public String getForInVar(String loopVarName, Node initExpr) {
//            return null;
//        }
//        
//        public String getForInInitVar() {
//            return null;
//        }
//        
//        public String getForInLoopVar() {
//            return null;
//        }
//        
//        public void addInitializer(CAstNode n) {
//            initializers.add(n);
//        }
//        
//    }
//    
//    private static class ScriptContext extends FunctionContext {
//        private final String script;
//        
//        ScriptContext(WalkContext parent, ScriptOrFnNode s, String script) {
//            super(parent, s);
//            this.script = script;
//        }
//        
//        public String script() {
//            return script;
//        }
//    }
//    
//    private static class ExpressionContext extends DelegatingContext {
//        
//        ExpressionContext(WalkContext parent) {
//            super(parent);
//        }
//        
//        public boolean expressionContext() {
//            return true;
//        }
//        
//    }
//    
//    private static class CatchBlockContext extends DelegatingContext {
//        private String catchVarName;
//        
//        CatchBlockContext(WalkContext parent) {
//            super(parent);
//        }
//        
//        public String getCatchVar() {
//            return catchVarName;
//        }
//        
//        public void setCatchVar(String name) {
//            catchVarName = name;
//        }
//    }
//    
//    private static class TryBlockContext extends DelegatingContext {
//        private final CAstNode catchNode;
//        
//        TryBlockContext(WalkContext parent, CAstNode catchNode) {
//            super(parent);
//            this.catchNode = catchNode;
//        }
//        
//        public CAstNode getCatchTarget() {
//            return catchNode;
//        }
//        
//    }
//    
//    private static class BaseCollectingContext extends DelegatingContext {
//        private final Set<Node> baseFor = HashSetFactory.make();
//        
//        private final CAstNode baseVar;
//        
//        private boolean foundBase = false;
//        
//        BaseCollectingContext(WalkContext parent, Node initialBaseFor, CAstNode baseVar) {
//            super(parent);
//            baseFor.add(initialBaseFor);
//            this.baseVar = baseVar;
//        }
//        
//        public CAstNode setBase(Node node) {
//            if (baseFor.contains(node)) {
//                foundBase = true;
//                return baseVar;
//            } else {
//                return null;
//            }
//        }
//        
//        public boolean foundBase(Node node) {
//            return foundBase;
//        }
//        
//        public void copyBase(Node from, Node to) {
//            if (baseFor.contains(from))
//                baseFor.add(to);
//        }
//    }
//    
//    private static class LoopContext extends DelegatingContext {
//        private static int counter = 0;
//        
//        private String loopVarName;
//        
//        private String forInVar = null;
//        
//        private Node forInInitExpr = null;
//        
//        private LoopContext(WalkContext parent) {
//            super(parent);
//        }
//        
//        public String getForInVar(String loopVarName, Node initExpr) {
//            this.loopVarName = loopVarName;
//            this.forInVar = "_forin_tmp" + counter++;
//            this.forInInitExpr = initExpr;
//            return forInVar;
//        }
//        
//        public String getForInInitVar() {
//            Assertions._assert(forInVar != null);
//            return forInVar;
//        }
//        
//        public String getForInLoopVar() {
//            Assertions._assert(loopVarName != null);
//            return loopVarName;
//        }
//        
//    }
//    
//    private CAstNode makeBuiltinNew(WalkContext context, String typeName) {
//        return Ast.makeNode(CAstNode.NEW, Ast.makeConstant(typeName));
//    }
//    
//    private CAstNode handleNew(WalkContext context, String globalName, Node arguments) {
//        return handleNew(context, Ast.makeNode(CAstNode.VAR, Ast.makeConstant(globalName)), arguments);
//    }
//    
//    private CAstNode handleNew(WalkContext context, CAstNode value, Node arguments) {
//        return makeCtorCall(value, arguments, context);
//    }
//    
//    private boolean isPrologueScript(WalkContext context) {
//        return false; //TranslatorBase.bootstrapFileNames.contains(context.script());
//    }
//    
//    private boolean isPrimitiveCall(WalkContext context, Node n) {
//        return isPrologueScript(context) && n.getType() == Token.CALL
//                && n.getFirstChild().getType() == Token.NAME
//                && n.getFirstChild().getString().equals("primitive");
//    }
//    
//    private boolean isPrimitiveCreation(WalkContext context, Node n) {
//        return isPrologueScript(context) && n.getType() == Token.NEW
//                && n.getFirstChild().getType() == Token.NAME
//                && n.getFirstChild().getString().equals("Primitives");
//    }
//    
//    private CAstNode makeCall(CAstNode fun, CAstNode thisptr, Node firstChild, WalkContext context) {
//        return makeCall(fun, thisptr, firstChild, context, "do");
//    }
//    
//    private CAstNode makeCtorCall(CAstNode thisptr, Node firstChild, WalkContext context) {
//        return makeCall(thisptr, null, firstChild, context, "ctor");
//    }
//    
//    private CAstNode makeCall(CAstNode fun, CAstNode thisptr, Node firstChild, WalkContext context,
//            String callee) {
//        int children = 0;
//        for (Node c = firstChild; c != null; c = c.getNext(), children++)
//            ;
//        
//        int nargs = (thisptr == null) ? children + 2 : children + 3;
//        int i = 0;
//        CAstNode arguments[] = new CAstNode[nargs];
//        arguments[i++] = fun;
//        arguments[i++] = Ast.makeConstant(callee);
//        if (thisptr != null)
//            arguments[i++] = thisptr;
//        for (Node arg = firstChild; arg != null; arg = arg.getNext())
//            arguments[i++] = walkNodes(arg, context);
//        
//        CAstNode call = Ast.makeNode(CAstNode.CALL, arguments);
//        
//        context.cfg().map(call, call);
//        if (context.getCatchTarget() != null) {
//            context.cfg().add(call, context.getCatchTarget(), null);
//        }
//        
//        return call;
//    }
//    
//    // TODO: SJF ... i put unchecked to make the generics compile; but this
//    // code is fishy.   This should be cleaned up and the unchecked suppressWarnings
//    // should be removed.
//    private CAstEntity walkEntity(final ScriptOrFnNode n, WalkContext context) {
//        // converted
//    }
//    
//    // TODO: SJF ... i put unchecked to make the generics compile; but this
//    // code is fishy.   This should be cleaned up and the unchecked suppressWarnings
//    // should be removed.
//    private CAstEntity walkMethod(final ScriptOrFnNode n, WalkContext context) {
//        final FunctionContext child = (n instanceof FunctionNode) ? new FunctionContext(context,
//                (FunctionNode) n) : new ScriptContext(context, n, n.getSourceName());
//        
//        CAstNode[] stmts = gatherChildren(n, child);
//        
//        // add initializers, if any
//        if (!child.initializers.isEmpty()) {
//            CAstNode[] newStmts = new CAstNode[stmts.length + 1];
//            
//            newStmts[0] = Ast.makeNode(CAstNode.BLOCK_STMT, child.initializers
//                    .toArray(new CAstNode[child.initializers.size()]));
//            
//            for (int i = 0; i < stmts.length; i++)
//                newStmts[i + 1] = stmts[i];
//            
//            stmts = newStmts;
//        }
//        
//        final CAstNode ast = Ast.makeNode(CAstNode.BLOCK_STMT, stmts);
//        final CAstControlFlowMap map = child.cfg();
//        final CAstSourcePositionMap pos = child.pos();
//        
//        final Map<CAstNode, Collection<CAstEntity>> subs = HashMapFactory.make();
//        for (Iterator<CAstNode> keys = child.getScopedEntities().keySet().iterator(); keys.hasNext();) {
//            CAstNode k = keys.next();
//            Object v = child.getScopedEntities().get(k);
//            if (v instanceof Collection)
//                subs.put(k, (Set<CAstEntity>) v);
//            else {
//                Set<CAstEntity> s = (Set<CAstEntity>) Collections.singleton((CAstEntity) v);
//                subs.put(k, s);
//            }
//        }
//        
//        return new ScriptOrFuncEntity(subs, ast, n, pos, map);
//    }
//    
//    private CAstNode[] gatherSiblings(Node n, WalkContext context) {
//        int cnt = 0;
//        for (Node c = n; c != null; cnt++, c = c.getNext())
//            ;
//        
//        CAstNode[] result = new CAstNode[cnt];
//        for (int i = 0; i < result.length; i++, n = n.getNext()) {
//            result[i] = walkNodes(n, context);
//        }
//        
//        return result;
//    }
//    
//    private CAstNode[] gatherChildren(Node n, WalkContext context, int skip) {
//        Node c = n.getFirstChild();
//        while (skip-- > 0)
//            c = c.getNext();
//        return gatherSiblings(c, context);
//    }
//    
//    private CAstNode[] gatherChildren(Node n, WalkContext context) {
//        return gatherSiblings(n.getFirstChild(), context);
//    }
//    
//    private CAstNode walkNodes(final Node n, WalkContext context) {
//        return noteSourcePosition(context, walkNodesInternal(n, context), n);
//    }
//    
//    private CAstSourcePositionMap.Position makePosition(Node n) {
//        URL url;
//        if (sourceModule instanceof SourceFileModule) {
//            try {
//                url = new URL("file://" + ((SourceFileModule) sourceModule).getFile());
//            } catch (MalformedURLException e) {
//                Assertions.UNREACHABLE();
//                return null;
//            }
//        } else {
//            url = ((SourceURLModule) sourceModule).getURL();
//        }
//        
//        return new LineNumberPosition(url, url, n.getLineno());
//    }
//    
//    private CAstNode noteSourcePosition(WalkContext context, CAstNode n, Node p) {
//        if (p.getLineno() != -1 && context.pos().getPosition(n) == null) {
//            context.pos().setPosition(n, makePosition(p));
//        }
//        return n;
//    }
//    
//    private CAstNode walkNodesInternal(final Node n, WalkContext context) {
//        int NT = n.getType();
//        switch (NT) {
//        
//        case Token.FUNCTION: {
//            // converted
//        }
//            
//        case Token.CATCH_SCOPE: {
//            Node catchVarNode = n.getFirstChild();
//            String catchVarName = catchVarNode.getString();
//            Assertions._assert(catchVarName != null);
//            context.setCatchVar(catchVarName);
//            return Ast.makeNode(CAstNode.EMPTY);
//        }
//            
//        case Token.LOCAL_BLOCK: {
//            return Ast.makeNode(CAstNode.BLOCK_EXPR, gatherChildren(n, context));
//        }
//            
//        case Token.TRY: {
//            Node catchNode = ((Node.Jump) n).target;
//            Node finallyNode = ((Node.Jump) n).getFinally();
//            
//            ArrayList<Node> tryList = new ArrayList<Node>();
//            ArrayList<Node> catchList = new ArrayList<Node>();
//            ArrayList<Node> finallyList = new ArrayList<Node>();
//            ArrayList<Node> current = tryList;
//            Node c;
//            for (c = n.getFirstChild(); c.getNext() != null; c = c.getNext()) {
//                if (c == catchNode) {
//                    current = catchList;
//                } else if (c == finallyNode) {
//                    current = finallyList;
//                }
//                
//                if (c.getType() == Token.GOTO &&
//                // ((Node.Jump)c).target == lastChildNode &&
//                        (c.getNext() == catchNode || c.getNext() == finallyNode)) {
//                    continue;
//                }
//                
//                current.add(c);
//            }
//            
//            CAstNode finallyBlock = null;
//            if (finallyNode != null) {
//                int i = 0;
//                CAstNode[] finallyAsts = new CAstNode[finallyList.size()];
//                for (Iterator<Node> fns = finallyList.iterator(); fns.hasNext();) {
//                    finallyAsts[i++] = walkNodes(fns.next(), context);
//                }
//                finallyBlock = Ast.makeNode(CAstNode.BLOCK_STMT, finallyAsts);
//            }
//            
//            if (catchNode != null) {
//                
//                int i = 0;
//                WalkContext catchChild = new CatchBlockContext(context);
//                CAstNode[] catchAsts = new CAstNode[catchList.size()];
//                for (Iterator<Node> cns = catchList.iterator(); cns.hasNext();) {
//                    catchAsts[i++] = walkNodes(cns.next(), catchChild);
//                }
//                CAstNode catchBlock = Ast.makeNode(CAstNode.CATCH,
//                        Ast.makeConstant(catchChild.getCatchVar()), Ast.makeNode(CAstNode.BLOCK_STMT,
//                                catchAsts));
//                context.cfg().map(catchBlock, catchBlock);
//                
//                i = 0;
//                WalkContext tryChild = new TryBlockContext(context, catchBlock);
//                CAstNode[] tryAsts = new CAstNode[tryList.size()];
//                for (Iterator<Node> tns = tryList.iterator(); tns.hasNext();) {
//                    tryAsts[i++] = walkNodes(tns.next(), tryChild);
//                }
//                CAstNode tryBlock = Ast.makeNode(CAstNode.BLOCK_STMT, tryAsts);
//                
//                if (finallyBlock != null) {
//                    return Ast.makeNode(CAstNode.BLOCK_STMT, Ast.makeNode(CAstNode.UNWIND, Ast.makeNode(
//                            CAstNode.TRY, tryBlock, catchBlock), finallyBlock), walkNodes(c, context));
//                } else {
//                    return Ast.makeNode(CAstNode.BLOCK_STMT,
//                            Ast.makeNode(CAstNode.TRY, tryBlock, catchBlock), walkNodes(c, context));
//                }
//                
//            } else {
//                int i = 0;
//                CAstNode[] tryAsts = new CAstNode[tryList.size()];
//                for (Iterator<Node> tns = tryList.iterator(); tns.hasNext();) {
//                    tryAsts[i++] = walkNodes(tns.next(), context);
//                }
//                CAstNode tryBlock = Ast.makeNode(CAstNode.BLOCK_STMT, tryAsts);
//                
//                return Ast.makeNode(CAstNode.BLOCK_STMT, Ast.makeNode(CAstNode.UNWIND, Ast.makeNode(
//                        CAstNode.BLOCK_STMT, tryBlock), Ast.makeNode(CAstNode.BLOCK_STMT, finallyBlock)),
//                        walkNodes(c, context));
//            }
//        }
//            
//        case Token.JSR: {
//            return Ast.makeNode(CAstNode.EMPTY);
//            /*
//             * Node jsrTarget = ((Node.Jump)n).target; Node finallyNode =
//             * jsrTarget.getNext(); return walkNodes(finallyNode, context);
//             */
//        }
//            
//        case Token.COMMA: {
//            int count = 0;
//            for (Node c = n.getFirstChild(); c != null; count++, c = c.getNext())
//                ;
//            
//            CAstNode[] cs = new CAstNode[count];
//            int i = 0;
//            for (Node c = n.getFirstChild(); c != null; i++, c = c.getNext()) {
//                if (c.getNext() == null) {
//                    context.copyBase(n, c);
//                }
//                cs[i] = walkNodes(c, context);
//            }
//            
//            return Ast.makeNode(CAstNode.BLOCK_EXPR, cs);
//        }
//            
//        case Token.ENTERWITH:
//        case Token.LEAVEWITH: {
//            return Ast.makeNode(CAstNode.EMPTY);
//        }
//            
//        case Token.LOOP: {
//            LoopContext child = new LoopContext(context);
//            CAstNode[] nodes = gatherChildren(n, child);
//            
//            if (child.forInInitExpr != null) {
//                String nm = child.forInVar;
//                return Ast.makeNode(CAstNode.BLOCK_STMT,
//                        Ast.makeNode(CAstNode.DECL_STMT, Ast.makeConstant(new CAstSymbolImpl(nm, true)),
//                                walkNodes(child.forInInitExpr, context)), nodes);
//            } else {
//                return Ast.makeNode(CAstNode.BLOCK_STMT, nodes);
//            }
//        }
//            
//        case Token.WITH:
//        case Token.FINALLY:
//        case Token.BLOCK:
//        case Token.LABEL: {
//            Node c1 = n.getFirstChild();
//            if (c1 != null && c1.getType() == Token.SWITCH) {
//                Node switchValue = c1.getFirstChild();
//                
//                CAstNode defaultLabel = Ast.makeNode(CAstNode.LABEL_STMT, Ast.makeNode(CAstNode.EMPTY));
//                context.cfg().map(defaultLabel, defaultLabel);
//                
//                for (Node kase = switchValue.getNext(); kase != null; kase = kase.getNext()) {
//                    Assertions._assert(kase.getType() == Token.CASE);
//                    Node caseLbl = kase.getFirstChild();
//                    Node target = ((Node.Jump) kase).target;
//                    context.cfg().add(c1, target, walkNodes(caseLbl, context));
//                }
//                
//                context.cfg().add(c1, defaultLabel, CAstControlFlowMap.SWITCH_DEFAULT);
//                
//                CAstNode switchAst = Ast.makeNode(CAstNode.SWITCH, walkNodes(switchValue, context), Ast
//                        .makeNode(CAstNode.BLOCK_STMT, defaultLabel, gatherChildren(n, context, 1)));
//                
//                noteSourcePosition(context, switchAst, c1);
//                context.cfg().map(c1, switchAst);
//                return switchAst;
//            }
//
//            else {
//                return Ast.makeNode(CAstNode.BLOCK_STMT, gatherChildren(n, context));
//            }
//        }
//            
//        case Token.EXPR_VOID:
//        case Token.EXPR_RESULT:
//        case Token.POS: {
//            WalkContext child = new ExpressionContext(context);
//            Node expr = n.getFirstChild();
//            
//            if (NT == Token.EXPR_RESULT) {
//                child.copyBase(n, expr);
//            }
//            
//            return walkNodes(expr, child);
//        }
//            
//        case Token.CALL: {
//            if (!isPrimitiveCall(context, n)) {
//                CAstNode base = Ast.makeNode(CAstNode.VAR, Ast.makeConstant("base"));
//                Node callee = n.getFirstChild();
//                WalkContext child = new BaseCollectingContext(context, callee, base);
//                CAstNode fun = walkNodes(callee, child);
//                
//                if (child.foundBase(callee))
//                    return Ast.makeNode(CAstNode.LOCAL_SCOPE, Ast.makeNode(CAstNode.BLOCK_EXPR, Ast.makeNode(
//                            CAstNode.DECL_STMT, Ast.makeConstant(new CAstSymbolImpl("base")), Ast
//                                    .makeConstant(null)), makeCall(fun, base, callee.getNext(), context)));
//                else
//                    return makeCall(fun, Ast.makeConstant(null), callee.getNext(), context);
//            } else {
//                return Ast.makeNode(CAstNode.PRIMITIVE, gatherChildren(n, context, 1));
//            }
//        }
//            
//        case Token.NAME: {
//            return Ast.makeNode(CAstNode.VAR, Ast.makeConstant(n.getString()));
//        }
//            
//        case Token.THIS: {
//            return Ast.makeNode(CAstNode.VAR, Ast.makeConstant("this"));
//        }
//            
//        case Token.THISFN: {
//            return Ast.makeNode(CAstNode.VAR, Ast.makeConstant(((FunctionNode) context.top())
//                    .getFunctionName()));
//        }
//            
//        case Token.STRING: {
//            return Ast.makeConstant(n.getString());
//        }
//            
//        case Token.NUMBER: {
//            return Ast.makeConstant(n.getDouble());
//        }
//            
//        case Token.FALSE: {
//            return Ast.makeConstant(false);
//        }
//            
//        case Token.TRUE: {
//            return Ast.makeConstant(true);
//        }
//            
//        case Token.NULL: {
//            return Ast.makeConstant(null);
//        }
//            
//        case Token.ADD:
//        case Token.DIV:
//        case Token.LSH:
//        case Token.MOD:
//        case Token.MUL:
//        case Token.RSH:
//        case Token.SUB:
//        case Token.URSH:
//        case Token.BITAND:
//        case Token.EQ:
//        case Token.SHEQ:
//        case Token.GE:
//        case Token.GT:
//        case Token.LE:
//        case Token.LT:
//        case Token.SHNE:
//        case Token.NE: {
//            Node l = n.getFirstChild();
//            Node r = l.getNext();
//            return Ast.makeNode(CAstNode.BINARY_EXPR, translateOpcode(NT), walkNodes(l, context), walkNodes(
//                    r, context));
//        }
//            
//        case Token.NEG: {
//            return Ast.makeNode(CAstNode.BINARY_EXPR, translateOpcode(Token.SUB), Ast.makeConstant(0),
//                    walkNodes(n.getFirstChild(), context));
//        }
//            
//        case Token.BITNOT:
//        case Token.NOT: {
//            return Ast.makeNode(CAstNode.UNARY_EXPR, translateOpcode(NT), walkNodes(n.getFirstChild(),
//                    context));
//        }
//            
//        case Token.VAR: {
//            Node nm = n.getFirstChild();
//            
//            context.addInitializer(Ast.makeNode(CAstNode.DECL_STMT, Ast.makeConstant(new CAstSymbolImpl(nm
//                    .getString())), Ast.makeNode(CAstNode.VAR, Ast.makeConstant("undefined"))));
//            
//            if (nm.getFirstChild() != null) {
//                WalkContext child = new ExpressionContext(context);
//                
//                return Ast.makeNode(CAstNode.ASSIGN, Ast.makeNode(CAstNode.VAR, Ast.makeConstant(nm
//                        .getString())), walkNodes(nm.getFirstChild(), child));
//                
//            } else {
//                if (n.getNext().getType() == Token.ENUM_INIT_KEYS) {
//                    context.getForInVar(nm.getString(), n.getNext().getFirstChild());
//                }
//                
//                return Ast.makeNode(CAstNode.EMPTY);
//            }
//        }
//            
//        case Token.REGEXP: {
//            int regexIdx = n.getIntProp(Node.REGEXP_PROP, -1);
//            
//            Assertions._assert(regexIdx != -1, "while converting: "
//                    + context.top().toStringTree(context.top()) + "\nlooking at bad regex:\n "
//                    + n.toStringTree(context.top()));
//            
//            String flags = context.top().getRegexpFlags(regexIdx);
//            Node flagsNode = Node.newString(flags);
//            
//            String str = context.top().getRegexpString(regexIdx);
//            Node strNode = Node.newString(str);
//            
//            strNode.addChildToFront(flagsNode);
//            
//            return handleNew(context, "RegExp", strNode);
//        }
//            
//        case Token.ENUM_INIT_KEYS: {
//            return Ast.makeNode(CAstNode.EMPTY);
//        }
//            
//        case Token.ENUM_ID: {
//            return Ast.makeNode(CAstNode.EACH_ELEMENT_GET, Ast.makeNode(CAstNode.VAR, Ast
//                    .makeConstant(context.getForInInitVar())));
//        }
//            
//        case Token.ENUM_NEXT: {
//            return Ast.makeNode(CAstNode.EACH_ELEMENT_HAS_NEXT, Ast.makeNode(CAstNode.VAR, Ast
//                    .makeConstant(context.getForInInitVar())));
//        }
//            
//        case Token.RETURN: {
//            Node val = n.getFirstChild();
//            if (val != null) {
//                WalkContext child = new ExpressionContext(context);
//                return Ast.makeNode(CAstNode.RETURN, walkNodes(val, child));
//            } else {
//                return Ast.makeNode(CAstNode.RETURN);
//            }
//        }
//            
//        case Token.SETNAME: {
//            Node nm = n.getFirstChild();
//            return Ast.makeNode(CAstNode.ASSIGN, walkNodes(nm, context), walkNodes(nm.getNext(), context));
//        }
//            
//        case Token.BINDNAME: {
//            return Ast.makeNode(CAstNode.VAR, Ast.makeConstant(n.getString()));
//        }
//            
//        case Token.IFNE:
//        case Token.IFEQ: {
//            context.cfg().add(n, ((Node.Jump) n).target, Boolean.TRUE);
//            WalkContext child = new ExpressionContext(context);
//            CAstNode gotoAst = Ast.makeNode(CAstNode.IFGOTO, translateOpcode(NT), walkNodes(
//                    n.getFirstChild(), child), Ast.makeConstant(1));
//            
//            context.cfg().map(n, gotoAst);
//            return gotoAst;
//        }
//            
//        case Token.GOTO: {
//            context.cfg().add(n, ((Node.Jump) n).target, null);
//            CAstNode gotoAst = Ast
//                    .makeNode(CAstNode.GOTO, Ast.makeConstant(((Node.Jump) n).target.labelId()));
//            
//            context.cfg().map(n, gotoAst);
//            return gotoAst;
//        }
//            
//        case Token.BREAK: {
//            context.cfg().add(n, ((Node.Jump) n).getJumpStatement().target, null);
//            CAstNode gotoAst = Ast.makeNode(CAstNode.GOTO, Ast.makeConstant(((Node.Jump) n)
//                    .getJumpStatement().target.labelId()));
//            
//            context.cfg().map(n, gotoAst);
//            return gotoAst;
//        }
//            
//        case Token.CONTINUE: {
//            context.cfg().add(n, ((Node.Jump) n).getJumpStatement().getContinue(), null);
//            CAstNode gotoAst = Ast.makeNode(CAstNode.GOTO, Ast.makeConstant(((Node.Jump) n)
//                    .getJumpStatement().getContinue().labelId()));
//            
//            context.cfg().map(n, gotoAst);
//            return gotoAst;
//        }
//            
//        case Token.TARGET: {
//            CAstNode result = Ast.makeNode(CAstNode.LABEL_STMT, Ast.makeConstant(n.labelId()), Ast
//                    .makeNode(CAstNode.EMPTY));
//            
//            context.cfg().map(n, result);
//            return result;
//        }
//            
//        case Token.OR: {
//            Node l = n.getFirstChild();
//            Node r = l.getNext();
//            return Ast.makeNode(CAstNode.IF_EXPR, walkNodes(l, context), Ast.makeConstant(true), walkNodes(r,
//                    context));
//        }
//            
//        case Token.AND: {
//            Node l = n.getFirstChild();
//            Node r = l.getNext();
//            return Ast.makeNode(CAstNode.IF_EXPR, walkNodes(l, context), walkNodes(r, context), Ast
//                    .makeConstant(false));
//        }
//            
//        case Token.HOOK: {
//            Node cond = n.getFirstChild();
//            Node thenBranch = cond.getNext();
//            Node elseBranch = thenBranch.getNext();
//            return Ast.makeNode(CAstNode.IF_EXPR, walkNodes(cond, context), walkNodes(thenBranch, context),
//                    walkNodes(elseBranch, context));
//            
//        }
//            
//        case Token.INC:
//        case Token.DEC: {
//            int flags = n.getIntProp(Node.INCRDECR_PROP, -1);
//            CAstNode op = ((flags & Node.DECR_FLAG) != 0) ? CAstOperator.OP_SUB : CAstOperator.OP_ADD;
//            
//            Node l = n.getFirstChild();
//            CAstNode last = walkNodes(l, context);
//            
//            return Ast.makeNode((((flags & Node.POST_FLAG) != 0) ? CAstNode.ASSIGN_POST_OP
//                    : CAstNode.ASSIGN_PRE_OP), last, Ast.makeConstant(1), op);
//        }
//            
//        case Token.NEW: {
//            if (isPrimitiveCreation(context, n)) {
//                return makeBuiltinNew(context, n.getFirstChild().getString());
//            } else {
//                Node receiver = n.getFirstChild();
//                return handleNew(context, walkNodes(receiver, context), receiver.getNext());
//            }
//        }
//            
//        case Token.ARRAYLIT: {
//            int count = 0;
//            for (Node x = n.getFirstChild(); x != null; count++, x = x.getNext())
//                ;
//            
//            int i = 0;
//            CAstNode[] args = new CAstNode[2 * count + 1];
//            args[i++] = (isPrologueScript(context)) ? makeBuiltinNew(context, "Array") : handleNew(context,
//                    "Array", null);
//            
//            int[] skips = (int[]) n.getProp(Node.SKIP_INDEXES_PROP);
//            int skip = 0;
//            int idx = 0;
//            Node elt = n.getFirstChild();
//            while (elt != null) {
//                if (skips != null && skip < skips.length && skips[skip] == idx) {
//                    skip++;
//                    idx++;
//                    continue;
//                }
//                
//                args[i++] = Ast.makeConstant(idx++);
//                args[i++] = walkNodes(elt, context);
//                
//                elt = elt.getNext();
//            }
//            
//            return Ast.makeNode(CAstNode.OBJECT_LITERAL, args);
//        }
//            
//        case Token.OBJECTLIT: {
//            Object[] propertyList = (Object[]) n.getProp(Node.OBJECT_IDS_PROP);
//            CAstNode[] args = new CAstNode[propertyList.length * 2 + 1];
//            int i = 0;
//            args[i++] = ((isPrologueScript(context)) ? makeBuiltinNew(context, "Object") : handleNew(context,
//                    "Object", null));
//            
//            Node val = n.getFirstChild();
//            int nameIdx = 0;
//            for (; nameIdx < propertyList.length; nameIdx++, val = val.getNext()) {
//                args[i++] = Ast.makeConstant(propertyList[nameIdx]);
//                args[i++] = walkNodes(val, context);
//            }
//            
//            return Ast.makeNode(CAstNode.OBJECT_LITERAL, args);
//        }
//            
//        case Token.GETPROP:
//        case Token.GETELEM: {
//            Node receiver = n.getFirstChild();
//            Node element = receiver.getNext();
//            
//            CAstNode rcvr = walkNodes(receiver, context);
//            CAstNode baseVar = context.setBase(n);
//            
//            CAstNode elt = walkNodes(element, context);
//            
//            if (baseVar != null) {
//                return Ast.makeNode(CAstNode.BLOCK_EXPR, Ast.makeNode(CAstNode.ASSIGN, baseVar, rcvr), Ast
//                        .makeNode(CAstNode.OBJECT_REF, baseVar, elt));
//            } else {
//                return Ast.makeNode(CAstNode.OBJECT_REF, rcvr, elt);
//            }
//        }
//            
//        case Token.SETPROP:
//        case Token.SETELEM: {
//            Node receiver = n.getFirstChild();
//            Node elt = receiver.getNext();
//            Node val = elt.getNext();
//            
//            CAstNode rcvr = walkNodes(receiver, context);
//            
//            return Ast.makeNode(CAstNode.ASSIGN, Ast.makeNode(CAstNode.OBJECT_REF, rcvr, walkNodes(elt,
//                    context)), walkNodes(val, context));
//        }
//            
//        case Token.DELPROP: {
//            Node receiver = n.getFirstChild();
//            Node element = receiver.getNext();
//            
//            CAstNode rcvr = walkNodes(receiver, context);
//            CAstNode baseVar = context.setBase(n);
//            
//            CAstNode elt = walkNodes(element, context);
//            
//            if (baseVar != null) {
//                return Ast.makeNode(CAstNode.BLOCK_EXPR, Ast.makeNode(CAstNode.ASSIGN, baseVar, rcvr), Ast
//                        .makeNode(CAstNode.ASSIGN, Ast.makeNode(CAstNode.OBJECT_REF, baseVar, elt), Ast
//                                .makeConstant(null)));
//            } else {
//                return Ast.makeNode(CAstNode.ASSIGN, Ast.makeNode(CAstNode.OBJECT_REF, rcvr, elt), Ast
//                        .makeConstant(null));
//            }
//        }
//            
//        case Token.TYPEOFNAME: {
//            return Ast
//                    .makeNode(CAstNode.TYPE_OF, Ast.makeNode(CAstNode.VAR, Ast.makeConstant(n.getString())));
//        }
//            
//        case Token.TYPEOF: {
//            return Ast.makeNode(CAstNode.TYPE_OF, walkNodes(n.getFirstChild(), context));
//        }
//            
//        case Token.SETPROP_OP:
//        case Token.SETELEM_OP: {
//            Node receiver = n.getFirstChild();
//            Node elt = receiver.getNext();
//            Node op = elt.getNext();
//            
//            CAstNode rcvr = walkNodes(receiver, context);
//            
//            return null;
////            return Ast.makeNode(CAstNode.ASSIGN_POST_OP, Ast.makeNode(CAstNode.OBJECT_REF, rcvr, walkNodes(
////                    elt, context)), walkNodes(op.getFirstChild().getNext(), context), translateOpcode(op
////                    .getType()));
//        }
//            
//        case Token.THROW: {
//            CAstNode catchNode = context.getCatchTarget();
//            if (catchNode != null)
//                context.cfg().add(n, context.getCatchTarget(), null);
//            else
//                context.cfg().add(n, CAstControlFlowMap.EXCEPTION_TO_EXIT, null);
//            
//            CAstNode throwAst = Ast.makeNode(CAstNode.THROW, walkNodes(n.getFirstChild(), context));
//            
//            context.cfg().map(n, throwAst);
//            return throwAst;
//        }
//            
//        case Token.EMPTY: {
//            return Ast.makeConstant(null);
//        }
//            
//        default: {
//            System.err.println("while converting: " + context.top().toStringTree(context.top())
//                    + "\nlooking at unhandled:\n " + n.toStringTree(context.top()) + "\n(of type " + NT
//                    + ") (of class " + n.getClass() + ")");
//            
//            Assertions.UNREACHABLE();
//            return null;
//        }
//        }
//    }
//    
//}
