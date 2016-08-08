package danxx.library.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
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
    private final static int SCROLL_DURATION = 400;

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

    /**header刷新情况的初始状态**/
    private RefreshStatus headerRefreshStatus = RefreshStatus.NORMAL;
    /**footer刷新情况的初始状态**/
    private RefreshStatus footerRefreshStatus = RefreshStatus.NORMAL;
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

    /**辅助滚动的scroller**/
    private Scroller mScroller;

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
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(mContext);
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
        mFooterProgressBar = (ProgressBar) footerView.findViewById(R.id.mFooterProgressBar);
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
     *
     * @param ev
     * @return -----------------------------示意图------------------------------------
     * <p/>
     * ** -----------> rawY = M1， 手指按下ACTION_DOWN， downY = M1
     * * 下拉距离pullY = (rawY - downY) = (M1 - M1) = 0
     * *
     * *
     * *
     * *  rawY = M2， 向下滑动ACTION_MOVE, downY
     * **  （downY不变是因为只会在第一次按下的时候才会记录downY的值）
     * *  pullY = (rawY - downY） = (M2 - M1)
     * *
     * *
     * *
     * ** rawY = M3，向下滑动ACTION_MOVE, downY不变
     * * pullY = (rawY - downY） = (M3 - M1)
     * *
     * *
     * V * -----------> rawY = M4，停止滑动ACTION_MOVE, downY不变
     * pullY = (rawY - downY） = (M4 - M1)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("danxx" ,"onInterceptTouchEvent");
//        return super.onInterceptTouchEvent(ev);
        /**每次手指按下时获取相对屏幕左上角的Y坐标值，一般都是负数**/
        int rawY = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**记录手指按下的时候相对于屏幕左上角的Y轴坐标**/
                downY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                /**pullY>0说明是下拉，pullY<0说明是上拉 */
                int pullY = rawY - downY;
                /**滑动的距离满足下拉条件就返回true，就回去调用当前控件的onTouchEvent方法来处理事件**/
                if ( Math.abs(pullY) > dp2px(MIN_MOVE_DISTANCE)  && canScroll(pullY) ) {
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
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(event);
        /**每次手指按下时获取相对屏幕左上角的Y坐标值，一般都是负数**/
        Log.d("danxx" ,"onTouchEvent22222");
        int rawY = (int) ev.getRawY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**记录手指按下的时候相对于屏幕左上角的Y轴坐标**/
                downY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("danxx" ,"ACTION_MOVE");
                /**pullY>0说明是下拉，pullY<0说明是上拉 */
                int pullY = rawY - downY;
                if(pullStatus == PullStatus.PULL_DOWN_STATE){
                    pullHeaderToRefresh(pullY);
                }else if(pullStatus == PullStatus.PULL_UP_STATE){
                    pullFooterToRefresh(pullY);
                }
                downY = rawY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                /**手指滑动松开后判断是否符合刷新条件**/
                int topMargin = getHeaderTopMargin();
                if(pullStatus == PullStatus.PULL_DOWN_STATE){
                   if(topMargin > 0 ){
                       setHeaderRefreshing();
                   }else{
                       /**不满足刷新条件，隐藏header**/
                       setHeaderViewTopMargin(-mHeaderViewHeight);
                   }
                }else if(pullStatus == PullStatus.PULL_UP_STATE){
                    if(Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight){
                        setFooterRefreshing();
                    }else{
                        setHeaderViewTopMargin(-mHeaderViewHeight);
                    }
                }
                Log.d("danxx" ,"ACTION_UP_CANCEL");
                break;

        }
        return false;
    }

    /**
     * 是否可以下拉或者上拉
     * @param pullY  下拉的距离值，
     * pullY > 0 说明是下拉
     * pullY < 0 说明是上拉
     **/
    @Override
    public boolean canScroll(int pullY) {
        Log.d("danxx" ,"canScroll");
        if(headerRefreshStatus == RefreshStatus.REFRESHING || footerRefreshStatus == RefreshStatus.REFRESHING){
            return false;
        }
        int childCount = getChildCount();
        if(childCount>1){
            View contentView = getChildAt(1);
           if(pullY > 0){  //下拉
               if(contentView instanceof ScrollView){
                   Log.d("danxx" ,"ScrollView");
                   if(((ScrollView)contentView).getScrollY() == 0){  //scrollView滚动到顶部才可以下拉
                       mHeaderImageView.setVisibility(VISIBLE);
                       mHeaderTextView.setVisibility(VISIBLE);
                       pullStatus = PullStatus.PULL_DOWN_STATE;
                       return true;
                   }else{
                       return false;
                   }
               }else if(contentView instanceof ListView){
                   int top = ((ListView) contentView).getChildAt(0).getTop();
                   int pad = ((ListView) contentView).getListPaddingTop();
                   if ((Math.abs(top - pad)) < 3
                           && ((ListView) contentView).getFirstVisiblePosition() == 0) {
                       mHeaderImageView.setVisibility(VISIBLE);
                       mHeaderTextView.setVisibility(VISIBLE);
                       pullStatus = PullStatus.PULL_DOWN_STATE;
                       return true;
                   } else {
                       return false;
                   }

               }else if(contentView instanceof RecyclerView){

               }else if(contentView instanceof LinearLayout){
                   mHeaderImageView.setVisibility(VISIBLE);
                   mHeaderTextView.setVisibility(VISIBLE);
                   pullStatus = PullStatus.PULL_DOWN_STATE;
                   return true;
               }
           }else {  //上拉
               if(contentView instanceof ScrollView){
                   View child = ((ScrollView)contentView).getChildAt(0);
                   if(child.getMeasuredHeight() <= getHeight() + ((ScrollView)contentView).getScrollY()){
                        mFooterImageView.setVisibility(VISIBLE);
                        mFooterTextView.setVisibility(VISIBLE);
                        pullStatus = PullStatus.PULL_UP_STATE;
                       return true;
                   }else{
                       return false;
                   }
               }else if(contentView instanceof ListView){
                    View lastChild = ((ListView)contentView).getChildAt(((ListView)contentView).getChildCount() - 1);
                    if(lastChild.getBottom() <= getHeight() &&
                            ((ListView)contentView).getLastVisiblePosition() == ((ListView)contentView).getCount() - 1){
                        mFooterImageView.setVisibility(VISIBLE);
                        mFooterTextView.setVisibility(VISIBLE);
                        pullStatus = PullStatus.PULL_UP_STATE;
                        return true;
                    }else{
                        return false;
                    }
               }else if(contentView instanceof RecyclerView){

               }else if(contentView instanceof LinearLayout){
                   mFooterImageView.setVisibility(VISIBLE);
                   mFooterTextView.setVisibility(VISIBLE);
                   pullStatus = PullStatus.PULL_UP_STATE;
                   return true;
               }
           }
        }

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
    public void pullHeaderToRefresh(int pullY) {
        Log.d("danxx" ,"pullHeaderToRefresh");
        int newTopMargin = changeHeaderViewTopMargin(pullY);
        // 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
        if(newTopMargin >= 0 && headerRefreshStatus != RefreshStatus.RELEASE_TO_REFRESH){
            mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mAnimation);
            headerRefreshStatus = RefreshStatus.RELEASE_TO_REFRESH;
        } else if(newTopMargin < 0 && newTopMargin > - mHeaderViewHeight){ // 拖动时没有释放
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mAnimation);
            mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
            headerRefreshStatus = RefreshStatus.PULL_TO_REFRESH;
        }


    }

    /**
     * 上拉手指还在移动没有抬起来，此过程中需要改变箭头的方向
     * ooter 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
     * 高度是一样，都是通过修改header view的topmargin的值来达到
     **/
    @Override
    public void pullFooterToRefresh(int pullY) {
        mFooterImageView.setVisibility(VISIBLE);
        int newTopMargin = changeHeaderViewTopMargin(pullY);
        // 如果header view topMargin 的绝对值大于或等于header + footer 的高度
        // 说明footer view 完全显示出来了，修改footer view 的提示状态
        if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
                && footerRefreshStatus != RefreshStatus.RELEASE_TO_REFRESH) {
            mFooterTextView
                    .setText(R.string.pull_to_refresh_footer_release_label);
            mFooterImageView.clearAnimation();
            mFooterImageView.startAnimation(mAnimation);
            footerRefreshStatus = RefreshStatus.RELEASE_TO_REFRESH;
        } else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
            mFooterImageView.clearAnimation();
            mFooterImageView.startAnimation(mAnimation);
            mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
            footerRefreshStatus = RefreshStatus.PULL_TO_REFRESH;
        }
    }

    /**
     * 在pullHeaderToRefresh动态改变HeaderView的TopMargin值
     *
     * @param pullY
     **/
    @Override
    public int changeHeaderViewTopMargin(int pullY) {
        Log.d("danxx", "changeHeaderViewTopMargin---pullY-->"+pullY);
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        /**对margin改变只是拉动距离的0.5倍，这样给人一种用力拉得感觉*/
        float newTopMargin = params.topMargin +  pullY*0.3f;

        /**对于在手指按下先下拉后又上拉的情况，避免在下拉刷新时出现footerView**/
        if(pullStatus == PullStatus.PULL_DOWN_STATE && pullY<0 ){
            if(newTopMargin < -mHeaderViewHeight){
                newTopMargin = -mHeaderViewHeight;
            }
        }

        /**上拉又下拉屏蔽出现headerView**/
        if(pullStatus == PullStatus.PULL_UP_STATE && pullY>0 ){
            if(params.topMargin >= -mHeaderViewHeight - dp2px(4)){
                return params.topMargin;
            }
        }

        params.topMargin = (int) newTopMargin;
        headerView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
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
        Log.d("danxx" ,"setHeaderViewTopMargin-->"+margin);
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        params.topMargin = margin;
        headerView.setLayoutParams(params);
        invalidate();
    }

    /**
     * 设置开始下拉刷新
     **/
    @Override
    public void setHeaderRefreshing() {
        headerRefreshStatus = RefreshStatus.REFRESHING;
//        setHeaderViewTopMargin(0);
        mScroller.startScroll(0, mScroller.getStartY(), 0, 0, SCROLL_DURATION);
        invalidate();
        mHeaderImageView.setVisibility(View.GONE);
        mHeaderImageView.clearAnimation();
        mHeaderProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
        if (refreshMoreLisenter != null) {
            refreshMoreLisenter.onRefresh();
        }
    }

    /**
     * 下拉刷新完成，提供给别的类外调
     **/
    @Override
    public void onHeaderRefreshFinish() {
//        setHeaderViewTopMargin(-mHeaderViewHeight);

        mHeaderImageView.setVisibility(View.GONE);
        mHeaderProgressBar.setVisibility(View.GONE);
        mScroller.startScroll(0, getHeaderTopMargin(), 0, -mHeaderViewHeight, SCROLL_DURATION);
        invalidate();
        // mHeaderUpdateTextView.setText("");
        headerRefreshStatus = RefreshStatus.NORMAL;
    }

    /**
     * 设置开始上拉加载
     **/
    @Override
    public void setFooterRefreshing() {
        Log.d("danxx" ,"setFooterRefreshing");
        footerRefreshStatus = RefreshStatus.REFRESHING;
        int top = mHeaderViewHeight + mFooterViewHeight;
        setHeaderViewTopMargin(-top);
//        mScroller.startScroll(0, getHeaderTopMargin(), 0, -top, SCROLL_DURATION);
//        invalidate();
        mFooterImageView.setVisibility(View.GONE);
        mFooterImageView.clearAnimation();
        if(mFooterProgressBar!=null)
        mFooterProgressBar.setVisibility(View.VISIBLE);
        mFooterTextView.setText(R.string.pull_to_refresh_footer_refreshing_label);
        if (refreshMoreLisenter != null) {
            refreshMoreLisenter.onLoadMore();
        }
    }

    /**
     * 上拉加载完成，提供给别的类外调
     **/
    @Override
    public void onFootrRefreshFinish() {
        setHeaderViewTopMargin(-mHeaderViewHeight);
//        mScroller.startScroll(0, mScroller.getCurrY(), 0, -mHeaderViewHeight, SCROLL_DURATION);
//        invalidate();
        mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
        if(mFooterProgressBar != null)
        mFooterProgressBar.setVisibility(View.GONE);
        // mHeaderUpdateTextView.setText("");
        footerRefreshStatus = RefreshStatus.NORMAL;
    }

    /**
     * 获取当前header view 的topMargin
     **/
    @Override
    public int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * 设置刷新监听器
     *
     * @param refreshListener
     **/
    @Override
    public void setRefreshListener(RefreshMoreLisenter refreshListener) {
        this.refreshMoreLisenter = refreshListener;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//scroll 动作还未结束
            Log.i(TAG, "----->computeScroll()");
            int i = this.mScroller.getCurrY();
            LayoutParams lp = (LayoutParams) this.headerView
                    .getLayoutParams();
            int k = Math.max(i, -mHeaderViewHeight);
//            if(footerRefreshStatus == RefreshStatus.REFRESHING){
//                lp.topMargin = -(mHeaderViewHeight+mFooterViewHeight);
//            }else{
                lp.topMargin = k;
//            }
            this.headerView.setLayoutParams(lp);
            postInvalidate();
        }

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

    protected float dp2pxf(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
    }
}
