package com.danxx.test2;

/**
 * Created by dawish on 2017/7/14.
 */

public class Main {
    public static void main(String[] args) {
        Channel channel = new Channel();
        new Thread(new Producer("Producer-1", channel)).start();
        new Thread(new Producer("Producer-2", channel)).start();
        new Thread(new Producer("Producer-3", channel)).start();

        new Thread(new Customer("Customer-1", channel)).start();
        new Thread(new Customer("Customer-2", channel)).start();
        new Thread(new Customer("Customer-3", channel)).start();
    }
}
