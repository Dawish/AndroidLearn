package com.java.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.java.factory.ChaoFan;
import com.java.factory.ChuanKitchen;

/**
 * Created by Dawish on 2016/10/16.
 */

public class ActivityFactory extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChuanKitchen chuanKitchen = new ChuanKitchen();

        ChaoFan eggChaoFan = chuanKitchen.orderChaoFan("EggChaoFan" , "西湖区西溪湿地");

        Log.d("danxx", ""+eggChaoFan.getUserData());

    }
}
