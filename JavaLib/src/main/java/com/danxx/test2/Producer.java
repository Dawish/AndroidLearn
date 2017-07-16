package com.danxx.test2;

import java.util.Random;

/**
 * 生产者
 * Created by dawish on 2017/7/14.
 */

public class Producer implements Runnable {

    private String name;
    private Channel channel;

    public Producer(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            int sleep = new Random().nextInt(2000);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Good good = new Good();
            channel.put(good);
            System.out.println(name + " Producer-Good:" + good.getName());
        }
    }
}