package com.danxx.javalib2;
import com.danxx.test.Good;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 数据存储仓库和操作
 * 一个缓冲区，缓冲区有最大限制，当缓冲区满
 * 的时候，生产者是不能将产品放入到缓冲区里面的，
 * 当然，当缓冲区是空的时候，消费者也不能从中拿出来产品，
 * 这就涉及到了在多线程中的条件判断
 * Created by dawish on 2017/7/13.
 */
public class Storage {

    private final static int MAX_SIZE = 50;
    /**
     *  Queue操作解析：
     *  add       增加一个元索                 如果队列已满， 则抛出一个IIIegaISlabEepeplian异常
     *  remove    移除并返回队列头部的元素     如果队列为空， 则抛出一个NoSuchElementException异常
     *  element   返回队列头部的元素           如果队列为空， 则抛出一个NoSuchElementException异常
     *  offer     添加一个元素并返回true       如果队列已满， 则返回false
     *  poll      移除并返问队列头部的元素     如果队列为空， 则返回null
     *  peek      返回队列头部的元素           如果队列为空， 则返回null
     *  put       添加一个元素                 如果队列满，   则阻塞
     *  take      移除并返回队列头部的元素     如果队列为空， 则阻塞
     *
     */
    Queue<String> storage;
    public Storage() {
        storage = new LinkedList<String>();
    }

    /**
     *
     * @param dataValue
     */
    public synchronized void put(String dataValue){
        if(storage.size() >= MAX_SIZE){
            try {
                storage.wait();  //当生产满了后让生产线程等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(dataValue);
        storage.notify();
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public synchronized String get() throws InterruptedException {
        if(storage.size() == 0){
            storage.wait();  //当产品仓库为空的时候让消费线程等待
            return null;
        }
        storage.notify();
        return storage.remove();
    }

}
