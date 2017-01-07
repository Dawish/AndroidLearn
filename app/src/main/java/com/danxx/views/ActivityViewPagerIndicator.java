package com.danxx.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import danxx.library.widget.OnePlusCloudy;

/**
 * Created by Dawish on 2017/1/7.
 */

public class ActivityViewPagerIndicator  extends AppCompatActivity {

    private View vline;

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

    private int[] drawableResIds = {R.mipmap.mm1,R.mipmap.mm2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_layout);

        vline = findViewById(R.id.line);
        mViewPager = (ViewPager) findViewById(R.id.anim_view_pager);

        screenWith = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        screeHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight()-dip2px(this, 45);
        //这里之所以是45，请查看布局文件，其中ViewPager以上的节点的高度总和为45

        ViewGroup.LayoutParams lp = vline.getLayoutParams();
        offsetWidth = lp.width = screenWith / 2;
        vline.setLayoutParams(lp);
        vline.setTag("0");

        mViewPager.setOnPageChangeListener(pageChangedListener);
        mViewPager.setAdapter(new ViewPagerAdapter());


    }
    private ViewPager.OnPageChangeListener pageChangedListener = new ViewPager.OnPageChangeListener() {

        private boolean isAnim = false;
        private int pos = 0;

        @Override
        public void onPageSelected(int position)
        {
            Log.e("ViewPager", "position===>"+position);
            vline.setTranslationX(position*offsetWidth);
            pos = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.d("ViewPager", "arg0="+arg0+"  arg1="+arg1+"   arg2="+arg2);
            if(isAnim && arg1!=0)
            {
                vline.setTranslationX(offsetWidth*arg1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
            Log.i("ViewPager", "=====>arg0="+arg0);
            if(arg0==1) //开始状态
            {
                isAnim  = true;
            }
            else if(arg0==2) //分界状态
            {
                isAnim = false;
                vline.setTranslationX(pos*offsetWidth);
            }
            else if(arg0==0) //结束状态
            {
                vline.setTranslationX(pos*offsetWidth);
            }
        }

    };
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

    public void doSwicth(View v) {
        switch (v.getId())
        {
            case R.id.fade_anim_left:
            {
                mViewPager.setCurrentItem(0,true);
            }
            break;
            case R.id.fade_anim_right:
                mViewPager.setCurrentItem(1,true);
                break;

            default:
                break;
        }
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
