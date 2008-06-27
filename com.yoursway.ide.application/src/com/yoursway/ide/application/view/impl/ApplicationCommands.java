package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.impl.commands.AbstractCommand;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.ide.application.view.impl.commands.HandlerOfOpenProject;

public class ApplicationCommands {
    
    public final Command openDocument = new AbstractCommand("openDocument") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command newProject = new AbstractCommand("newProject") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command openProject = new AbstractCommand(HandlerOfOpenProject.class) {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command closeProject = new AbstractCommand("closeProject") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command newFile = new AbstractCommand("newFile") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command closeFile = new AbstractCommand("closeFile") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
    public final Command saveFile = new AbstractCommand("saveFile") {

        public boolean invokeSpecificHandler(Handler handler) {
            return false;
        }
        
    };
    
}
