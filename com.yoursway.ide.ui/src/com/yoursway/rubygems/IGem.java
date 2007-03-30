package com.yoursway.rubygems;

/**
 * This interface represents an installed Ruby gem.
 */
public interface IGem {
    /**
     * Short name of gem, e.g. 'rails'
     * 
     * @return name of gem.
     */
    String getName();
    
    /**
     * String representation of gem version, e.g. '1.2.3'
     * 
     * @return gem version
     */
    String getVersion();
    
    /**
     * Get list of directories to be 'require'd by gem users.
     * 
     * @return list of directories relative to the gem directory.
     */
    String[] getRequirePaths();
    
    /**
     * Returns the installation directory for the gem.
     * 
     * @return absolute path to the installation gem directory.
     */
    String getDirectory();
}
