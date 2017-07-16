package com.danxx.test2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 消费通道
 * Created by dawish on 2017/7/14.
 */

public class Channel {
	
	private static volatile int goodNumber = 1;
	
	private static final int MAX_SIZE = 20;
	
    private Queue<Good> goodList = new LinkedList<>();

    public synchronized Good get() {
        if (goodList.size() == 0) {
            return null;
        }
        Good good = goodList.remove();
        return good;
    }

    public synchronized void put(Good good) {
    	if(goodList.size() >= MAX_SIZE){
			notify();
			goodNumber = 1;  //仓库满了以后重置标记器
    		return;
    	}
    	good.setName("Good-Num-"+goodNumber++);
        goodList.add(good);
    }
    
    public synchronized boolean isEmpty(){
    	return goodList.isEmpty();
    } 
}
