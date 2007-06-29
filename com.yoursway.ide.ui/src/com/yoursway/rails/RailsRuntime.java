package com.yoursway.rails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.utils.InterpreterRunnerUtil;

public class RailsRuntime {
    
    private static Map<String, Map<String, Rails>> rails = new HashMap<String, Map<String, Rails>>();
    
    public synchronized static List<Rails> getRails() {
        ArrayList<Rails> railsList = new ArrayList<Rails>();
        for (Map<String, Rails> rs : rails.values())
            railsList.addAll(rs.values());
        return railsList;
    }
    
    public synchronized static void addRails(IInterpreterInstall ruby, String railsVersion,
            Rails railsInstance) {
        if (!rails.containsKey(ruby.getId()))
            rails.put(ruby.getId(), new HashMap<String, Rails>());
        
        System.out.println("Adding/updating rails instance: " + ruby.getName() + " rails " + railsVersion);
        
        rails.get(ruby.getId()).put(railsVersion, railsInstance);
    }
    
    public synchronized static void removeRails(IInterpreterInstall ruby) {
        System.out.println("Removing all rails instances for " + ruby.getName());
        rails.remove(ruby.getId());
    }
    
    // FIXME: ugly
    public static String checkFinishedProcess(IProcess launchProcess, String processLabel) {
        int exitValue = InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess);
        switch (exitValue) {
        case 0:
            return null;
        case 1:
            return "Unable to retrieve gem info (" + processLabel + "): no gem passed to the script.";
        case 2:
            return "Unable to retrieve gem info (" + processLabel + "): gem is not found";
        case 3:
            return "Unable to retrieve gem info (" + processLabel + "): rubygems not found";
        default:
            return "Runtime error during fetching gem information (" + processLabel + "):\n"
                    + launchProcess.getStreamsProxy().getErrorStreamMonitor().getContents();
        }
    }
}
