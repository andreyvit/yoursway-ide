package com.yoursway.ruby;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

public class RubyInstanceCollection {
    
    private RubyInstanceCollection() {
    }
    
    private static final RubyInstanceCollection INSTANCE = new RubyInstanceCollection();
    
    public static RubyInstanceCollection instance() {
        return INSTANCE;
    }
    
    public Collection<RubyInstance> getAll() {
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        
        Collection<RubyInstance> result = new ArrayList<RubyInstance>();
        for (IInterpreterInstall rubyInterpreter : rubyInterpreters[0].getInterpreterInstalls())
            result.add(RubyInstance.adapt(rubyInterpreter));
        return result;
    }
    
}
