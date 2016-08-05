package danxx.library.widget;

/**
 * 下拉刷新和上拉加载更多的回调接口
 * -------------------------------
 * @Description: RefreshMoreLisenter
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public interface RefreshMoreLisenter {
    /****/
    void onRefresh();
    /****/
    void onLoadMore();
}
