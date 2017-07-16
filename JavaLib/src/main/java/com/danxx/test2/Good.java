package com.danxx.test2;

/**
 * 商品
 * Created by dawish on 2017/7/14.
 */

public class Good {
    private String name;
    
    public Good(){
    	
    }
    
    public Good(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
