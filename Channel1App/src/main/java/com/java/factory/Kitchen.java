package com.java.factory;

/**
 * 厨房，一个饭店可能会有好多个厨房
 * Created by Dawish on 2016/10/16.
 */

public abstract class Kitchen {

    /**
     * 所有厨房对外的方法，顾客向厨房下单，具体的做炒饭在具体的厨房实行，
     * @param type
     * @param userData
     * @return
     */
    public ChaoFan orderChaoFan(String type, String userData){
        ChaoFan chaoFan;
        chaoFan = createChaoFan(type, userData);
        /**我们不管是什么炒饭，下面的三个方法都会有**/
        chaoFan.prepare();
        chaoFan.fry();
        chaoFan.box();
        return chaoFan;
    }

    /**川菜 粤菜 厨房等需要实现的方法，来做炒饭
     * 这样，ChaoFan超类的代码就和鸡蛋炒饭、肉丝炒饭这些子类对象创建代码解耦了。
     *
     **/
    protected abstract ChaoFan createChaoFan(String type, String userData);

}
