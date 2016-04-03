package com.niki.multi.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  -   Chat client, extending thread. Can instantiate n no. of chat clients.
 *
 */
public class ChatClient extends Thread {

    private String user = "";
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = null;
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);

    private boolean ThreadRunFlag = true;
    private String HOSTNAME;
    private int PORT;

    /**
     * Thread run constructor
     * @param name
     * @param srvHostName
     * @param srvPort
     */
    public ChatClient(String name, String srvHostName, int srvPort) {
        super(name);
        this.HOSTNAME = srvHostName;
        this.PORT = srvPort;

        // Layout GUI
        frame = new JFrame(name);
        textField.setEditable(true);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        /// Add Listeners
        textField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userInput = textField.getText();

                out.println(userInput);
                messageArea.append(user + ": " + textField.getText() + "\n");
                textField.setText("");

            }
        });

    }

    /**
     * Thread run class
     */
    public void run() {
        while (ThreadRunFlag) {
            System.out.println("+ Started the client thread...");
            String serverAddress = "localhost";

            // Process all messages from server, according to the protocol.
            String srvResp = null;
            try {
                Socket socket = new Socket(HOSTNAME, PORT);
                out = new PrintWriter(socket.getOutputStream(), true);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                while (true) {
                    srvResp = dataInputStream.readUTF();
                    System.out.println(srvResp);

                    if (srvResp.startsWith("UserName")) {
                        this.user = getChatUserName();
                        frame.setName(this.user);
                        out.println(this.user);

                    } else if (srvResp.contains("Server")) {
                        messageArea.append(srvResp.substring(0) + "\n");
                    }

                }
            } catch (EOFException eof) {
                frame.setVisible(false);
                frame.dispose();

                showByeDialog(srvResp);
                ThreadRunFlag = false;


                return;


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Accept chat user name
     * @return
     */
    private String getChatUserName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Popup bye-bye dialog box
     * @param msg
     */
    private void showByeDialog(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    /**
     *  Parse cli args and instantiate chat clients
     * @param args
     */
    private static void parseCLI(String[] args) {
        String hostname = "localhost";
        int port = 6061;

        if (args.length == 0) {
            System.out.println("# USAGE: ARGS # 1=[\"<MsgServer-HostName>\" or \"default\"] ARGS # 2=6061" +
                "\nEg: localhost 6061");
            System.exit(-1);
        }

        if (args.length == 1 & args[0].equalsIgnoreCase("default")) {
            System.out.println("# Will be using default configurations of messaging server.");
        }
        else if (args.length == 2) {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        }

        System.out.println("# Using, HostName=" + hostname + ", Port="+ port);

        Thread t1 = new ChatClient("client-1", hostname, port);
        Thread t2 = new ChatClient("client-2", hostname, port);
        t1.start();
        t2.start();

        while (true) {
            if (!t1.isAlive() & !t2.isAlive()) {
                System.out.println("closed/.");
                System.exit(0);
            }
        }
    }


    /**
     * main method to start with.
     * @param args
     */
    public static void main(String[] args) {
        parseCLI(args); /** Parse and handle instantiating clients */
    }

}   // end class
