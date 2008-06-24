package com.mkalugin.corchy.ui.core.preference;

import java.io.IOException;

/**
 * IPersistentPreferenceStore is a preference store that can 
 * be saved.
 */
public interface IPersistentPreferenceStore extends IPreferenceStore {

    /**
     * Saves the non-default-valued preferences known to this preference
     * store to the file from which they were originally loaded.
     *
     * @exception java.io.IOException if there is a problem saving this store
     */
    public void save() throws IOException;

}
