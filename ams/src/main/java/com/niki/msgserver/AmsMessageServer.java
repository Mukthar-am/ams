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

public class AmsMessageServer extends Thread {
    private ServerSocket SERVER_SOCKET;
    private int TIMEOUT = 20000;
    private Socket FROM_CLIENT_SOCKET;

    /**
     * FROM_CLIENT_SOCKET constructor, establising socket connection
     */
    public AmsMessageServer() throws IOException {
        SERVER_SOCKET = new ServerSocket(6066);

        /** Waits for the client to connect (in milli seconds) and time-out */
        //SERVER_SOCKET.setSoTimeout(TIMEOUT);
    }


    public void run() {
        while (true) {
            try {
                System.out.println("\n# Waiting for client on port " + SERVER_SOCKET.getLocalPort() + "...");
                FROM_CLIENT_SOCKET = SERVER_SOCKET.accept();

                System.out.println("# Successfully connected to " + FROM_CLIENT_SOCKET.getRemoteSocketAddress());

                DataOutputStream out = new DataOutputStream(FROM_CLIENT_SOCKET.getOutputStream());
                out.writeUTF("# Thanks for connecting to " + FROM_CLIENT_SOCKET.getLocalSocketAddress());
                out.flush();


                /** PrintWriter pw - Object to print at stdout */
                PrintWriter pw = new PrintWriter(FROM_CLIENT_SOCKET.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(FROM_CLIENT_SOCKET.getInputStream()));
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println("Incoming Message: " + str);

                    if (str.equals("bye")) {
                        pw.println("bye");
                        break;
                    } else {
                        str = "Server Response: " + str;
                        out.writeUTF(str);
                    }
                }

                /** close the persistent connection if "bye" string is found */
                pw.close();
                br.close();
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

    public void closeSocketConnection() {
        try {
            FROM_CLIENT_SOCKET.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            Thread t = new AmsMessageServer();
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}