package com.yoursway.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.yoursway.ide.ui.Activator;

public class ServerUtils {
    
    private static final int PORT_CHECK_TIMEOUT = 100;
    
    /**
     * Checks if the given port is available on the local host.
     * 
     * @param port
     *            the port to check
     * @return true if the port is available, false otherwise
     */
    public static boolean isPortAvailable(int port) {
        Socket socket = new Socket();
        try {
            // Attempt to connect to the localhost on that port
            // If an error occurs, we weren't able to connect, which
            // means the port is available.
            socket.connect(new InetSocketAddress(InetAddress.getByName("localhost"), port),
                    PORT_CHECK_TIMEOUT);
            if (socket.isConnected())
                // Connection established, which means the port is in use
                return false;
            else
                return true;
        } catch (IOException e) {
            // Could not connect, the port is available
            return true;
        } finally {
            // Close the socket if necessary
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Log this, even though it shouldn't happen
                    Activator.unexpectedError(e, "Error when closing socket");
                }
            }
        }
    }
    
    public static final class NoFreePortFound extends Exception {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    public static int findFreePort(final int startingPort, int portsToSearch) throws NoFreePortFound {
        for (int port = startingPort; port < startingPort + portsToSearch; ++port)
            if (isPortAvailable(port))
                return port;
        throw new NoFreePortFound();
    }
    
}
