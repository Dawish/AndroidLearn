package com.danxx.javalib2;

import java.util.List;
import java.util.UUID;

/**
 * 生产者
 * Created by dawish on 2017/7/13.
 */
public class Producer extends Thread{

    private final static int MAX_SIZE = 50;

    private List<String> storage;//生产者仓库
    public Producer(List<String> storage) {
        this.storage = storage;
    }
    public void run(){
        //生产者每隔1s生产1~100消息
        long oldTime = System.currentTimeMillis();
        while(true){
            synchronized(storage){
                if (System.currentTimeMillis() - oldTime >= 1000) {
                    oldTime = System.currentTimeMillis();
                    int size = (int)(Math.random()*100) + 1;
                    for (int i = 0; i < size; i++) {
                        String msg = UUID.randomUUID().toString();
                        storage.add(msg);
                    }
                    System.out.println("Thread Name:"+this.getName()+"  Producer Data Size:"+size);
                    storage.notify();  //生产满了，通知消费线程消费
                }
            }
        }
    }
}