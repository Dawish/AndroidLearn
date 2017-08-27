package com.anno.proxy;

import android.os.RemoteException;
import android.util.Log;

/**
 * 真实对象RealRemote给服务端调用
 * Created by dawish on 2017/8/25.
 */
public class RealRemote extends BaseRemote {

    @Override
    public IRemote asRemote() {
        return this;
    }

    @Override
    protected void onSend(String data) throws RemoteException {
        super.onSend(data);
        if(data != null){
            return;
        }
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
        Log.d("danxx", "远程服务真实角色接收到数据并处理");
        String temp = data + " name:围城 " + "price:66元";
        return temp;
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
            try {
                iRemote.send(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return iRemote.request(data);
        }

    }

}
