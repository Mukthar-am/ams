package com.niki.multi.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mukthar.ahmed on 4/2/16.
 * <p>
 * - Messaging server code, thread type implementation
 */
public class AmsMultiClientServer {

    private static final int PORT = 6061;
    private static HashSet<String> names = new HashSet<>();
    private static HashSet<DataOutputStream> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new AmsServer(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }


    private static class AmsServer extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private DataOutputStream out;

        public AmsServer(Socket socket) {
            super("AmsMultiClientServer");
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    out.writeUTF("UserName");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }


                writers.add(out);

                /** Session for accepting messages start from here */
                System.out.println("\n# [Session start: " + name + "]");
                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println("Incoming Message: " + input);

                    String srvResp = serverResponses(input.toLowerCase());

                    if (srvResp == null) {
                        input = "[Server - " + name + "]: " + input;
                        out.writeUTF(input);
                    }
                    else if (srvResp != null & input.equalsIgnoreCase("bye") ) {
                        input = "[Server - " + name + "]: " + srvResp;
                        out.writeUTF(input);

                        System.out.println(input);
                        System.out.println("# [Closing the session: " + name + "]");

                        break;
                    }
                    else {
                        input = "[Server - " + name + "]: " + srvResp;
                        out.writeUTF(input);
                        System.out.println(input);
                    }

                }

            } catch (IOException e) {
                System.out.println(e);

            } finally {
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }

    private static String serverResponses(String inKey) {
        HashMap<String, String> responses = new HashMap<>();

        responses.put("hi", "Hello");
        responses.put("how are you doing", "Doing good, thanks for asking.");
        responses.put("bye", "Bye bye, Hope we addressed your concerns!");

        if (responses.containsKey(inKey)) {
            return responses.get(inKey);
        } else {
            return null;
        }

    }


}
