package danxx.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 下拉刷新和上拉加载更多自定义控件的功能定义接口
 * -------------------------------
 * @Description: IPullToRefreshMore
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public interface IPullToRefreshMore {

    /**init初始化**/
    void init(Context context, AttributeSet attrs, int defStyleAttr);

    /**添加刷新View**/
    void addHeaderRefreshView();

    /**添加加载更多View**/
    void addFooterRefreshView();

    /**测量子控件大小**/
    void measureView(View child);

    /**是否可以下拉或者上拉**/
    boolean canScroll();

//    /**获取当然的刷新情况**/
//    void getRefreshStatus();
//
//    /**获取当前是下拉还是上拉**/
//    void getPullStatus();

    /**下拉手指还在移动没有抬起来,此过程中需要改变箭头的方向**/
    void pullHeaderToRefresh();

    /**上拉手指还在移动没有抬起来，此过程中需要改变箭头的方向**/
    void pullFooterToRefresh();

    /**在pullHeaderToRefresh动态改变HeaderView的TopMargin值**/
    void changeHeaderViewTopMargin(int margin);

    /**
     * 在刷新中或者刷新完成后设置HeaderView的TopMargin值，
     * 上拉实现加载更多的动画也是设置setHeaderViewTopMargin的值，
     * 这样就会使整个ViewGroup往上升，这样就可以显示出FooterView了，
     */
    void setHeaderViewTopMargin(int margin);

    /**设置开始下拉刷新**/
    void setHeaderRefreshing();

    /**下拉刷新完成，提供给别的类外调**/
    void onHeaderRefreshFinish();

    /**设置开始上拉加载**/
    void setFooterRefreshing();

    /**上拉加载完成，提供给别的类外调**/
    void onFootrRefreshFinish();

    /**获取当前header view 的topMargin**/
    int getHeaderTopMargin();

    /**设置会点监听器**/
    void setRefreshListener(RefreshMoreLisenter refreshListener);
}
