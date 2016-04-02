package com.niki.multi.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mukthar.ahmed on 4/2/16.
 */
public class ChatClientThread extends Thread {
    private String user = "";
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = null;
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);

    public ChatClientThread(String name) {
        super(name);

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

    public void run() {
        System.out.println("+ Started the client thread...");
        String serverAddress = "localhost";

        // Process all messages from server, according to the protocol.
        String srvResp = null;
        try {
            Socket socket = new Socket(serverAddress, 6061);
            out = new PrintWriter(socket.getOutputStream(), true);

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            while (true) {
                srvResp = dataInputStream.readUTF();
                System.out.println(srvResp);

                if (srvResp.startsWith("UserName")) {
                    this.user = getChatUserName();
                    frame.setName(this.user);
                    out.println(this.user);

                }
                else if (srvResp.contains("Server")) {
                    messageArea.append(srvResp.substring(0) + "\n");
                }

            }
        } catch (EOFException eof) {
            frame.setVisible(false);
            frame.dispose();

            showByeDialog(srvResp);

            System.exit(0);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getChatUserName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    private void showByeDialog(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    public static void main(String[] args) {
        Thread t1 = new ChatClientThread("client-1");
        t1.start();
    }
}
