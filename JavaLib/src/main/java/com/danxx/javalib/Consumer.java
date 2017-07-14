package com.danxx.javalib;

import java.util.List;

/**
 * 消费者
 * Created by dawish on 2017/7/13.
 */

public class Consumer extends Thread{

    private List<String> storage;//仓库
    public Consumer(List<String> storage) {
        this.storage = storage;
    }
    public void run(){
        while(true){
            synchronized(storage){
                //消费者去仓库拿消息的时候，如果发现仓库数据为空，则等待
                if (storage.isEmpty()) { // 消费完时,挂起
                    try {
                        storage.wait(); //使当前消费产品的线程 进入休眠
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int size = storage.size();
                for (int i = size - 1; i >= 0; i--) {
                    storage.remove(i);
                }
                System.out.println("Thread Name:"+this.getName()+"  Consumer Data Size:"+size);
            }
        }
    }
}