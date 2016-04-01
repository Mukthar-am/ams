package com.niki.multi.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  - Multi server start up.
 *  - For every socket connection, init a thread of server type
 */
public class MultiServer {
    private static int PORT = 6066; /** Hard coded port number */

    public static void main(String[] args) throws IOException {
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (listening) {
                new AmsMultiClientServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            System.exit(-1);
        }
    }
}
