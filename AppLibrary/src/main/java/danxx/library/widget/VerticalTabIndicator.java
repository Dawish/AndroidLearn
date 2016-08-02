package danxx.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import danxx.library.R;

/**
 * Created by Danxingxi on 2016/7/31.
 * 垂直的指示器
 */
public class VerticalTabIndicator extends ScrollView implements View.OnFocusChangeListener, View.OnClickListener {

    /**
     * tabItem的默认高度
     */
    private static final int DEFUALT_ITEM_HEIGHT = 100;
    /**
     * tabItem默认选中后文字的大小
     */
    private static final int DEFUALT_TEXT_SELECT_SIZE = 20;
    /**
     * tabItem默认非选中后文字的大小
     */
    private static final int DEFUALT_TEXT_UNSELECT_SIZE = 18;

    private int DEFUALT_SELECT_BG_COLOR = Color.YELLOW;

    private int DEFUALT_UNSELECT_BG_COLOR = Color.TRANSPARENT;

    private int DEFUALT_SELECT_TEXT_COLOR = Color.BLUE;

    private int DEFUALT_UNSELECT_TEXT_COLOR = Color.WHITE;

    /**tab指示器容器**/
    private LinearLayout tabBox;
    private Context mContext;
    /**tabItem数据集合**/
    private SparseArray<String> mData;

    /**tabItem选中后文字的颜色**/
    private int tabTextSelectColor = DEFUALT_SELECT_TEXT_COLOR;
    /**tabItem非选中后文字的颜色**/
    private int tabTextUnselectColor = DEFUALT_UNSELECT_TEXT_COLOR;

    /**tabItem选中后文字的大小**/
    private int tabTextSelectSize = DEFUALT_TEXT_SELECT_SIZE;
    /**tabItem非选中后文字的大小**/
    private int tabTextUnselectSize = DEFUALT_TEXT_UNSELECT_SIZE;


    /**tabItem选中后背景的颜色**/
    private int tabBackGroundSelectColor = DEFUALT_SELECT_BG_COLOR;
    /**tabItem非选中后背景的颜色**/
    private int tabBackGroundUnselectColor = DEFUALT_UNSELECT_BG_COLOR;

    /**tabItem的默认高度**/
    private int tabItemHeight = DEFUALT_ITEM_HEIGHT;

    /**当前没选中的item**/
    private int currentSelectIndex = -1;
    /**
     * tabItem点击监听器
     */
    private TabClickListrner tabClickListrner;
    /**
     * tabItem点击监听器
     */
    private TabSelectListrner tabSelectListrner;
    private TypedArray typedArray;

    public VerticalTabIndicator(Context context) {
        super(context);
        init(context ,null ,0);
    }

    public VerticalTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context ,attrs ,0);
    }

    public VerticalTabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context ,attrs ,defStyleAttr);
    }

    private void init( Context context ,AttributeSet attrs, int defStyleAttr){
        this.mContext = context;
        setVerticalScrollBarEnabled(false);
        setSmoothScrollingEnabled(true);

        if(attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTabIndicator, defStyleAttr, 0);
        }
        if(typedArray != null){
            tabBackGroundSelectColor = typedArray.getColor(R.styleable.VerticalTabIndicator_tabBackGroundSelectColor, DEFUALT_SELECT_BG_COLOR);
            tabBackGroundUnselectColor = typedArray.getColor(R.styleable.VerticalTabIndicator_tabBackGroundUnselectColor, DEFUALT_UNSELECT_BG_COLOR);
            tabTextSelectColor = typedArray.getColor(R.styleable.VerticalTabIndicator_tabTextSelectColor, DEFUALT_SELECT_TEXT_COLOR);
            tabTextUnselectColor = typedArray.getColor(R.styleable.VerticalTabIndicator_tabTextUnselectColor, DEFUALT_UNSELECT_TEXT_COLOR);
            tabTextSelectSize = (int) typedArray.getDimension(R.styleable.VerticalTabIndicator_tabTextSelectSize, sp2px(DEFUALT_TEXT_SELECT_SIZE));
            tabTextUnselectSize = (int) typedArray.getDimension(R.styleable.VerticalTabIndicator_tabTextUnselectSize, sp2px(DEFUALT_TEXT_UNSELECT_SIZE));
            tabItemHeight = (int) typedArray.getDimension(R.styleable.VerticalTabIndicator_tabItemHeight, dp2px(DEFUALT_ITEM_HEIGHT));

            typedArray.recycle();
        }

        tabBox = new LinearLayout(context);
        tabBox.setOrientation(LinearLayout.VERTICAL);
