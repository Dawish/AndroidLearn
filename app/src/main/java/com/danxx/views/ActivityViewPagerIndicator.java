package com.danxx.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import danxx.library.widget.TabStrip;

/**
 * Created by Dawish on 2017/1/7.
 */

public class ActivityViewPagerIndicator  extends AppCompatActivity {

    private ViewPager viewPager;
    private TabStrip tabStrip;
    private String[] titles = new String[] { "大小桥", " 波多", "未久久", "小乔", "阿萨", "撒旦", "递归",
            "搞死", "大防" };
    /**
     * 指示器偏移宽度
     */
    private int offsetWidth = 0;

    private ViewPager mViewPager;

    /**
     * viewPager宽度
     */
    private int screenWith = 0;
    /**
     * viewPager高度
     */
    private int screeHeight = 0;

    private int[] drawableResIds = {R.mipmap.mm1,R.mipmap.mm2,R.mipmap.mm3,R.mipmap.mm4,R.mipmap.mm5,
            R.mipmap.mm6,R.mipmap.mm7,R.mipmap.mm8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        tabStrip = (TabStrip) findViewById(R.id.tabstrip);


        screenWith = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        screeHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight()-dip2px(this, 45);
        //这里之所以是45，请查看布局文件，其中ViewPager以上的节点的高度总和为45

        mViewPager.setAdapter(new ViewPagerAdapter());
        tabStrip.setViewPager(mViewPager);

    }
    private class ViewPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount() {
            return drawableResIds.length;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = (ImageView) LayoutInflater.from(ActivityViewPagerIndicator.this).inflate(R.layout.image_display, null);
            imageView.setImageBitmap(adjustBitmapSimpleSize(drawableResIds[position]));
            imageView.setTag(position);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            ImageView image = (ImageView)((ViewPager) container).findViewWithTag(position);
            ((ViewPager) container).removeView(image);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0==arg1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
            return titles[position];
        }
    }


    /**
     * 调整压缩采样率
     * @param resId
     * @return
     */
    private Bitmap adjustBitmapSimpleSize(int resId)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resId, opts);
        int visibleHeight = screeHeight;
        int visibleWidth = screenWith;
        if(opts.outWidth>visibleWidth ||opts.outHeight>visibleHeight)
        {
            float wRatio =  opts.outWidth/visibleWidth;
            float hRatio =  opts.outHeight/visibleHeight;
            opts.inSampleSize = (int) Math.max(wRatio, hRatio);
        }
        opts.inJustDecodeBounds = false;
        if(bitmap!=null){
            bitmap.recycle();
        }
        return BitmapFactory.decodeResource(getResources(),resId, opts);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
