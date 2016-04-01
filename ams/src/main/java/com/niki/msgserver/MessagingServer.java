package com.niki.msgserver;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  - Simple abstraction for Messaging server
 */

public interface MessagingServer {
    public void startServer();  /** Method to start msg server */
    public void stopServer();   /** Method to stop msg server */
}
