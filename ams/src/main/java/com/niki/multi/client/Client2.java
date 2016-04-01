package com.niki.multi.client;

import java.io.IOException;

/**
 * Created by mukthar.ahmed on 4/2/16.
 *
 *  - Client simulatin # 1
 */
public class Client2 {
    public static void main(String[] args) {
        try {
            new ClientThread("client # 2").start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
