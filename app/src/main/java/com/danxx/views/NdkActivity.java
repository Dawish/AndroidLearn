package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jzp.myapplication.JniUtils;

/**
 * Created by Dawish on 2017/2/4.
 */

public class NdkActivity extends AppCompatActivity {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
        mTextView = (TextView) this.findViewById(R.id.ndkText);

        String encrypt = JniUtils.myEncrypt("123456");
        String decrypt = JniUtils.myDecrypt(encrypt);

        mTextView.setText(JniUtils.getStringC()+"\n未加密:\t123456"+"\n加密:\t"+encrypt
                +"\n解密:\t"+decrypt);

//        mTextView.setText(jni.getCLanguageString());
    }

}
