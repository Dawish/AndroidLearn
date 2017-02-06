package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ndk.NdkJniUtils;

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
        NdkJniUtils jni = new NdkJniUtils();
        mTextView.setText(jni.getCLanguageString());
    }

}
