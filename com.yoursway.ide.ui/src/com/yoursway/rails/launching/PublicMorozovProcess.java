package com.yoursway.rails.launching;

import java.lang.reflect.Field;
import java.util.Map;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.RuntimeProcess;

import com.yoursway.ide.ui.Activator;

public class PublicMorozovProcess extends RuntimeProcess {
    
    public PublicMorozovProcess(ILaunch launch, Process process, String name, Map attributes) {
        super(launch, process, name, attributes);
    }
    
    @Override
    public Process getSystemProcess() {
        return super.getSystemProcess();
    }
    
    /**
     * Returns the process ID of the launched process on Unix-like platforms.
     * 
     * @return the PID of the lauched process, or <code>0</code> if not
     *         applicable.
     */
    public int getPid() {
        Process process = getSystemProcess();
        final Class<? extends Process> processClass = process.getClass();
        final String processClassName = processClass.getName();
        if (processClassName.equals("java.lang.UNIXProcess")) {
            try {
                final Field pidField = processClass.getDeclaredField("pid");
                pidField.setAccessible(true);
                Integer intPidValue = (Integer) pidField.get(process);
                return intPidValue.intValue();
            } catch (SecurityException e) {
                Activator.unexpectedError(e);
            } catch (NoSuchFieldException e) {
                Activator.unexpectedError(e);
            } catch (IllegalArgumentException e) {
                Activator.unexpectedError(e);
            } catch (IllegalAccessException e) {
                Activator.unexpectedError(e);
            }
        }
        return 0;
    }
    
}
