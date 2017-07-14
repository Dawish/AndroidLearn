package com.danxx.javalib;

/**
 *  Java中的多线程会涉及到线程间通信，常见的线程通信方式，例如共享变量、管道流等，
 *  这里我们要实现生产者消费者模式，也需要涉及到线程通信，不过这里我们用到了java中的
 *  wait()、notify()方法：
 *  wait()：进入临界区的线程在运行到一部分后，发现进行后面的任务所需的资源还没有准备充分，
 *  所以调用wait()方法，让线程阻塞，等待资源，同时释放临界区的锁，此时线程的状态也从RUNNABLE状态变为WAITING状态；
 *  notify()：准备资源的线程在准备好资源后，调用notify()方法通知需要使用资源的线程，
 *  同时释放临界区的锁，将临界区的锁交给使用资源的线程。
 *  wait()、notify()这两个方法，都必须要在临界区中调用，即是在synchronized同步块中调用，
 *  不然会抛出IllegalMonitorStateException的异常。
 *  Created by dawish on 2017/7/14.
 */

public class App {
    public static void main(String[] args) {
        Storage storage = new Storage();
        Producer producer = new Producer(storage.getStorage());
        Consumer consumer = new Consumer(storage.getStorage());
        producer.start();
        consumer.start();
    }
}
