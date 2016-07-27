package danxx.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Danxx on 2016/7/27.
 * 自定义Viewpager
 */
public class CustomViewPager extends ViewGroup {

    private int mLastX;

    public CustomViewPager(Context context) {
        super(context);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count  = getChildCount();
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.layout(i * getWidth(), t, (i+1) * getWidth(), b);
        }
    }

    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int oldScrollX = getScrollX();//原来的偏移量
                int preScrollX = oldScrollX + dx;//本次滑动后形成的偏移量
                if(preScrollX > (getChildCount() - 1) * getWidth()){
                    preScrollX = (getChildCount() - 1) * getWidth();
                }
                if(preScrollX < 0){
                    preScrollX = 0;
                }
                scrollTo(preScrollX,getScrollY());
                mLastX = x;
                break;
        }
        return true;
    }
}
