package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Description:
 * @Author: Danxingxi
 * @CreateDate: 2016/12/14 19:56
 */
public class ThreeDViewContainer extends ViewGroup{

    /**当前绘制的View**/
    private int currentItemIndex = 1;
    /**控件四周的padding**/
    private int padding = 30;
    /**子View顶边个底边的距离**/
    private int edge = 28;

    public ThreeDViewContainer(Context context) {
        super(context);
        init(context);
    }

    public ThreeDViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThreeDViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //可以改变子view的绘制顺序
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * 先测量整个Viewgroup的大小
         */
        setMeasuredDimension(sizeWidth, sizeHeight);

        int childCount = getChildCount();

        /**由于每一个子View的宽高都是一样的所以就一起计算每一个View的宽高*/
        int childWidth = getMeasuredWidth() - padding*2;
        int childHeight = getMeasuredHeight() - padding*2 - edge*2;

        int childWidthMeasureSpec = 0;
        int childHeightMeasureSpec = 0;

        // 循环测量每一个View
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            // 系统自动测量子View:
//            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            /** 以一个精确值来测量子View的宽度 */
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        // 循环测量每一个View
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //四个方向的margin值
            int measureL = 0, measurelT = 0, measurelR = 0, measurelB = 0;
            switch (i){
                case 0:
                    measureL = padding;
                    measurelT = padding;
                    measurelB = childView.getMeasuredHeight() + padding;
                    measurelR = childView.getMeasuredWidth() + padding;
                    childView.layout(measureL, measurelT, measurelR, measurelB);
                    break;
                case 1:
                    measureL = padding;
                    measurelT = padding + edge;
                    measurelB = childView.getMeasuredHeight() + padding + edge;
                    measurelR = childView.getMeasuredWidth() + padding;
                    childView.layout(measureL, measurelT, measurelR, measurelB);
                    break;
                case 2:
                    measureL = padding;
                    measurelT = padding + edge*2;
                    measurelB = childView.getMeasuredHeight() + padding + edge*2;
                    measurelR = childView.getMeasuredWidth() + padding;
                    childView.layout(measureL, measurelT, measurelR, measurelB);
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 获取子控件dispatchDraw的次序
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (currentItemIndex < 0) {
            return i;
        }
        if (i < (childCount - 1)) {
            if (currentItemIndex == i)
                i = childCount - 1;
        } else {
            if (currentItemIndex < childCount)
                i = currentItemIndex;
        }
        return i;
    }

//    protected int sp2px(float spValue) {
//        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (spValue * fontScale + 0.5f);
//    }
//
//    protected int dp2px(float dp) {
//        final float scale = mContext.getResources().getDisplayMetrics().density;
//        return (int) (dp * scale + 0.5f);
//    }
//
//    protected float dp2pxf(float dp) {
//        final float scale = mContext.getResources().getDisplayMetrics().density;
//        return (dp * scale + 0.5f);
//    }
}
