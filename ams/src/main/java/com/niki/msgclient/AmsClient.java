package com.niki.msgclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mukthar.ahmed on 4/1/16.
 *
 * - AmsClient (Ahmed's Chat client) implementation
 */
public class AmsClient {
    private static final int AMS_SERVER_PORT = 6066;
    private static final String AMS_SERVER_NAME = "localhost";  //InetAddress.getLocalHost()
    private static String TAG = "[Client]: ";
    private Socket CLIENT_SOCKET;

    /** DataInputStrea to read server responses */
    private InputStream inFromServer;
    private DataInputStream dataInputStream;


    public AmsClient(String serverName, int port) throws IOException {
        CLIENT_SOCKET = new Socket(serverName, port);

        /** DataInputStrea to read server responses */
        inFromServer = CLIENT_SOCKET.getInputStream();
        dataInputStream = new DataInputStream(inFromServer);
        System.out.println(TAG + "[ Initialized(): Server Response - " + dataInputStream.readUTF() + " ]");
    }

    public void bridgeWithServer() throws Exception {

        /** PrintWriter to write msgs to socket */
        PrintWriter socketPrintWriter = new PrintWriter(CLIENT_SOCKET.getOutputStream(), true);

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
    }



    /**
     * main method to start with.
     *
     * @param args
     */
    public static void main(String[] args) {
        AmsClient client = null;

        try {
            if (args.length != 0) {
                String serverName = args[0];
                int port = Integer.parseInt(args[1]);
                client = new AmsClient(serverName, port);

                client.bridgeWithServer();

            } else {
                client = new AmsClient(AMS_SERVER_NAME, AMS_SERVER_PORT);
                client.bridgeWithServer();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
