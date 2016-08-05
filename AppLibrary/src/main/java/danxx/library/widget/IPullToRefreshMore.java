package danxx.library.widget;

/**
 * 下拉刷新和上拉加载更多自定义控件的功能定义接口
 * -------------------------------
 * @Description: IPullToRefreshMore
 * @Author: Danxingxi
 * @CreateDate: 2016/8/5 10:12
 */
public interface IPullToRefreshMore {

    /**添加刷新View**/
    void addRefreshView();

    /**添加加载更多View**/
    void addMoreView();

    /**测量子控件大小**/
    void measureView();



}
