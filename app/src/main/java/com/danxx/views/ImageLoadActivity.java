package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.dximageloader.DxImageLoader;

/**
 * Created by dawish on 2017/6/26.
 */
public class ImageLoadActivity  extends AppCompatActivity {
    String[] imgUrls = {
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f2p0v9vwr5j20b70gswfi.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f2nxxvgz7xj20hs0qognd.jpg",
            "http://ww2.sinaimg.cn/large/7a8aed7bjw1f2mteyftqqj20jg0siq6g.jpg",
            "http://ww2.sinaimg.cn/large/7a8aed7bjw1f2lkx2lhgfj20f00f0dhm.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f2h04lir85j20fa0mx784.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f2fuecji0lj20f009oab3.jpg",
            "http://ww1.sinaimg.cn/large/610dc034jw1f2ewruruvij20d70miadg.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f2cfxa9joaj20f00fzwg2.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1f2cf4ulmpzj20dw0kugn0.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f27uhoko12j20ez0miq4p.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f27uhoko12j20ez0miq4p.jpg",
            "http://ww2.sinaimg.cn/large/610dc034jw1f27tuwswd3j20hs0qoq6q.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f26lox908uj20u018waov.jpg",
            "http://ww2.sinaimg.cn/large/7a8aed7bjw1f25gtggxqjj20f00b9tb5.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f249fugof8j20hn0qogo4.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1yjc38i9oj20hs0qoq6k.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1f1yj0vc3ntj20e60jc0ua.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1xad7meu2j20dw0ku0vj.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f1w5m7c9knj20go0p0ae4.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1so7l2u60j20zk1cy7g9.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1rmqzruylj20hs0qon14.jpg",
            "http://ww2.sinaimg.cn/large/7a8aed7bjw1f1qed6rs61j20ss0zkgrt.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f1p77v97xpj20k00zkgpw.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f1o75j517xj20u018iqnf.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1klhuc8w5j20d30h9gn8.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1jionqvz6j20hs0qoq7p.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f1ia8qj5qbj20nd0zkmzp.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f1h4f51wbcj20f00lddih.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f1g2xpx9ehj20ez0mi0vc.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        //初始化图片加载库
        DxImageLoader.getInstance().init(ImageLoadActivity.this.getApplicationContext());

    }

    public void load(View v){
        ImageView imageView0 = (ImageView) findViewById(R.id.image0);
        DxImageLoader.getInstance().load(imgUrls[0]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView0);

        ImageView imageView1 = (ImageView) findViewById(R.id.image1);
        DxImageLoader.getInstance().load(imgUrls[1]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView1);

        ImageView imageView2 = (ImageView) findViewById(R.id.image2);
        DxImageLoader.getInstance().load(imgUrls[2]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView2);


        ImageView imageView3 = (ImageView) findViewById(R.id.image3);
        DxImageLoader.getInstance().load(imgUrls[3]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView3);

        ImageView imageView4 = (ImageView) findViewById(R.id.image4);
        DxImageLoader.getInstance().load(imgUrls[4]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView4);

        ImageView imageView5 = (ImageView) findViewById(R.id.image5);
        DxImageLoader.getInstance().load(imgUrls[5]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView5);

        ImageView imageView6 = (ImageView) findViewById(R.id.image6);
        DxImageLoader.getInstance().load(imgUrls[6]).placeholder(R.drawable.default_pic_loading).error(R.drawable.app_bg).into(imageView6);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
