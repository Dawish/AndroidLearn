package com.java.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.danxx.views.R;
import com.java.decorator.ChaoFan;
import com.java.decorator.ChaoMian;
import com.java.decorator.EggDecorator;
import com.java.decorator.IFood;
import com.java.decorator.RoseDecorator;

/**
 *
 *  顾客点单
 *
 * @Description: 装饰者设计模式测试
 * @Author: Danxingxi
 * @CreateDate: 2016/10/13 10:00
 */
public class ActivityDecorator extends AppCompatActivity {

    private TextView name,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorator);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
    }

    /**
     * 做一份肉丝炒饭
     * @return
     */
    public void roseChaoFan(View view){
        IFood iFood = new RoseDecorator(new ChaoFan());
        name.setText("主食: "+ iFood.description());
        price.setText("价格: "+ iFood.caculatePrice() +" 元");
    }

    /**
     * 做一份肉丝鸡蛋炒饭
     * @return
     */
    public void roseEggChaoFan(View view){
        IFood iFood = new RoseDecorator(new EggDecorator(new ChaoFan()));
        name.setText("主食: "+ iFood.description());
        price.setText("价格: "+ iFood.caculatePrice() +" 元");
    }

    /**
     * 做一份鸡蛋肉丝炒面
     * @return
     */
    public void eggRoseChaoMian(View view){
        IFood iFood = new EggDecorator(new RoseDecorator(new ChaoMian()));
        name.setText("主食: "+ iFood.description());
        price.setText("价格: "+ iFood.caculatePrice() +" 元");
    }

    @Override
    public void finish() {
        super.finish();
    }
}
