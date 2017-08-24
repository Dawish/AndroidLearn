package com.anno.service.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import danxx.library.tools.MyLog;

/**
 * Created by dawish on 2017/8/24.
 * 自己实现Binder实现进程通信
 */

public class BinderService extends Service {

    public BinderService (){
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //将实际的Binder对象返回给服务端
        MyLog.i("danxx", "BinderService onBind");
        return new BookManager().asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.i("danxx", "BinderService onCreate");
    }
}
