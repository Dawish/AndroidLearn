package danxx.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import danxx.library.R;

/**
 * 万能的下拉刷新和上拉加载更多控件
 * -------------------------------
 * @Description: DXPullRefreshMoreView
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public class DXPullRefreshMoreView extends LinearLayout implements IPullToRefreshMore {

    private static final String TAG = DXPullRefreshMoreView.class.getSimpleName();

    /**最小移动距离，用于判断是否在下拉或者上拉，设置为0则touch事件的判断会过于频繁**/
    private final static float MIN_MOVE_DISTANCE = 8.0f;

    enum RefreshStatus{
        PULL_TO_REFRESH,    // 从没刷新拖动到刷新
        RELEASE_TO_REFRESH, // 本来就在刷新中，但还在拖动
        REFRESHING,         // 刷新中
        NORMAL,             // 当前无操作
    }
    enum PullStatus{
        PULL_UP_STATE,     //下拉刷新
        PULL_DOWN_STATE,   //上拉加载更多
    }

    /**刷新情况的初始状态**/
    private RefreshStatus refreshStatus = RefreshStatus.NORMAL;
    /**拖动方向的初始状态**/
    private PullStatus pullStatus = PullStatus.PULL_DOWN_STATE;
    private Context mContext;

    /** pull to refresh View **/
    private View headerView;
    /** pull to loadmore View **/
    private View footerView;

    /** header view height **/
    private int mHeaderViewHeight;
    /** footer view height **/
    private int mFooterViewHeight;

    /*** header view image*/
    private ImageView mHeaderImageView;
    /*** footer view image*/
    private ImageView mFooterImageView;

    /*** header tip text*/
    private TextView mHeaderTextView;
    /*** footer tip text*/
    private TextView mFooterTextView;

    /*** header progress bar*/
    private ProgressBar mHeaderProgressBar;
    /*** footer progress bar*/
    private ProgressBar mFooterProgressBar;

    /*** 变为向下的箭头,改变箭头方向*/
    private RotateAnimation mAnimation;
    /*** 变为向上的箭头*/
    private RotateAnimation mReverseAnimation;

    /**记录手指滑动之前按下时的Y坐标**/
    private int downY;

    /**回调监听器**/
    private RefreshMoreLisenter refreshMoreLisenter;

    /**Constructor & Init Method.**/
    public DXPullRefreshMoreView(Context context) {
        this(context, null);
    }

    public DXPullRefreshMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DXPullRefreshMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * init初始化
     **/
    @Override
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        mAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(250);
        mAnimation.setFillAfter(true);

        mReverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseAnimation.setInterpolator(new LinearInterpolator());
        mReverseAnimation.setDuration(200);
        mReverseAnimation.setFillAfter(true);
        /**在创建的时候就添加headerView，保证在LinearLayout的顶部**/
        addHeaderRefreshView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /**在解析完所有的XML文件后添加footerView，保证在LinearLayout的最底部**/
        addFooterRefreshView();
    }

    /**
     * 添加刷新View
     **/
    @Override
    public void addHeaderRefreshView() {
        headerView = LayoutInflater.from(mContext).inflate(R.layout.header_refresh_view, this, false);
        mHeaderImageView = (ImageView) headerView.findViewById(R.id.mHeaderImageView);
        mHeaderProgressBar = (ProgressBar) headerView.findViewById(R.id.mHeaderProgressBar);
        mHeaderTextView = (TextView) headerView.findViewById(R.id.mHeaderTextView);
        /**设置和测量headerView的大小**/
        measureView(headerView);
        /**获取headerView的真实高度**/
        mHeaderViewHeight = headerView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        /**设置topMargin的值为负的header View高度,即将其隐藏在最上方**/
        params.topMargin = -(mHeaderViewHeight);
        addView(headerView, params);
    }

    /**
     * 添加加载更多View
     **/
    @Override
    public void addFooterRefreshView() {
        footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_refresh_view, this, false);
        mFooterImageView = (ImageView) footerView.findViewById(R.id.mFooterImageView);
        mFooterProgressBar = (ProgressBar) footerView.findViewById(R.id.mHeaderProgressBar);
        mFooterTextView = (TextView) footerView.findViewById(R.id.mFooterTextView);
        measureView(footerView);
        mFooterViewHeight = footerView.getMeasuredHeight();
        /**footerView不用设置bottomMatgin值，因为中间的ContentView的高度是MATCH_PARENT，会把footerView挤下来**/
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
        addView(footerView, params);
        /**最后检测最后的子View数量是否符合要求**/
        int count = getChildCount();
        if (count < 3) {
            throw new IllegalArgumentException(
                    "this layout must contain 3 child views!");
        }
    }

    /**
     * 测量子控件大小
     *
     * @param child
     **/
    @Override
    public void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if(lp == null){
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        /**用来计算一个合适子视图的宽度大小并返回最后的宽度值**/
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 事件分发拦截
     * onInterceptTouchEvent()用于处理事件并改变事件的传递方向。
     * 返回值为false时事件会传递给子控件的onInterceptTouchEvent()；
     * 返回值为true时事件会传递给当前控件的onTouchEvent()，而不在传递给子控件，这就是所谓的Intercept(截断)。
     * @param ev
     * @return
     * -----------------------------示意图------------------------------------
     *
     *      ** -----------> rawY = M1， 手指按下ACTION_DOWN， downY = M1
     *      * 下拉距离pullY = (rawY - downY) = (M1 - M1) = 0
     *      *
     *      *
     *      *
     *      *  rawY = M2， 向下滑动ACTION_MOVE, downY
     *      **  （downY不变是因为只会在第一次按下的时候才会记录downY的值）
     *      *  pullY = (rawY - downY） = (M2 - M1)
     *      *
     *      *
     *      *
     *      ** rawY = M3，向下滑动ACTION_MOVE, downY不变
     *      * pullY = (rawY - downY） = (M3 - M1)
     *      *
     *      *
     *      V * -----------> rawY = M4，停止滑动ACTION_MOVE, downY不变
     *        pullY = (rawY - downY） = (M4 - M1)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        /**每次手指按下时获取相对屏幕左上角的Y坐标值，一般都是负数**/
        int rawY = (int) ev.getRawY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**记录手指按下的时候相对于屏幕左上角的Y轴坐标**/
                downY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                int pullY = rawY - downY;
                /**滑动的距离满足下拉条件就返回true，就回去调用当前控件的onTouchEvent方法来处理事件**/
                if (pullY>MIN_MOVE_DISTANCE && canScroll()){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return false;
    }

    /**
     * 处理手势事件
     * onTouchEvent() 用于处理事件，返回值决定当前控件是否消费（consume）了这个事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 是否可以下拉或者上拉
     **/
    @Override
    public boolean canScroll() {



        return canScroll(this);
    }

    /**
     * 子类重写此方法可以兼容其它的子控件来支持刷新和加载
     * @param refreshMoreView
     * @return
     */
    protected boolean canScroll(DXPullRefreshMoreView refreshMoreView){
        return false;
    }

    /**
     * 下拉手指还在移动没有抬起来,此过程中需要改变箭头的方向
     **/
    @Override
    public void pullHeaderToRefresh() {

    }

    /**
     * 上拉手指还在移动没有抬起来，此过程中需要改变箭头的方向
     **/
    @Override
    public void pullFooterToRefresh() {

    }

    /**
     * 在pullHeaderToRefresh动态改变HeaderView的TopMargin值
     *
     * @param margin
     **/
    @Override
    public void changeHeaderViewTopMargin(int margin) {

    }

    /**
     * 在刷新中或者刷新完成后设置HeaderView的TopMargin值，
     * 上拉实现加载更多的动画也是设置setHeaderViewTopMargin的值，
     * 这样就会使整个ViewGroup往上升，这样就可以显示出FooterView了，
     *
     * @param margin
     */
    @Override
    public void setHeaderViewTopMargin(int margin) {

    }

    /**
     * 设置开始下拉刷新
     **/
    @Override
    public void setHeaderRefreshing() {

    }

    /**
     * 下拉刷新完成，提供给别的类外调
     **/
    @Override
    public void onHeaderRefreshFinish() {

    }

    /**
     * 设置开始上拉加载
     **/
    @Override
    public void setFooterRefreshing() {

    }

    /**
     * 上拉加载完成，提供给别的类外调
     **/
    @Override
    public void onFootrRefreshFinish() {

    }

    /**
     * 获取当前header view 的topMargin
     **/
    @Override
    public int getHeaderTopMargin() {
        return 0;
    }

    /**
     * 设置刷新监听器
     *
     * @param refreshListener
     **/
    @Override
    public void setRefreshListener(RefreshMoreLisenter refreshListener) {

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    protected int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
