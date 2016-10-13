package com.java.decorator;

/**
 * @Description: 添加食材的超类接口，就是继承了IFood接口，当
 *               食材实现这个接口的时候需要用到IFood中的方法来对价格和描述做出修改
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 9:31
 */
public interface IFoodDecorator extends IFood {
}
