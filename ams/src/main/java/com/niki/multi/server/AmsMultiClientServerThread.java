package com.niki.multi.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  - Messaging server code, thread type implementation
 *
 */
public class AmsMultiClientServerThread extends Thread {
    private Socket SERVER_SOCKET = null;

    public AmsMultiClientServerThread(Socket socket) {
        super("AmsMultiClientServer");
        this.SERVER_SOCKET = socket;
    }

    public void run() {
        try {
            System.out.println("\n# Waiting for client on port " + SERVER_SOCKET.getLocalPort() + "...");
            System.out.println("# Successfully connected to " + SERVER_SOCKET.getRemoteSocketAddress());

            /** DatOutputStrea - To send server response */
            DataOutputStream out = null;
            out = new DataOutputStream(SERVER_SOCKET.getOutputStream());
            out.writeUTF("# Thanks for connecting to " + SERVER_SOCKET.getLocalSocketAddress());
            out.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(SERVER_SOCKET.getInputStream()));
            String str;
            while ((str = br.readLine()) != null) {
                System.out.println("Incoming Message: " + str);
                out = new DataOutputStream(SERVER_SOCKET.getOutputStream());
                out.writeUTF("# Thanks for connecting to " + SERVER_SOCKET.getLocalSocketAddress());
                out.flush();


                if (str.equals("bye")) {
                    out.writeUTF(str);
                    break;
                } else {
                    str = "server Response: " + str;
                    out.writeUTF(str);
                }
            }

            /** close the persistent connection if "bye" string is found */
            br.close();
            out.close();
            SERVER_SOCKET.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
