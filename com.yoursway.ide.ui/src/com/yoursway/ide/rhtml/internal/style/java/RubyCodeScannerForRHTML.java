package com.yoursway.ide.rhtml.internal.style.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dltk.ruby.internal.ui.text.RubyCodeScanner;
import org.eclipse.dltk.ruby.internal.ui.text.RubyColorConstants;
import org.eclipse.dltk.ruby.internal.ui.text.RubyPercentStringRule;
import org.eclipse.dltk.ruby.internal.ui.text.RubySlashRegexpRule;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

public class RubyCodeScannerForRHTML extends RubyCodeScanner {
    
    public RubyCodeScannerForRHTML(IColorManager manager, IPreferenceStore store) {
        super(manager, store);
    }
    
    private static class CannotGetPartitionerRules extends Exception {
        
        private static final long serialVersionUID = -7534912517337210769L;
        
        public CannotGetPartitionerRules(Throwable cause) {
            super(cause);
        }
        
    }
    
    private static final String[] STRING_PROPERTIES = new String[] { RubyColorConstants.RUBY_STRING };
    
    @Override
    protected String[] getTokenProperties() {
        List<String> props = new ArrayList<String>();
        props.addAll(Arrays.asList(super.getTokenProperties()));
        props.addAll(Arrays.asList(STRING_PROPERTIES));
        return props.toArray(new String[props.size()]);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected List createRules() {
        final List rules = super.createRules();
        
        Token string = getToken(RubyColorConstants.RUBY_STRING);
        
        rules.add(new MultiLineRule("\'", "\'", string, '\\'));
        
        rules.add(new MultiLineRule("\"", "\"", string, '\\'));
        
        rules.add(new RubyPercentStringRule(string, false));
        
        rules.add(new RubySlashRegexpRule(string));
        
        return rules;
    }
    
}
