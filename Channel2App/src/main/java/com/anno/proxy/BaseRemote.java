package com.anno.proxy;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by dawish on 2017/8/25.
 * 远程服务基类，可以写一些自己特有的方法
 */

public class BaseRemote implements IRemote {

    /**
     * 服务端处理客户端发送过来的数据
     * @throws RemoteException
     */
    protected void onSend(String data) throws RemoteException {
        Log.i("danxx", "接收到数据的默认处理！");
    }

    @Override
    public boolean send(String data) throws RemoteException {
        Log.i("danxx", "执行默认的数据发送行为！");
        if(data != null){
            onSend(data);
        }
        return false;
    }

    @Override
    public IRemote asRemote() {
        return this;
    }
}