//        tabBox.setPadding(8, 8, 8, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(tabBox, params);
    }
    /**
         * 设置当前哪一个tabitem被选中
         * @param index
         */
    public void setCurrentTabItemSelect(int index){
        if(currentSelectIndex == index){
            TextView setTab = (TextView) tabBox.getChildAt(index);
            setTab.requestFocus();
            return;
        }else{
            TextView setTab = (TextView) tabBox.getChildAt(index);
            setTab.requestFocus();
            onFocusChange(setTab, true);
        }
    }

    /**
     * 设置数据，设置数据后马上添加Item
     * @param data
     */
    public void setData(SparseArray<String> data){
        if(data != null && data.size()>0){
            this.mData = data;
            addTabItems();
        }else{
            new IllegalArgumentException("Argument is null or empty!");
        }
    }

    /**
     * 根据数据添加tabItem
     */
    private void addTabItems(){
        tabBox.removeAllViews();
        for(int i=0 ; i<mData.size() ; i++){
            final TextView tabItem = new TextView(mContext);
            tabItem.setTextColor(DEFUALT_UNSELECT_TEXT_COLOR);
            tabItem.setTextSize(tabTextUnselectSize);
            tabItem.setFocusable(true);
            tabItem.setClickable(true);
            tabItem.setTag(i);
            tabItem.setFocusableInTouchMode(true);
            tabItem.setText(mData.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , tabItemHeight);
            params.setMargins(0, 8 ,0, 0);
            tabItem.setOnFocusChangeListener(this);
            tabItem.setOnClickListener(this);
            tabItem.setGravity(Gravity.CENTER);
            tabBox.addView(tabItem, params);
        }
    }

    public void setTabSelectListrner(TabSelectListrner selectListrner){
        this.tabSelectListrner = selectListrner;
    }
    public void setTabClickListrner(TabClickListrner clickListrner){
        this.tabClickListrner = clickListrner;
    }
    /**
     *  tabItem焦点变化监听
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        Log.d("danxx" ,"当前的position-->"+currentSelectIndex);
        if(hasFocus){
            if(-1 != currentSelectIndex){
                /**在焦点切换之前把当前选中的item切换状态**/
                TextView currentItem = (TextView) tabBox.getChildAt(currentSelectIndex);
                currentItem.setTextSize(tabTextUnselectSize);
                currentItem.setTextColor(tabTextUnselectColor);
                currentItem.setBackgroundColor(tabBackGroundUnselectColor);
            }
            int position = Integer.parseInt(String.valueOf(view.getTag()));
            this.currentSelectIndex = position;

            if(tabSelectListrner!=null)
                tabSelectListrner.onItemSelect(position);
            /**把获得焦点的item切换状态**/
            ((TextView)view).setTextSize(tabTextSelectSize);
            ((TextView)view).setTextColor(tabTextSelectColor);
            ((TextView)view).setBackgroundColor(tabBackGroundSelectColor);
        }else{
            ((TextView)view).setBackgroundColor(tabBackGroundUnselectColor);
        }

    }

    /**
     *  tabItem点击监听
     * @param clickView
     */
    @Override
    public void onClick(View clickView) {
        int position = Integer.parseInt(String.valueOf(clickView.getTag()));

        if(tabClickListrner != null)
            tabClickListrner.onItemClick(position);

        if(currentSelectIndex == position){
            /**如果点击的tabItem就是选中的tabItem就直接返回**/
            return;
        }else{
            TextView currentTab = (TextView) tabBox.getChildAt(position);
            /**利用onFocusChange间接来更新tabItem的选中和非选中状态**/
            onFocusChange(currentTab, false);
            clickView.requestFocus();
            onFocusChange(clickView ,true);
        }

    }

    /**
     *  tabitem被选中后的回调接口
     */
    public interface TabSelectListrner{
        void onItemSelect(int index);
    }

    /**
     *  tabitem被选中后的回调接口
     */
    public interface TabClickListrner{
        void onItemClick(int index);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        /**增加提前滚动距离**/
        int fadingEdge = getVerticalFadingEdgeLength()+200;

        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }

        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }

        int scrollYDelta = 0;

        if (rect.bottom > screenBottom && rect.top > screenTop) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += (rect.top - screenTop);
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += (rect.bottom - screenBottom);
            }

            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.height() > height) {
                // screen size chunk
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (screenTop - rect.top);
            }

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
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
