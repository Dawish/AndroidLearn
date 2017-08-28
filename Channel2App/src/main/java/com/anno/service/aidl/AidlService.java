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
 * http://blog.csdn.net/u013700502/article/details/66477378
 * http://www.cnblogs.com/punkisnotdead/p/5168857.html
 * http://blog.csdn.net/universus/article/details/6211589
 */

public class AidlService extends Service {

    public static final String TAG = "MyService";
    private MyBinder mBinder = new MyBinder();

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
     * 本地service用法 Binder就是通讯的桥；
     * 点击Bind Service按钮就会崩溃呢？这是由于在Bind Service按钮的点击事件里面我们会让Activity和MyService建立关联，
     * 但是目前MyService已经是一个远程Service了，Activity和Service运行在两个不同的进程当中，
     * 这时就不能再使用传统的建立关联的方式，程序也就崩溃了。
     * 那么如何才能让Activity与一个远程Service建立关联呢？这就要使用AIDL来进行跨进程通信了（IPC）。
     */
    public class MyBinder extends Binder {

        public void startDownload() {
            Log.d("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }
    }
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
