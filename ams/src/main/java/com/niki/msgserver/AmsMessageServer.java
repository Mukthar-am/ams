package com.niki.msgserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by mukthar.ahmed on 4/1/16.
 * <p>
 * - Message exchange socket FROM_CLIENT_SOCKET
 */

public class AmsMessageServer extends Thread implements MessagingServer {
    private ServerSocket SERVER_SOCKET;
    private int TIMEOUT = 20000;
    private Socket FROM_CLIENT_SOCKET;
    private boolean SERVER_RUN_FLAG = true;


    /**
     * FROM_CLIENT_SOCKET constructor, establising socket connection
     */
    public AmsMessageServer() throws IOException {
        SERVER_SOCKET = new ServerSocket(6066);

        /** Waits for the client to connect (in milli seconds) and time-out */
        //SERVER_SOCKET.setSoTimeout(TIMEOUT);
    }


    public void run() {
        while (SERVER_RUN_FLAG) {
            try {
                System.out.println("\n# Waiting for client on port " + SERVER_SOCKET.getLocalPort() + "...");
                FROM_CLIENT_SOCKET = SERVER_SOCKET.accept();

                System.out.println("# Successfully connected to " + FROM_CLIENT_SOCKET.getRemoteSocketAddress());

                /** DatOutputStrea - To send server response */
                DataOutputStream out = new DataOutputStream(FROM_CLIENT_SOCKET.getOutputStream());
                out.writeUTF("# Thanks for connecting to " + FROM_CLIENT_SOCKET.getLocalSocketAddress());
                out.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(FROM_CLIENT_SOCKET.getInputStream()));
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println("Incoming Message: " + str);

                    if (str.equals("bye")) {
                        out.writeUTF(str);
                        break;
                    } else {
                        str = "Server Response: " + str;
                        out.writeUTF(str);
                    }
                }

                /** close the persistent connection if "bye" string is found */
                br.close();
                out.close();
                FROM_CLIENT_SOCKET.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out, after " + TIMEOUT + " seconds!");
                break;

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    /**
     * Stop server: Set server-run-flag to false, so as to terminate server
     */
    public void stopServer() {
        SERVER_RUN_FLAG = false;
    }


    /**
     * Start Server: Just init the thread and start
     */
    public void startServer() {
        try {
            Thread srvThread = new AmsMessageServer();
            srvThread.start();  /** starts the server thread */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) {
        this.startServer();
    }
}