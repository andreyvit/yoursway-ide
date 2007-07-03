package com.yoursway.rails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.utils.InterpreterRunnerUtil;

public class RailsInstancesManager {
    
    private static Map<String, Map<String, RailsInstance>> railsInstance = new HashMap<String, Map<String, RailsInstance>>();
    
    public synchronized static List<RailsInstance> getRailsInstances() {
        ArrayList<RailsInstance> railsInstanceList = new ArrayList<RailsInstance>();
        for (Map<String, RailsInstance> rs : railsInstance.values())
            railsInstanceList.addAll(rs.values());
        return railsInstanceList;
    }
    
    public synchronized static void addRailsInstance(IInterpreterInstall ruby, String railsVersion,
            RailsInstance railsInstanceInstance) {
        if (!railsInstance.containsKey(ruby.getId()))
            railsInstance.put(ruby.getId(), new HashMap<String, RailsInstance>());
        
        System.out.println("Adding/updating rails instance: " + ruby.getName() + " rails " + railsVersion);
        
        railsInstance.get(ruby.getId()).put(railsVersion, railsInstanceInstance);
    }
    
    public synchronized static void removeRails(IInterpreterInstall ruby) {
        System.out.println("Removing all rails instances for " + ruby.getName());
        railsInstance.remove(ruby.getId());
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
