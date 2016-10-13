package com.java.decorator;

/**
 * @Description: 肉丝添加食材
 *               价格： 5 元
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 9:35
 */
public class RoseDecorator implements IFoodDecorator {

    private IFood iFood;

    /**
     * 在构建食材的时候告诉是用于在什么主食中，
     * 我们只需要使用IFood接口就可以来修改主食的价格和描述，这就是IFood超类的好处
     * @param food
     */
    public RoseDecorator(IFood food){
        this.iFood = food;
    }

    /**
     * 主食价格
     */
    @Override
    public int caculatePrice() {
        /**在主食本来具有的价格上增加 5 元**/
        return iFood.caculatePrice() + 5;
    }

    /**
     * 主食描述
     */
    @Override
    public String description() {
        /**在主食本来具有的描述上增加 '肉丝' 描述 **/
        return "肉丝 "+iFood.description();
    }
}
