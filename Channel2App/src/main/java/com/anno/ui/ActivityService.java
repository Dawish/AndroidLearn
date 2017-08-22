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

import com.anno.R;
import com.anno.service.MyAIDLService;
import com.anno.service.MyService;

public class ActivityService extends AppCompatActivity implements View.OnClickListener{

    private Button startService;
    private Button stopService;
    private Button bindService;
    private Button unbindService;

    /**本地service使用*/
    private MyService.MyBinder myBinder;
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
//            myBinder = (MyService.MyBinder) service; //本地service写法
//            myBinder.startDownload();
            myAIDLService = MyAIDLService.Stub.asInterface(service); //远程service写法
            Log.d("danxx", "result is " );
            Log.d("danxx", "upperStr is ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
                //用Intent匹配的方式绑定service
//                Intent intent = new Intent("com.example.servicetest.MyAIDLService");
//                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(serviceConnection);
                break;
            default:
                break;
        }
    }
}
