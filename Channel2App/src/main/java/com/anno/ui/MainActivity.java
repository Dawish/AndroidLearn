package com.anno.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.OnClick;
import com.anno.annotation.ViewInject;
import com.anno.ui.dummy.DummyContent;
import com.anno.webview.WebViewActivity;


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

    @OnClick(R.id.btn)
    public void click(View view){
        Intent intent = new Intent(MainActivity.this, ActivityRecyclerWheelView.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn2)
    public void clickService(View view){
        Intent intent = new Intent(MainActivity.this, ActivityAidlService.class);
        startActivity(intent);
//        WebViewActivity.webviewEntrance(MainActivity.this, "http://blog.csdn.net/u010072711/article/details/77040159");
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        toGoodDetail("101");
    }
    public void toGoodDetail(String id){
        Intent intent = new Intent(MainActivity.this, ActivityDetail.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}

