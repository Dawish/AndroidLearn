package com.anno.proxy;

import android.os.RemoteException;
import android.util.Log;

/**
 * 真实对象RealRemote给服务端调用
 * Created by dawish on 2017/8/25.
 */
public class RealRemote extends BaseRemote implements IFuncation {

    private String data = "";

    @Override
    public IRemote asRemote() {
        return this;
    }

    @Override
    protected void onSend(String data) throws RemoteException {
        if(data != null){
            String temp = "真实角色接收到数据并处理:"+data + " name:围城 " + "price:66元";
            data = temp;
            Log.i("danxx", temp);
            return;
        }
        super.onSend(data);
    }

    /**
     * 返回代理对象
     * @param iRemote
     * @return
     */
    public static IFuncation asProxyFuncation(IRemote iRemote){
        return new ProxyRemote(iRemote);
    }

    @Override
    public String request(String data) {
        return data;
    }

    /**
     * 代理对象ProxyRemote给客户端调用
     */
    public static class ProxyRemote implements IFuncation {
        IRemote iRemote;

        public ProxyRemote(IRemote iRemote){
            this.iRemote = iRemote;
        }

        @Override
        public String request(String data) {
            String newData = "处理后的数据---"+data;
            try {
                iRemote.send(newData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return newData;
        }

    }

}
