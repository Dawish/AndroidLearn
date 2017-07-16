package com.danxx.javalib2;

import java.util.LinkedList;
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
	
	private static volatile int goodNumber = 1;
	
    private final static int MAX_SIZE = 20;
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
    public synchronized void put(String dataValue, String threadName){
        if(storage.size() >= MAX_SIZE){
            try {
            	goodNumber = 1;
                super.wait();  //当生产满了后让生产线程等待
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(dataValue + goodNumber++);
        System.out.println(threadName + dataValue + goodNumber);
        super.notify();  //每次添加一个数据就唤醒一个消费等待的线程来消费
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public synchronized String get(String threadName) {
        if(storage.size() == 0){
            try {
            	super.wait();  //当产品仓库为空的时候让消费线程等待
                System.out.println(threadName + "wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        super.notify();  //当数据不为空的时候就唤醒一个生产线程来生产
        String value = storage.remove();
        return value;
    }

}
