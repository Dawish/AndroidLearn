package com.danxx.javalib;

/**
 * 消费者
 * Created by dawish on 2017/7/13.
 */

public class Consumer implements Runnable {
    /**
     * http://blog.csdn.net/a352193394/article/details/39381271
     * http://xiao-hua.iteye.com/blog/852450
     */
    private EventStorage storage;

    public Consumer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            storage.get();
        }
    }

    public static void main(String[] args) {
        EventStorage storage = new EventStorage();
        Producer producer = new Producer(storage);
        Thread thread1 = new Thread(producer);

        Consumer consumer = new Consumer(storage);
        Thread thread2 = new Thread(consumer);

        thread2.start();
        thread1.start();
    }
}

