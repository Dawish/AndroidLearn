package com.danxx.views;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Description: 一个looper线程
 * @Author: Danxingxi
 * @CreateDate: 2016/8/31 9:58
 */
public class LooperThread extends Thread {
    /**public属性的Handler，其他线程可以通过此handler给本线程的消息队列发送message**/
    /**只有looper线程才可以创建handler，应为一个handler需要持有queue的引用**/
    public Handler handler;
    @Override
    public void run() {
        Looper.prepare();
    /**需要在线程进入死循环之前，创建一个Handler实例供外界线程给自己发消息**/
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //Handler 对象在这个线程构建，那么handleMessage回调方法就在这个线程执行
            }
        };
        Looper.loop();
    }
}
