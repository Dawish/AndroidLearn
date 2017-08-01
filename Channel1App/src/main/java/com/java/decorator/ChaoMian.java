package com.java.decorator;

/**
 * @Description:  素炒面  价格 4 元
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 9:27
 */
public class ChaoMian implements IFood {
    /**
     * 主食价格
     */
    @Override
    public int caculatePrice() {
        return 4;
    }

    /**
     * 主食描述
     */
    @Override
    public String description() {
        return "炒面";
    }
}
