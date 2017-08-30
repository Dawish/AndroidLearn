package com.anno.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.anno.service.data.ServiceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawish on 2017/8/22.
 * AIDL实现进程通信
 *Android 多进程编程 15问15答！
 * http://www.cnblogs.com/punkisnotdead/p/5168857.html
 * binder原理解析设计篇
 * http://blog.csdn.net/universus/article/details/6211589
 * 一对多
 * http://blog.csdn.net/qq_30379689/article/details/52312026
 * 手动实现Binder
 * http://www.cnblogs.com/punkisnotdead/p/5163464.html
 */

public class AidlService extends Service {

    public static final String TAG = "MyService";
//    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        return mBinder;
        return remoteBinder;
    }

    /**
     * 本地service用法 ；
     */
//    public class MyBinder extends Binder {
//
//        public void startDownload(String url) {
//            Log.d("TAG", "startDownload() executed");
//            // 执行具体的下载任务
//        }
//    }
    /***/
    private List<ServiceData> mData = new ArrayList<>();
    /**
     * 远程service需要使用AIDL来通讯，其实也是基于Binder，只是Google规定了写法
     */
    MyAIDLService.Stub remoteBinder = new MyAIDLService.Stub() {

        @Override
        public void addData(ServiceData data) throws RemoteException {
            mData.add(data);
        }

        @Override
        public List<ServiceData> getDataList() throws RemoteException {
            return mData;
        }
    };



}
