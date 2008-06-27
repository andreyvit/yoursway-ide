package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.impl.commands.AbstractCommand;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.ide.application.view.impl.commands.HandlerOfOpenProject;

public class ApplicationCommands {
    
    public final Command openDocument = new AbstractCommand("openDocument") {

    };
    
    public final Command newProject = new AbstractCommand("newProject") {

    };
    
    public final Command openProject = new AbstractCommand(HandlerOfOpenProject.class) {

    };
    
    public final Command closeProject = new AbstractCommand("closeProject") {

    };
    
    public final Command newFile = new AbstractCommand("newFile") {

    };
    
    public final Command closeFile = new AbstractCommand("closeFile") {
        
    };
    
    public final Command saveFile = new AbstractCommand("saveFile") {

    };
    
}
