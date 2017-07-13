package com.danxx.javalib;

/**
 * 生产者
 * Created by dawish on 2017/7/13.
 */
public class Producer implements Runnable {

    private EventStorage storge;

    public Producer(EventStorage storage) {
        this.storge = storage;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            storge.set();
        }
    }

}
