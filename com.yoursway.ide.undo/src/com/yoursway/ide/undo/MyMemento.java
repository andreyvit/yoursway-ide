package com.yoursway.ide.undo;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.XMLMemento;

public class MyMemento {

    public static IMemento forPersistableElement(IPersistableElement element) {
        XMLMemento memento = XMLMemento.createWriteRoot("a"); //?
        element.saveState(memento);
        return memento;
    }
    
    public static IMemento childForPersistableElement(String type, IPersistableElement element) {
        XMLMemento memento = XMLMemento.createWriteRoot("a"); //?
        IMemento child = memento.createChild(type);
        element.saveState(child);
        return memento;
    }
    
}
