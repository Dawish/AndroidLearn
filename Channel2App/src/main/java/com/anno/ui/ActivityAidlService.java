package com.anno.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.OnClick;
import com.anno.annotation.ViewInject;
import com.anno.service.aidl.AidlService;
import com.anno.service.aidl.MyAIDLService;
import com.anno.service.data.ServiceData;

public class ActivityAidlService extends AppCompatActivity {

    @ViewInject(R.id.start_service)
    private Button startService;

    @ViewInject(R.id.stop_service)
    private Button stopService;

    @ViewInject(R.id.bind_service)
    private Button bindService;

    @ViewInject(R.id.unbind_service)
    private Button unbindService;

    @ViewInject(R.id.addData)
    private Button addData;

    @ViewInject(R.id.getData)
    private Button getData;

    @ViewInject(R.id.txt)
    private TextView txt;
    /**服务是否连接*/
    private boolean isBound;
//    /**本地service使用*/
//    private AidlService.MyBinder myBinder;
    /**远程service使用*/
    private MyAIDLService myAIDLService;
    /**
     *  ServiceConnection is Interface for monitoring the state of an application service
     *  ServiceConnection是一个观察程序service的回调接口
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        /**
         * service连接成功后的回调
         * @param name
         * @param service  通讯接口
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
//            myBinder = (AidlService.MyBinder) service; //本地service写法
//            myBinder.startDownload("http://slide.news.sina.com.cn/iframe/download.php?img=http://k.sinaimg.cn/n/eladies/3_ori/upload/4e8441c4/20170811/eokO-fyixhyw7231373.jpg/w5000hdp.jpg");
            myAIDLService = MyAIDLService.Stub.asInterface(service); //远程service写法
            try {
                //设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            myAIDLService = null;
            Log.d("danxx", "-----onServiceDisconnected-----");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        AnnotateUtils.inject(ActivityAidlService.this);
    }

    /**
     * Service 的两种启动方法和区别
     Service的生命周期方法onCreate, onStart, onDestroy
     有两种方式启动一个Service,他们对Service生命周期的影响是不一样的。
     1 通过startService
     　　Service会经历 onCreate -> onStart
     　stopService的时候直接onDestroy
     如果是调用者自己直接退出而没有调用stopService的话，Service会一直在后台运行。下次调用者再起来可以stopService。
     2 通过bindService
     　　Service只会运行onCreate， 这个时候服务的调用者和服务绑定在一起
     调用者退出了，Srevice就会调用onUnbind->onDestroyed所谓绑定在一起就共存亡了。并且这种方式还可以使得
     *
     */

    @OnClick({R.id.start_service,R.id.stop_service,R.id.bind_service,
            R.id.unbind_service,R.id.addData,R.id.getData})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, AidlService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, AidlService.class);
                stopService(stopIntent);
                Log.i("danxx", "stopService");
                break;
            case R.id.bind_service:
                if (!isBound) {
//                    Intent bindIntent = new Intent(this, AidlService.class);
//                    bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
                    //用Intent匹配的方式绑定service
                    Intent intent = new Intent();
                    intent.setAction("com.danxx.aidlService");
                    intent.setPackage("com.anno");
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                }
                break;
            case R.id.unbind_service:
                if(isBound && myAIDLService != null) {
                    unbindService(serviceConnection);
                    Log.i("danxx", "unbindService");
                }
                break;
            case R.id.addData:
                addData();
                break;
            case R.id.getData:
                getData();
                break;
            default:
                break;
        }
    }

    private void addData(){
        if(isBound){
            ServiceData serviceData = new ServiceData();
            try {
                int size = myAIDLService.getDataList().size();
                serviceData.setName("no-"+size);
                serviceData.setId(String.valueOf(size));
                serviceData.setPrice(String.valueOf(size+2));
                serviceData.setType(String.valueOf(size%8));

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Name:"+serviceData.getName());
                stringBuffer.append("  Id:"+serviceData.getId());
                stringBuffer.append("  Type:"+serviceData.getType());
                stringBuffer.append("  Price:"+serviceData.getPrice());
                txt.setText(stringBuffer.toString());
                myAIDLService.addData(serviceData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"服务还未连接！", Toast.LENGTH_SHORT).show();
        }

    }

    private void getData(){
        if(isBound){
            try {
                int size = myAIDLService.getDataList().size();
                txt.setText("Data size: "+ size);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"服务还未连接！", Toast.LENGTH_SHORT).show();
        }
    }
    //客户端使用死亡代理，可以重启service
    //http://blog.csdn.net/liuyi1207164339/article/details/51706585
    //服务端使用死亡回调回收数据
    //http://www.cnblogs.com/punkisnotdead/p/5158016.html
    //死亡通知原理分析
    //http://light3moon.com/2015/01/28/Android%20Binder%20%E5%88%86%E6%9E%90%E2%80%94%E2%80%94%E6%AD%BB%E4%BA%A1%E9%80%9A%E7%9F%A5[DeathRecipient]/
    /**
     * 监听Binder是否死亡
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (myAIDLService == null) {
                return;
            }
            //死亡后解除绑定
            myAIDLService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            myAIDLService = null;
            //重新绑定
            doBindService();
            Log.i("danxx", "binderDied 重连");
        }
    };

    private void doBindService(){
        Intent intent = new Intent();
        intent.setAction("com.danxx.aidlService");
        intent.setPackage("com.anno");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

}
