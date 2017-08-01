package com.java.decorator;

/**
 * @Description: 鸡蛋食材  价格 3 元
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 9:53
 */
public class EggDecorator implements IFoodDecorator {

    private IFood iFood;

    /**
     * 在构建食材的时候告诉是用于在什么主食中，
     * 我们只需要使用IFood接口就可以来修改主食的价格和描述，这就是IFood超类的好处
     * @param food
     */
    public EggDecorator(IFood food){
        this.iFood = food;
    }

    /**
     * 修改 主食价格
     */
    @Override
    public int caculatePrice() {
        return iFood.caculatePrice() + 3;
    }

    /**
     * 修改 主食描述
     */
    @Override
    public String description() {
        return "鸡蛋 " + iFood.description() ;
    }
}
