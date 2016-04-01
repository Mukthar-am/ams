package com.niki.msgclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mukthar.ahmed on 4/1/16.
 * <p>
 * - AmsClient implementation
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

    public void getmain() throws Exception {
        //CLIENT_SOCKET = new Socket(AMS_SERVER_NAME, AMS_SERVER_PORT);


        /** PrintWriter to write msgs to socket */
        PrintWriter pw = new PrintWriter(CLIENT_SOCKET.getOutputStream(), true);

//        /** DataInputStrea to read server responses */
//        InputStream inFromServer = CLIENT_SOCKET.getInputStream();
//        DataInputStream in = new DataInputStream(inFromServer);
//
//        System.out.println(TAG + "[ Initialized(): Server Response - " + in.readUTF() + " ]");


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
                pw.println(input);  /** Sending msg to server socket */

                if (input.equalsIgnoreCase("bye")) {
                    break;

                } else {
                    inFromServer = CLIENT_SOCKET.getInputStream();
                    dataInputStream = new DataInputStream(inFromServer);
                    System.out.println(TAG + "[" + dataInputStream.readUTF() + "]");
                }
            }

        }


        System.out.println("# END: Closing connection.");
        pw.close();
        CLIENT_SOCKET.close();
        scanner.close();
    }


//
//    private void sendMessage(String inMsg) {
//        try {
//            System.out.println(TAG + "Successfully connected to " + CLIENT_SOCKET.getRemoteSocketAddress());
//
//            OutputStream outToServer = CLIENT_SOCKET.getOutputStream();
//            DataOutputStream out = new DataOutputStream(outToServer);
//            out.writeUTF(TAG + " " + inMsg + " " + CLIENT_SOCKET.getLocalSocketAddress());
//            out.writeUTF(inMsg);
//
//            InputStream inFromServer = CLIENT_SOCKET.getInputStream();
//            DataInputStream dataInputStream = new DataInputStream(inFromServer);
//            System.out.println(TAG + "Server response: [" + dataInputStream.readUTF() + "]");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void establishConnection(String serverName, int serverPort) {
//        this.getConnection(serverName, serverPort);
//
//        /** close connection based on user input */
//        Scanner scanner = new Scanner(System.dataInputStream);
//        while (true) {
//            System.out.println("\nMenu Options:");
//            System.out.println("(1) Close server connection. " +
//                "(2) Send message. " +
//                "(3) Quit client");
//            scanner.useDelimiter("\\n");
//
//            String input = scanner.nextLine();
//            if (input.isEmpty()) {
//                System.out.println("# Invalid input.");
//
//            } else {
//                int option = Integer.parseInt(input);
//                System.out.println("= You have entered: " + option);
//
//                System.out.println("Your answer is = " + option);
//                switch (option) {
//                    case 1:             /** Closing client connection */
//                        try {
//                            CLIENT_SOCKET.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.printf("Closed client connection.");
//                        break;
//
//                    case 2:             /** Sending message */
//                        System.out.println("\nWhat's your message ?\n");
//                        String msg = scanner.nextLine();
//                        System.out.println("+ Your msg is: " + msg);
//
//                        sendMessage(msg);   /** Send server socket messages */
//
//                        break;
//
//                    case 3:
//                        scanner.close();
//
//                    default:
//                        System.out.println("No options selected, please see menu options.");
//                }
//            }
//
//        }
//
//    }

    /**
     * main method to start with.
     *
     * @param args
     */
    public static void main(String[] args) {
        AmsClient client;

        try {
            if (args.length != 0) {
                String serverName = args[0];
                int port = Integer.parseInt(args[1]);
                client = new AmsClient(serverName, port);

            } else {

                client = new AmsClient(AMS_SERVER_NAME, AMS_SERVER_PORT);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
