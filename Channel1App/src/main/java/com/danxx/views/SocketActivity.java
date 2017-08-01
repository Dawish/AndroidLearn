package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by dawish on 2017/7/19.
 * SocketTest android
 */

public class SocketActivity extends AppCompatActivity {

    private EditText mEditText;
    private TextView mTextView;
    private static final String TAG = "TAG";
    private static final String HOST = "192.168.123.27";
    private static final int PORT = 1024;
    private PrintWriter printWriter;
    private BufferedReader in;
    private ExecutorService mExecutorService = null;
    private String receiveMsg;

    private BufferedSink mSink;
    private BufferedSource mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void connect(View view) {
        mExecutorService.execute(new connectService());
    }

    public void send(View view) {
        String sendMsg = mEditText.getText().toString();
        mExecutorService.execute(new sendService(sendMsg));
    }

    public void disconnect(View view) {
        mExecutorService.execute(new sendService("0"));
    }

//    private class sendService implements Runnable {
//        private String msg;
//
//        sendService(String msg) {
//            this.msg = msg;
//        }
//
//        @Override
//        public void run() {
//            printWriter.println(this.msg);
//        }
//    }
//
//    private class connectService implements Runnable {
//        @Override
//        public void run() {
//            try {
//                Socket socket = new Socket(HOST, PORT);
//                socket.setSoTimeout(60000);
//                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                        socket.getOutputStream(), "UTF-8")), true);
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                receiveMsg();
//            } catch (Exception e) {
//                Log.e(TAG, ("connectService:" + e.getMessage()));
//            }
//        }
//    }
//
//    private void receiveMsg() {
//        while (true) {
//            try {
//                if ((receiveMsg = in.readLine()) != null) {
//                    Log.d(TAG, "receiveMsg:" + receiveMsg);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextView.setText(receiveMsg + "\n\n" + mTextView.getText());
//                        }
//                    });
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /*********************okio简化IO操作优化*******************************/
    private class sendService implements Runnable {
        private String msg;

        sendService(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                mSink.writeUtf8(this.msg+"\n");
                mSink.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class connectService implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(HOST, PORT);
                mSink = Okio.buffer(Okio.sink(socket));
                mSource = Okio.buffer(Okio.source(socket));
                receiveMsg();
            } catch (Exception e) {
                Log.e(TAG, ("connectService:" + e.getMessage()));
            }
        }
    }

    private void receiveMsg() {
        try {
            while (true) {
                for (String receiveMsg; (receiveMsg = mSource.readUtf8Line()) != null; ) {
                    Log.d(TAG, "receiveMsg:" + receiveMsg);
                    final String finalReceiveMsg = receiveMsg;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(finalReceiveMsg + "\n\n" + mTextView.getText());
                        }
                    });
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "receiveMsg: ");
            e.printStackTrace();
        }
    }
}
