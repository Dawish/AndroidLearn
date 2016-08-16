package danxx.library.focusview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * @Description:
 * @Author: Danxingxi
 * @CreateDate: 2016/8/16 21:14
 */
public class FocusHorizontalScrollView extends HorizontalScrollView {
    public FocusHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    public FocusHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setSmoothScrollingEnabled(true);
    }

    /*
     * (非 Javadoc) <p>Title: computeScrollDeltaToGetChildRectOnScreen</p>
     * <p>Description: PageView翻页时，某个Item没有显示完整，则进行滚动</p>
     *
     * @param rect
     *
     * @return 返回滚动距离
     *
     * @see
     * android.widget.HorizontalScrollView#computeScrollDeltaToGetChildRectOnScreen
     * (android.graphics.Rect)
     */
//    @Override
//    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
//
//        int scrollXDelta = 0;
//
//        int itemLeft = rect.left;
//        int itemRight = rect.right;
//        int scrollX = getScrollX();
//        int width = getWidth();
//        if ((itemLeft - scrollX) < leftPadding) {// 如果左边越界
//
//			/*
//			 * int x = itemRight - width + rightPadding; if(x < 0) { x = 0; }
//			 * smoothScrollTo(x, 0);
//			 */
//
//            int x = itemRight - width + rightPadding;// 右边的所有控件
//            scrollXDelta = x - scrollX;// 把右边控件以及滚动差值滚动出来
//            if (x < 0) {// 如果右边部分不超出屏幕
//                scrollXDelta = -scrollX;
//            }
//
//        }
//
//        if ((itemRight + rightPadding) > (scrollX + width)) {// 如果右边越界
//			/*
//			 * int pWidth = pageBox.getWidth(); int x = itemLeft - leftPadding;
//			 * if((x+width) > pWidth) { x = pWidth-width; } smoothScrollTo(x,
//			 * 0);
//			 */
//
//            int pWidth = pageBox.getWidth();
//            int x = itemLeft - leftPadding;
//            scrollXDelta = x - scrollX;
//            if ((x + width) > pWidth) {// 如果往右把超出屏幕的部分全部滚动过来以后该view左边也不超出屏幕
//                scrollXDelta = pWidth - scrollX - width;// 滚动超出屏幕的部分
//            }
//            // 否则滚动左边展示出来的所有控件的距离
//        }
//
//        return scrollXDelta;
//    }
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0)
            return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
		/*提前滚动的距离*/
        int fadingEdge = 180;
//        int fadingEdge = this.getResources().getDimensionPixelSize(180);



        // leave room for left fading edge as long as rect isn't at very left
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }

        // leave room for right fading edge as long as rect isn't at very right
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }

        int scrollXDelta = 0;

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move right to get it in view: move right just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += (rect.left - screenLeft);
            } else {
                // get entire rect at right of screen
                scrollXDelta += (rect.right - screenRight);
            }

            // make sure we aren't scrolling beyond the end of our content
            int right = getChildAt(0).getRight();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move right to get it in view: move right just enough so
            // that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= (screenRight - rect.right);
            } else {
                // entire rect at left
                scrollXDelta -= (screenLeft - rect.left);
            }

            // make sure we aren't scrolling any further than the left our
            // content
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }

}
