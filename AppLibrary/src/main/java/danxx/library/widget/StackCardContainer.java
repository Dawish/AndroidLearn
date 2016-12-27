package danxx.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import danxx.library.R;
import danxx.library.tools.FocusAnimUtils;

/**
 * @Description: 自定义堆叠卡片容器
 * @Author: Danxingxi
 * @CreateDate: 2016/12/14 19:56
 */
public class StackCardContainer extends ViewGroup implements View.OnTouchListener {
    private static final String TAG = "StackCardContainer";

    /**一些默认值**/
    private static final int DEFAULT_PADDING = 46;
    private static final int DEFAULT_EDGE = 60;
    /**最小移动距离，用于判断是否在滑动，设置为0则touch事件的判断会过于频繁。具体值可以根据自己来设定**/
    private final static int DEFAULT_MIN_CHANGE_DISTANCE = 20;
    private final static int DEFAULT_ANIM_DURATION = 300;
    /**默认竖直方向**/
    private static final int DEFAULT_SHAPE_TYPE = ShapeType.VERTICAL.ordinal();
    /**最小移动距离，用于判断是否在滑动，设置为0则touch事件的判断会过于频繁。具体值可以根据自己来设定**/
    private final static float MIN_MOVE_DISTANCE = 8.0f;

    /**是横向还是竖向**/
    public enum ShapeType {
        VERTICAL,
        HORIZONTAL
    }

    /**当前绘制的View，中间的默认显示在最上层**/
    private int currentItemIndex = 1;
    /**控件四周的padding**/
    private int padding ;
    /**子View顶边个底边的距离**/
    private int edge;
    /**滑动距离大于这个值才翻页**/
    private int changeDistance;
    /**View放大的动画时间，在此动画期间无法翻页**/
    private long animDuration;
    private int mShapeType;
    private final static int MSG_UP = 1;
    private final static int MSG_DOWN = 2;

    private Context mContext;
    private OnItemViewClickListener onItemViewClickListener;

    public StackCardContainer(Context context) {
        this(context, null);
    }

    public StackCardContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackCardContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mContext = context;
        //可以改变子view的绘制顺序
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);

        // Load the styled attributes and set their properties
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StackCardContainer, defStyleAttr, 0);
        //取值
        padding = typedArray.getInteger(R.styleable.StackCardContainer_scc_padding, dp2px(DEFAULT_PADDING));
        edge = typedArray.getInteger(R.styleable.StackCardContainer_scc_edge, dp2px(DEFAULT_EDGE));
        changeDistance = typedArray.getInteger(R.styleable.StackCardContainer_scc_min_change_distance,dp2px(DEFAULT_MIN_CHANGE_DISTANCE));
        animDuration = typedArray.getInteger(R.styleable.StackCardContainer_scc_anim_duration, DEFAULT_ANIM_DURATION);
        mShapeType = typedArray.getInteger(R.styleable.StackCardContainer_scc_type, DEFAULT_SHAPE_TYPE);

        typedArray.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /**首次进来的时候放大中间的View**/
        FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
        int itemCount = getChildCount();
        if(itemCount>0){
            for(int i=0;i<itemCount;i++){
                final int finalI = i;
                getChildAt(i).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemViewClickListener!=null){
                            onItemViewClickListener.onItemViewOnClickListener(v, finalI);
                            if(currentItemIndex != finalI){
                                setCurrentPage(finalI);
                            }
                        }
                    }
                });
            }
        }
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
        int childWidth, childHeight;
        /**由于每一个子View的宽高都是一样的所以就一起计算每一个View的宽高*/
        if(ShapeType.VERTICAL.ordinal() == mShapeType){  //竖向模式
            childWidth = getMeasuredWidth() - padding*2;
            childHeight = getMeasuredHeight() - padding*2 - edge*2;
        }else{   //横向模式
            childWidth = getMeasuredWidth() - padding*2 - edge*2;
            childHeight = getMeasuredHeight() - padding*2;
        }

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

            if(ShapeType.VERTICAL.ordinal() == mShapeType){  //竖向模式
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
            }else{   //横向模式
                switch (i){
                    case 0:
                        measureL = padding;
                        measurelT = padding;
                        measurelB = childView.getMeasuredHeight() + padding;
                        measurelR = childView.getMeasuredWidth() + padding;
                        childView.layout(measureL, measurelT, measurelR, measurelB);
                        break;
                    case 1:
                        measureL = padding + edge;
                        measurelT = padding;
                        measurelB = childView.getMeasuredHeight() + padding;
                        measurelR = childView.getMeasuredWidth() + padding + edge;
                        childView.layout(measureL, measurelT, measurelR, measurelB);
                        break;
                    case 2:
                        measureL = padding + edge*2;
                        measurelT = padding;
                        measurelB = childView.getMeasuredHeight() + padding;
                        measurelR = childView.getMeasuredWidth() + padding + edge*2;
                        childView.layout(measureL, measurelT, measurelR, measurelB);
                        break;
                }
            }


        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int lastY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v!=null)
        Log.d("danxx", "onTouch---->"+v.getId());
        return false;
    }
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
        Log.d("danxx", "onInterceptTouchEvent");
//        return super.onInterceptTouchEvent(e);
        // layout截取touch事件
        int action = e.getAction();
        int y;
        if(ShapeType.VERTICAL.ordinal() ==  mShapeType){
            y = (int) e.getRawY();
        }else{
            y = (int) e.getRawX();
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // y移动坐标
                int m = y - lastY;
                Log.d("danxx" ,"手指按下，currY--->"+y);
                Log.d("danxx" ,"手指按下，lastY--->"+lastY);
                // 记录下此刻y坐标
                this.lastY = y;
                /**如果下拉距离足够并且 当前 情况 可以下拉就返回true，这样事件会传递给当前控件的onTouchEvent()**/
                if (Math.abs(m) > MIN_MOVE_DISTANCE) {
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
        Log.d("danxx", "onTouchEvent");
//        return super.onTouchEvent(event);
        /**以屏幕左上角为坐标原点计算的Y轴坐标**/
        int y;
        if(ShapeType.VERTICAL.ordinal() ==  mShapeType){
            y = (int) event.getRawY();
        }else{
            y = (int) event.getRawX();
        }
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
                if(m>0 && m>changeDistance){  //手指向下滑动
                    changeHandler.removeMessages(MSG_UP);
                    changeHandler.sendEmptyMessageDelayed(MSG_UP, animDuration);
//                    upPage();

                }else if(m< 0&& Math.abs(m)>changeDistance){ //手指向上滑动
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("danxx", "dispatchKeyEvent");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("danxx", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

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

    private void setCurrentPage(int index){
        if(currentItemIndex != index)
        FocusAnimUtils.animItem(getChildAt(currentItemIndex), false, 1.0f, animDuration);
        currentItemIndex = index;
        postInvalidate();
        FocusAnimUtils.animItem(getChildAt(currentItemIndex), true, 1.06f, animDuration);
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

    public interface OnItemViewClickListener{
        void onItemViewOnClickListener(View itemView, int position);
    }
    public void setOnItemViewClickListener(OnItemViewClickListener itemViewClickListener){
        this.onItemViewClickListener = itemViewClickListener;
    }
    /**
     * Paint.setTextSize(float textSize) default unit is px.
     *
     * @param spValue The real size of text
     * @return int - A transplanted sp
     */
    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
