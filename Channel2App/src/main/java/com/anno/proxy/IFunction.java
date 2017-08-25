package com.anno.proxy;

/**
 * Created by dawish on 2017/8/25.
 * 上班狗的功能接口
 */
public interface IFunction {

    /**是人都想做的事情*/
    void eat(String s);
    void speak(String s);
    void sleep(int time);

    /**正常人都不想做的事情*/
    void toGoToTheOffice();
    void work();
    void getOffWork();

    /**用来获取与当前接口关联的Human对象*/
    IFunction asHuman();
}
