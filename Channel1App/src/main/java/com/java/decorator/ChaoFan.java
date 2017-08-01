package com.java.decorator;

/**
 * @Description:  素炒饭 价格 5 元
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 9:26
 */
public class ChaoFan implements IFood {
    /**
     * 主食价格
     */
    @Override
    public int caculatePrice() {
        return 5;
    }

    /**
     * 主食描述
     */
    @Override
    public String description() {
        return "炒饭";
    }
}
