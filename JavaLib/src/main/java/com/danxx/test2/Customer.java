package com.danxx.test2;

/**
 * 消费者
 * Created by dawish on 2017/7/14.
 */

public class Customer implements Runnable {
    private String name;
    private Channel channel;

    public Customer(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
        	synchronized (channel){
	        	if(channel.isEmpty()){
	        		
	        		try {
	                    System.out.println(name + " wait");
	                    channel.wait();
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	        		
	        	}else {
	        		Good good = channel.get();
	        		if (good != null) {
	                    System.out.println(name + " Customer Good:" + good.getName());
	                }
				}
        	}
        }
    }
}
