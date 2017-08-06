package com.anno.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.ViewInject;
import com.anno.ui.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    @ViewInject(R.id.tv)
    TextView tv;

    @ViewInject(R.id.btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnnotateUtils.inject(MainActivity.this);


        tv.setText("AnnoTv");
        button.setText("AnnoBtn");

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}

