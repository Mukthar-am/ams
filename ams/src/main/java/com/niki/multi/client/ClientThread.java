package com.niki.multi.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  - Messaging chat client, thread type implementation
 *
 */

public class ClientThread extends Thread {
    private static final int AMS_SERVER_PORT = 6066;
    private static final String AMS_SERVER_NAME = "localhost";  //InetAddress.getLocalHost()
    private Socket CLIENT_SOCKET;
    private String TAG;

    private InputStream inFromServer;
    private DataInputStream dataInputStream;


    public ClientThread(String name) throws IOException {
        super(name);
        TAG = "[" + name + "]: ";
        CLIENT_SOCKET = new Socket(AMS_SERVER_NAME, AMS_SERVER_PORT);

        /** DataInputStrea to read server responses */
        inFromServer = CLIENT_SOCKET.getInputStream();
        dataInputStream = new DataInputStream(inFromServer);
        System.out.println(TAG + "[ Initialized(): server Response - " + dataInputStream.readUTF() + " ]");
    }


    public void run() {
        /** PrintWriter to write msgs to socket */
        PrintWriter socketPrintWriter = null;
        try {
            socketPrintWriter = new PrintWriter(CLIENT_SOCKET.getOutputStream(), true);

            /** close connection based on user input */
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n" + TAG + "Type Message here: ");
                scanner.useDelimiter("\\n");

                String input = scanner.nextLine();
                if (input.isEmpty()) {
                    System.out.println("# Warning: Invalid input!");

                } else {
                    System.out.println(TAG + "# Msg = " + input);
                    socketPrintWriter.println(input);  /** Sending msg to server socket */

                    if (input.equalsIgnoreCase("bye")) {
                        break;

                    } else {
                        inFromServer = CLIENT_SOCKET.getInputStream();
                        dataInputStream = new DataInputStream(inFromServer);
                        System.out.println(TAG + "[" + dataInputStream.readUTF() + "]");
                    }
                }

            }

            /** Close all the connectin objects */
            System.out.println("# END: Closing connection.");
            socketPrintWriter.close();

            CLIENT_SOCKET.close();

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
