package com.yoursway.ide.common.mru;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

import com.yoursway.ide.common.mru.internal.MruListContents;
import com.yoursway.ide.common.mru.internal.MruListEntry;
import com.yoursway.ide.ui.Activator;

/**
 * Manages a persistent "most recently used" list.
 * 
 * For serialization purposes uses the class loader used to load
 * <code>this.getClass()</code>, so declared abstract to remind clients to
 * make a derived class.
 * 
 * @author Andrey Tarantsov
 * 
 * @param <E>
 *            a type of list entries — a class derived from
 *            <code>MruListEntry</code>
 */
public abstract class MostRecentlyUsedList<E extends MruListEntry> {
    
    private MruListContents<E> contents;
    
    private final File storageFile;
    
    public MostRecentlyUsedList(String id) {
        storageFile = new File(Activator.getDefault().getStateLocation().toFile(), "mru-" + id + ".xml");
    }
    
    public synchronized List<E> getEntries() {
        loadIfNecessary();
        return contents.getEntries();
    }
    
    public synchronized void entryUsed(E usedEntry) {
        loadIfNecessary();
        E entry = lookupEntry(usedEntry);
        if (entry == null)
            entry = usedEntry;
        else {
            contents.getEntries().remove(entry);
            entry.setLastUseToNow();
        }
        contents.getEntries().add(0, usedEntry);
        save();
    }
    
    private E lookupEntry(E usedEntry) {
        for (E entry : contents.getEntries())
            if (entry.equals(usedEntry))
                return entry;
        return usedEntry;
    }
    
    @SuppressWarnings("unchecked")
    private void loadIfNecessary() {
        if (contents != null)
            return;
        contents = new MruListContents<E>(); // if something goes wrong
        try {
            final BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(storageFile));
            //            XMLDecoder decoder = new XMLDecoder(fileStream, this, null,
            //                    getClass().getClassLoader());
            //            contents = (MruListContents<E>) decoder.readObject();
            //            decoder.close();
            ObjectInputStream stream = new ObjectInputStream(fileStream);
            contents = (MruListContents<E>) stream.readObject();
            stream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            Activator.unexpectedError(e);
        }
        List<E> list = contents.getEntries();
        Collections.sort(list);
        contents.setEntries(list);
    }
    
    private void save() {
        try {
            storageFile.getParentFile().mkdirs();
            final BufferedOutputStream fileStream = new BufferedOutputStream(
                    new FileOutputStream(storageFile));
            //            XMLEncoder encoder = new XMLEncoder(fileStream);
            //            encoder.writeObject(contents);
            //            encoder.close();
            ObjectOutputStream stream = new ObjectOutputStream(fileStream);
            stream.writeObject(contents);
            stream.close();
        } catch (FileNotFoundException e) {
            Activator.unexpectedError(e);
        } catch (IOException e) {
            Activator.unexpectedError(e);
        }
    }
    
}
