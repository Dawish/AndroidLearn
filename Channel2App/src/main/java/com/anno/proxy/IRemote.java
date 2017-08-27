package com.anno.proxy;

import android.os.RemoteException;

/**
 * Created by dawish on 2017/8/25.
 * 远程功能接口
 */
public interface IRemote extends IFuncation {

    /**用来获取与当前接口关联的Remote对象*/
    IRemote asRemote();
    /**
     * 在CS模式中，客户端一次请求会产生一次request 和 一次response
     * 会交换一次数据，客户端携带参数发送请求后会收到服务端的响应数据
     * send承担着发送和响应两次数据传送
     */
    boolean send(String data) throws RemoteException;
}
