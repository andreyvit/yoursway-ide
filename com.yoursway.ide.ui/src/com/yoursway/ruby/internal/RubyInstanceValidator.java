package com.yoursway.ruby.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.Version;
import com.yoursway.ruby.RubyInstallWrapper;
import com.yoursway.ruby.RubyScriptInvokationError;
import com.yoursway.ruby.RubyToolUtils;
import com.yoursway.ruby.ProcessResult;

public class RubyInstanceValidator {
    
    private static final Pattern LEGAL_RUBY_VERSION = Pattern.compile("^\\d+(\\.\\d+)*$");
    
    public static RubyInstanceValidationResult validate(File executableLocation, IProgressMonitor monitor) {
        RubyInstallWrapper rubyInstance = RubyInstallWrapper.create(executableLocation, "Temp Ruby Install");
        try {
            String script = RubyToolUtils.getScriptCopySuitableForRunning("ruby_version.rb");
            try {
                ProcessResult result = rubyInstance.runRubyScript(script, new ArrayList<String>(),
                        monitor);
                if (result.getExitCode() == 0) {
                    String versionString = result.getOutputData().trim();
                    if (LEGAL_RUBY_VERSION.matcher(versionString).find()) {
                        Version version = Version.fromDotDelimitedString(versionString);
                        return new RubyInstanceValidationResult(true, version);
                    }
                }
            } catch (RubyScriptInvokationError e) {
                Activator.expectedError(e);
            }
        } finally {
            rubyInstance.destroy();
        }
        return new RubyInstanceValidationResult(false, null);
    }
}
