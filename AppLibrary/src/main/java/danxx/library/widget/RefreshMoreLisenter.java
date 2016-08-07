package danxx.library.widget;

/**
 * 下拉刷新和上拉加载更多的回调接口
 * -------------------------------
 * @Description: RefreshMoreLisenter
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public interface RefreshMoreLisenter {
    /**下拉刷新**/
    void onRefresh();
    /**上拉加载更多**/
    void onLoadMore();
    /**重度上拉，一般是做跳转到新页面**/
    void onLongPullUp();
}
