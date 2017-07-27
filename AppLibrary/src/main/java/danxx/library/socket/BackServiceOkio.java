package danxx.library.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Arrays;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zjyh on 2017/7/26.
 */

public class BackServiceOkio extends Service {
    private static final String TAG = "danxx";
    private static final long HEART_BEAT_RATE = 3 * 1000;

    public static final String HOST = "192.168.123.27";// "192.168.1.21";//
    public static final int PORT = 9800;

    public static final String MESSAGE_ACTION="message_ACTION";
    public static final String HEART_BEAT_ACTION="heart_beat_ACTION";

    private BackServiceOkio.ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;  //弱引用防止泄露
    private BufferedSource mSource = null; //输入流源可读
    private BufferedSink mSink = null; //输出流源可写

    // For heart Beat
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = sendMsg("HeartBeat");//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    new BackServiceOkio.InitSocketThread().start();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private long sendTime = 0L;
    private IBackService.Stub iBackService = new IBackService.Stub() {

        @Override
        public boolean sendMessage(String message) throws RemoteException {
            return sendMsg(message);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return iBackService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new BackServiceOkio.InitSocketThread().start();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);

    }
    public boolean sendMsg(final String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        final Socket soc = mSocket.get();
        if (!soc.isClosed() && !soc.isOutputShutdown()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(mSink == null){
                            mSink = Okio.buffer(Okio.sink(soc));
                        }
                        String message = msg + "\r\n";
                        mSink.writeUtf8(message);
                        mSink.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
        } else {
            return false;
        }
        return true;
    }

    private void initSocket() {//初始化Socket
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);
            mReadThread = new BackServiceOkio.ReadThread(so);
            mReadThread.start();
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     * @param mSocket
     */
    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (!sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
            if(mSocket!=null){
                mSource.close();
                mSource = null;
            }
            if(mSink!=null){
                mSink.close();
                mSink = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Log.d("danxx", "run");
                try {
                    Socket socket = mWeakSocket.get();
                    Log.d("danxx", "start");
                    while (!socket.isClosed() && !socket.isInputShutdown() && isStart ) {
                        BufferedSource dxSource = null;
                        Log.d("danxx", "mSource = Okio.buffer");
                            if (null != socket) {
                                dxSource = Okio.buffer(Okio.source(socket));
                            }else {
                                Log.d("danxx", "=====");
                            }
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String receiveMsg; (receiveMsg = dxSource.readUtf8Line()) != null; ) {
                                if(!TextUtils.isEmpty(receiveMsg)){
                                    stringBuffer.append(receiveMsg);
                                }
                        }
                        Log.d("danxx", "while----->"+stringBuffer.toString());
                        String message = stringBuffer.toString();
                        Log.d("danxx", "message-->");
                        Log.e(TAG, message);
                        //收到服务器过来的消息，就通过Broadcast发送出去
                        if(message.equals("ok")){//处理心跳回复
                            Intent intent=new Intent(HEART_BEAT_ACTION);
                            mLocalBroadcastManager.sendBroadcast(intent);
                        }else{
                            //其他消息回复
                            Intent intent=new Intent(MESSAGE_ACTION);
                            intent.putExtra("message", message);
                            mLocalBroadcastManager.sendBroadcast(intent);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(heartBeatRunnable);
        mReadThread.release();
        releaseLastSocket(mSocket);
    }
}
