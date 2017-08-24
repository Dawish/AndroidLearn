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
import com.anno.service.MyAIDLService;
import com.anno.service.AidlService;
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
    /**本地service使用*/
    private AidlService.MyBinder myBinder;
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
//            myBinder = (MyService.MyBinder) service; //本地service写法
//            myBinder.startDownload();
            myAIDLService = MyAIDLService.Stub.asInterface(service); //远程service写法
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
                    Intent bindIntent = new Intent(this, AidlService.class);
                    bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
                }
                //用Intent匹配的方式绑定service
//                Intent intent = new Intent("com.example.servicetest.MyAIDLService");
//                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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

}
