package danxx.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 万能的下拉刷新和上拉加载更多控件
 * -------------------------------
 * @Description: DXPullRefreshMoreView
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public class DXPullRefreshMoreView extends LinearLayout {
    private static final String TAG = DXPullRefreshMoreView.class.getSimpleName();

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



    public DXPullRefreshMoreView(Context context) {
        super(context);
    }

    public DXPullRefreshMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DXPullRefreshMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
