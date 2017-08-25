package com.anno.proxy;

/**
 * Created by dawish on 2017/8/25.
 * IInterface这是任何实现Human的子类必须实现的接口,
 * 用来获取与当前接口关联的Human对象
 */

public interface IInterface {

    void toGoToTheOffice();

    void work();

    void getOffWork();

    IFunction asHuman();
}
