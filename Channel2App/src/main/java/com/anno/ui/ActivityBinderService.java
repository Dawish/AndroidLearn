package com.anno.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
import com.anno.service.binder.BookManager;
import com.anno.service.binder.IBookManager;
import com.anno.service.data.Book;
import danxx.library.tools.MyLog;

public class ActivityBinderService extends AppCompatActivity {

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
    /**继承Binder类实现的自己的Binder*/
    private IBookManager bookManagerBinder;

    ServiceConnection serviceConnection = new ServiceConnection() {
        /**
         * service连接成功后的回调
         * @param name
         * @param service  通讯接口
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("danxx", "-----onServiceConnected-----");
            isBound = true;
            //将Binder的代理对象返回给客户端
            bookManagerBinder = BookManager.asInterface(service);
//            if(mBinder instanceof IBookManager){
//                bookManagerBinder = (IBookManager) mBinder;
                Log.d("danxx", "-----bookManagerBinder-----");
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            bookManagerBinder = null;
            Log.d("danxx", "-----onServiceDisconnected-----");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_service);
        AnnotateUtils.inject(ActivityBinderService.this);

    }
    @OnClick({R.id.bind_service,R.id.unbind_service,R.id.addData,R.id.getData})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_service:
                if (!isBound) {
                    MyLog.i("danxx", "bind_service");
                    Intent intent = new Intent();
                    intent.setAction("com.danxx.binderService");
                    intent.setPackage("com.anno");
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                }
                break;
            case R.id.unbind_service:
                if(isBound && bookManagerBinder != null) {
                    unbindService(serviceConnection);
                    MyLog.i("danxx", "unbind_service");
                }
                break;
            case R.id.addData:
                addBook();
                break;
            case R.id.getData:
                getBook();
                break;
            default:
                break;
        }
    }
    private void addBook(){
        if(isBound){
            MyLog.i("danxx", "addBook");
            Book book = new Book();
            int size = bookManagerBinder.getBook().size();
            book.setName("book-"+size);
            book.setId((size + 3)%8);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Name:"+book.getName());
            stringBuffer.append("  Id:"+book.getId());
            txt.setText(stringBuffer.toString());
            bookManagerBinder.addBook(book);
        }else {
            Toast.makeText(this,"服务还未连接！", Toast.LENGTH_SHORT).show();
        }

    }

    private void getBook(){
        if(isBound){
            MyLog.i("danxx", "getBook");
            int size = bookManagerBinder.getBook().size();
            txt.setText("Book size: "+ size);
        }else {
            Toast.makeText(this,"服务还未连接！", Toast.LENGTH_SHORT).show();
        }
    }

}
