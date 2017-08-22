package com.anno.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anno.App;
import com.anno.R;
import com.anno.annotation.AnnotateUtils;
import com.anno.annotation.OnClick;
import com.anno.annotation.ViewInject;

import java.util.Collections;

/**
 * Created by dawish on 2017/8/10.
 */

public class ActivityDetail extends AppCompatActivity {

    @ViewInject(R.id.recGoods1)
    private Button recGoods1;

    @ViewInject(R.id.recGoods2)
    private Button recGoods2;

    @ViewInject(R.id.recGoods3)
    private Button recGoods3;

    @ViewInject(R.id.currentGoodsId)
    private TextView currentGoodsId;

    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        AnnotateUtils.inject(ActivityDetail.this);

        ID = getIntent().getStringExtra("ID");
        currentGoodsId.setText("当前详情页展示商品ID: "+ID);

    }

    public String getID() {
        return ID;
    }

    @OnClick({R.id.recGoods1, R.id.recGoods2, R.id.recGoods3})
    public void recGoodClick(View v){

        int id = v.getId();

        switch (id){
            case R.id.recGoods1:
                String goodId1 = "101";
                toGoodDetail(goodId1);
                break;
            case R.id.recGoods2:
                String goodId2 = "102";
                toGoodDetail(goodId2);
                break;
            case R.id.recGoods3:
                String goodId3 = "103";
                toGoodDetail(goodId3);
                break;
        }

    }

    /**
     * 根据推荐商品的点击打开对应的详情页
     * @param id
     */

    public void toGoodDetail(@NonNull String id){
        App.toGoodsDetail(id); //调用App中的方法去检测点击的商品详情页是否被打开，被打开就将其关闭
        Intent intent = new Intent(ActivityDetail.this, ActivityDetail.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}
