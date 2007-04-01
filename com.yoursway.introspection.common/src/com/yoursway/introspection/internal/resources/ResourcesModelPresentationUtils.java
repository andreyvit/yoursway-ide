package com.yoursway.introspection.internal.resources;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;

public class ResourcesModelPresentationUtils {

    public static String deltaKindToString(IResourceDelta delta) {
        switch (delta.getKind()) {
        case IResourceDelta.ADDED:
            return "ADDED";
        case IResourceDelta.REMOVED:
            return "REMOVED";
        case IResourceDelta.ADDED_PHANTOM:
            return "ADDED_PHANTOM";
        case IResourceDelta.REMOVED_PHANTOM:
            return "REMOVED_PHANTOM";
        case IResourceDelta.CHANGED:
            return "CHANGED";
        case IResourceDelta.NO_CHANGE:
            return "NO_CHANGE";
        default:
            return "UNKNOWN";
        }
    }

    public static void appendDeltaFlags(StringBuilder result, IResourceDelta delta) {
        int flags = delta.getFlags();
        if ((flags & IResourceDelta.CONTENT) != 0)
            result.append(' ').append("CONTENT");
        if ((flags & IResourceDelta.COPIED_FROM) != 0)
            result.append(' ').append("COPIED_FROM");
        if ((flags & IResourceDelta.DESCRIPTION) != 0)
            result.append(' ').append("DESCRIPTION");
        if ((flags & IResourceDelta.ENCODING) != 0)
            result.append(' ').append("ENCODING");
        if ((flags & IResourceDelta.MARKERS) != 0)
            result.append(' ').append("MARKERS");
        if ((flags & IResourceDelta.MOVED_FROM) != 0)
            result.append(' ').append("MOVED_FROM");
        if ((flags & IResourceDelta.MOVED_TO) != 0)
            result.append(' ').append("MOVED_TO");
        if ((flags & IResourceDelta.OPEN) != 0)
            result.append(' ').append("OPEN");
        if ((flags & IResourceDelta.REPLACED) != 0)
            result.append(' ').append("REPLACED");
        if ((flags & IResourceDelta.SYNC) != 0)
            result.append(' ').append("SYNC");
        if ((flags & IResourceDelta.TYPE) != 0)
            result.append(' ').append("TYPE");
    }

    public static String changeEventTypeNameToString(IResourceChangeEvent event) {
        switch (event.getType()) {
        case IResourceChangeEvent.PRE_BUILD:
            return "PRE_BUILD";
        case IResourceChangeEvent.POST_BUILD:
            return "POST_BUILD";
        case IResourceChangeEvent.POST_CHANGE:
            return "POST_CHANGE";
        case IResourceChangeEvent.PRE_CLOSE:
            return "PRE_CLOSE";
        case IResourceChangeEvent.PRE_DELETE:
            return "PRE_DELETE";
        default:
            return "UNKNOWN";
        }
    }
    
}
