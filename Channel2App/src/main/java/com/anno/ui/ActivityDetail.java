package com.anno.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anno.App;
import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.OnClick;
import com.anno.annotation.ViewInject;

/**
 * Created by dawish on 2017/8/10.
 */

public class ActivityDetail extends AppCompatActivity {

    @ViewInject(R.id.recGood1)
    private Button recGood1;

    @ViewInject(R.id.recGood2)
    private Button recGood2;

    @ViewInject(R.id.recGood3)
    private Button recGood3;

    @ViewInject(R.id.currentGoodId)
    private TextView currentGoodId;

    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        AnnotateUtils.inject(ActivityDetail.this);

        ID = getIntent().getStringExtra("ID");
        currentGoodId.setText("当前详情页展示商品ID: "+ID);

    }

    public String getID() {
        return ID;
    }

    @OnClick({R.id.recGood1, R.id.recGood2, R.id.recGood3})
    public void recGoodClick(View v){

        int id = v.getId();

        switch (id){
            case R.id.recGood1:
                String goodId1 = "101";
                toGoodDetail(goodId1);
                break;
            case R.id.recGood2:
                String goodId2 = "102";
                toGoodDetail(goodId2);
                break;
            case R.id.recGood3:
                String goodId3 = "103";
                toGoodDetail(goodId3);
                break;
        }

    }

    /**
     * 根据推荐商品的点击打开对应的详情页
     * @param id
     */
    public void toGoodDetail(String id){
        App.toGoodDetail(id);
        Intent intent = new Intent(ActivityDetail.this, ActivityDetail.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}
