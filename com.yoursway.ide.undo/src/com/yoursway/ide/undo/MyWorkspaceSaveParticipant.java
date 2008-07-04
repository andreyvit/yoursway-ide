package com.yoursway.ide.undo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.XMLMemento;
import org.w3c.dom.Document;

public class MyWorkspaceSaveParticipant implements ISaveParticipant {
    
    public void doneSaving(ISaveContext context) {       
        
        Activator plugin = Activator.getDefault();

        // delete the old saved state since it is not necessary anymore
        int previousSaveNumber = context.getPreviousSaveNumber();
        String oldFileName = "operationHistory-" + Integer.toString(previousSaveNumber);
        File f = plugin.getStateLocation().append(oldFileName).toFile();
        f.delete();
    }
    
    public void prepareToSave(ISaveContext context) throws CoreException {
        // TODO Auto-generated method stub
        
    }
    
    public void rollback(ISaveContext context) {
        // TODO Auto-generated method stub
        
    }
    
    public void saving(ISaveContext context) throws CoreException {
        //> check context kind
        
        
        Activator plugin = Activator.getDefault();
        
        /*OperationHistory history = OperationHistory.get();
        history.dispose();*/        
        
        
        
        int saveNumber = context.getSaveNumber();
        String saveFileName = "operationHistory-" + Integer.toString(saveNumber);
        File f = plugin.getStateLocation().append(saveFileName).toFile();
        //! if we fail to write, an exception is thrown and we do not update the path
        
        
        //: myPluginInstance.writeImportantState(f);

        
        
        XMLMemento memento = XMLMemento.createWriteRoot("operationHistory");
        OperationHistory.get().saveState(memento);
        try {
            memento.save(new FileWriter(f));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        context.map(new Path("operationHistory"), new Path(saveFileName));
        context.needSaveNumber();
        
        
        
    }
    
}
