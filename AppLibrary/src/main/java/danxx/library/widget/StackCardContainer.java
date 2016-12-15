package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import danxx.library.tools.FocusAnimUtils;

/**
 * @Description:
 * @Author: Danxingxi
 * @CreateDate: 2016/12/14 19:56
 */
public class StackCardContainer extends ViewGroup{
    private static final String TAG = "ThreeDViewContainer";
    /**当前绘制的View，中间的默认显示在最上层**/
    private int currentItemIndex = 1;
    /**控件四周的padding**/
    private int padding = 46;
    /**子View顶边个底边的距离**/
    private int edge = 60;
    /**最小移动距离，用于判断是否在下拉，设置为0则touch事件的判断会过于频繁。具体值可以根据自己来设定**/
    private final static float MIN_MOVE_DISTANCE = 8.0f;
    /**滑动距离大于这个值才翻页**/
    private int MIN_CHANGE_DISTANCE = 40;
    /**View放大的动画时间，在此动画期间无法翻页**/
    private long animDuration = 300;

    private final static int MSG_UP = 1;
    private final static int MSG_DOWN = 2;

    public StackCardContainer(Context context) {
        super(context);
        init(context);
    }

    public StackCardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StackCardContainer(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        /**首次进来的时候放大中间的View**/
        FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
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

        /**卡片的间距为控件实际高度的1/8**/
        edge = sizeHeight/8;
        /**pading值为控件实际高度的1/12**/
        padding = sizeHeight/16;

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

    private int lastY;

    /**
     * 事件分发拦截
     * onInterceptTouchEvent()用于处理事件并改变事件的传递方向。
     * 返回值为false时事件会传递给子控件的onInterceptTouchEvent()；
     * 返回值为true时事件会传递给当前控件的onTouchEvent()，而不在传递给子控件，这就是所谓的Intercept(截断)。
     * @param e
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
//        return super.onInterceptTouchEvent(ev);
        // layout截取touch事件
        int action = e.getAction();
        int y = (int) e.getRawY();
        Log.d("danxx" ,"手指在操作，y--->"+y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                Log.d("danxx" ,"手指按下，lastY--->"+lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                // y移动坐标
                int m = y - lastY;
                Log.d("danxx" ,"手指移动，m--->"+m);
                // 记录下此刻y坐标
                this.lastY = y;
                /**如果下拉距离足够并且 当前 情况 可以下拉就返回true，这样事件会传递给当前控件的onTouchEvent()**/
                if (m > MIN_MOVE_DISTANCE) {
                    Log.i("danxx", "to onTouchEvent");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /**
     * 事件分发
     * onTouchEvent() 用于处理事件，返回值决定当前控件是否消费（consume）了这个事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        /**以屏幕左上角为坐标原点计算的Y轴坐标**/
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "MotionEvent.ACTION_DOWN");
                // 手指按下时记录下y坐标
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "MotionEvent.ACTION_MOVE");
                // 手指向下滑动时 y坐标 = 屏幕左上角为坐标原点计算的Y轴坐标 - 手指滑动的Y轴坐标
                int m = y - lastY;
                if(m>0 && m>MIN_CHANGE_DISTANCE){  //手指向下滑动
                    changeHandler.removeMessages(MSG_UP);
                    changeHandler.sendEmptyMessageDelayed(MSG_UP, animDuration);
//                    upPage();

                }else if(m< 0&& Math.abs(m)>MIN_CHANGE_DISTANCE){ //手指向上滑动
                    changeHandler.removeMessages(MSG_DOWN);
                    changeHandler.sendEmptyMessageDelayed(MSG_DOWN, animDuration);
//                    downPage();
                }

                // 记录下此刻y坐标
                this.lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "MotionEvent.ACTION_UP");
                break;
        }
        return true;
    }

    final Handler changeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            if(MSG_DOWN == what){
                downPage();
                return true;
            }else if(MSG_UP == what){
                upPage();
                return true;
            }

            return false;
        }
    });

    /**
     * 显示下面的一页
     * 翻页成功返回true，否则false
     */
    private boolean downPage(){
        if(1 == currentItemIndex){
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), false, 1.0f, animDuration);
            // 重绘，改变堆叠顺序
            currentItemIndex = 2;
            postInvalidate();
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
            return true;
        }else if(0 == currentItemIndex){
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), false, 1.0f, animDuration);
            // 重绘，改变堆叠顺序
            currentItemIndex = 1;
            postInvalidate();
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
            return true;
        }else if(2 == currentItemIndex){
            return false;
        }
        return false;
    }

    /**
     * 显示上面的一页
     * 翻页成功返回true，否则false
     */
    private boolean upPage(){
        if(1 == currentItemIndex){
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), false, 1.0f, animDuration);
            // 重绘，改变堆叠顺序
            currentItemIndex = 0;
            postInvalidate();
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
            return true;
        }else if(0 == currentItemIndex){
            return false;
        }else if(2 == currentItemIndex){
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), false, 1.0f, animDuration);
            currentItemIndex = 1;
            postInvalidate();
            FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
            return true;
        }
        return false;
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
}
