package com.niki.msgclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mukthar.ahmed on 4/1/16.
 * <p>
 * - AmsClient (Ahmed's Chat client) implementation
 */
public class AmsClient {
    private String CLIENT_ID;
    private static final int AMS_SERVER_PORT = 6066;
    private static final String AMS_SERVER_NAME = "localhost";  //InetAddress.getLocalHost()
    private static String TAG;
    private Socket CLIENT_SOCKET;

    /**
     * DataInputStrea to read server responses
     */
    private InputStream inFromServer;
    private DataInputStream dataInputStream;

    public AmsClient(String name) {
        this.CLIENT_ID = name;
        this.TAG = "[" + CLIENT_ID + "]: ";
    }

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


    public void startClient(String amsServerName, int amsPort) {
        AmsClient client = null;
        try {
            client = new AmsClient(amsServerName, amsPort);
            client.bridgeWithServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * main method to start with.
     *
     * @param args
     */
    public static void main(String[] args) {
        String name = "Client # 1";

        if (args.length != 0) {
            String serverName = args[0];
            int port = Integer.parseInt(args[1]);
            new AmsClient(name).startClient(serverName, port);

        } else {
            new AmsClient(name).startClient(AMS_SERVER_NAME, AMS_SERVER_PORT);

        }
    }
}
