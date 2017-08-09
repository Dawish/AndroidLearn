package com.anno.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import danxx.library.widget.RecyclerWheelView;

/**
 * Created by dawish on 2017/8/9.
 */

public class ActivityRecyclerWheelView extends AppCompatActivity {

    @ViewInject(R.id.recyclerWheelView)
    RecyclerWheelView recyclerWheelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclervheelview);

        AnnotateUtils.inject(ActivityRecyclerWheelView.this);

        ArrayList<String> data = new ArrayList<String>();
        for(int i=0;i<29;i++){
            data.add("data: "+i);
        }

        recyclerWheelView.setData(data);

    }
}
