package com.yoursway.ide.platforms.mac;

import static com.yoursway.utils.YsStrings.emptyToNullWithTrim;

import java.io.File;
import java.io.IOException;

import com.yoursway.ide.platforms.api.PlatformSupport;

public abstract class MacPlatform implements PlatformSupport {
    
    protected File homeFolder() throws FolderNotAvailable {
        String home = emptyToNullWithTrim(System.getProperty("user.home"));
        if (home == null)
            throw new FolderNotAvailable("Home folder not set.");
        File result = new File(home);
        if (!result.isDirectory())
            throw new FolderNotAvailable("Home folder is not an existing directory!");
        return result;
    }
    
    public File defaultProjectsLocation() {
        try {
            return new File(homeFolder(), "Documents");
        } catch (FolderNotAvailable e) {
            // TODO: log a warning about the problem with the home folder
            e.printStackTrace();
            try {
                return new File(".").getCanonicalFile();
            } catch (IOException e1) {
                return new File(".").getAbsoluteFile();
            }
        }
    }

}
