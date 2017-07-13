package com.danxx.javalib;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
/**
 * 数据存储仓库和操作
 * 一个缓冲区，缓冲区有最大限制，当缓冲区满
 * 的时候，生产者是不能将产品放入到缓冲区里面的，
 * 当然，当缓冲区是空的时候，消费者也不能从中拿出来产品，
 * 这就涉及到了在多线程中的条件判断
 * Created by dawish on 2017/7/13.
 */
public class EventStorage {

    private int maxSize;

    private List<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<Date>();
    }

    public synchronized void set() {
        while(storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        storage.add(new Date());
        System.out.printf("Set: %d",storage.size());
        notifyAll();
    }

    public synchronized void get() {
        while(storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.printf("Get: %d: %s",storage.size(),((LinkedList<?>)storage).poll());
        notifyAll();
    }

}
