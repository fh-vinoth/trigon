package com.trigon.mobile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class AvailablePorts {

    private static final Logger logger = LogManager.getLogger(AvailablePorts.class);

    /*
     * Generates Random ports
     * Used during starting appium server
     */
    public int getPort() {
        ServerSocket socket = null;
        int port = 0;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error(e);
                }
        }
        return port;
    }
}