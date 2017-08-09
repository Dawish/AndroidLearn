package danxx.library.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class UIUtils {

    public static float getPixelFromDpi(Context context, float dpi) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, mDisplayMetrics);
    }

    /**
     *
     * @Title: drawDrawableAt
     * @Description: 在窗口中绘制UI
     * @param @param canvas
     * @param @param position
     * @param @param drawable
     * @return void
     * @throws
     *
     * @param canvas
     * @param position
     * @param drawable
     */
    public static void drawDrawableAt(Canvas canvas, Rect position, Drawable drawable) {
        if (null == canvas || null == position || null == drawable) {
            return;
        }

        Rect padding = new Rect();
        drawable.getPadding(padding);
        Rect bounds = new Rect();
        bounds.left = position.left - padding.left;
        bounds.top = position.top - padding.top;
        bounds.right = position.right + padding.right;
        bounds.bottom = position.bottom + padding.bottom;
        drawable.setBounds(bounds);
        drawable.draw(canvas);
    }

    /**
     *
     * @Title: createViewRect
     * @Description: 根据容器View(parent), 和子View计算并生成Rect
     * @param @param parent
     * @param @param child
     * @param @param offset
     * @param @return
     * @return Rect
     * @throws
     *
     * @param parent
     * @param child
     * @param offset
     * @return
     */
    public static Rect createViewRect(View parent, View child, int offset) {

        if (null == child || !isDescentantOf(parent, child)) {
            return null;
        }

        Rect rect = new Rect();

        int[] l1 = new int[2];
        parent.getLocationInWindow(l1);
        int[] l2 = new int[2];
        child.getLocationInWindow(l2);
        child.getGlobalVisibleRect(rect, new Point(0, 0));

        int offsetX = l2[0] - l1[0];
        int offsetY = l2[1] - l1[1];

        int widthOrig = child.getWidth();
        int heightOrig = child.getHeight();
        int widthCurr = rect.width();
        int heightCurr = rect.height();

        float widthRatio = (float) widthCurr / (float) widthOrig;
        float heightRatio = (float) heightCurr / (float) heightOrig;

        int width = 0, height = 0;
        if (widthRatio > heightRatio) {
            width = widthCurr;
            height = (int) (heightOrig * widthRatio);
        } else {
            height = heightCurr;
            width = (int) (widthOrig * heightRatio);
        }

        rect.left = offsetX;
        rect.top = offsetY;
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;

        rect.top += offset;
        rect.left += offset;
        rect.bottom -= offset;
        rect.right -= offset;

        return rect;
    }

    public static boolean isDescentantOf(View parent, View child) {
        boolean isDescent = false;

        if (null == parent || null == child)
            return isDescent;

        View view = child;
        while (null != view.getParent()) {
            if (!(view.getParent() instanceof View))
                break;

            view = (View) view.getParent();

            if (view == parent) {
                isDescent = true;
                break;
            }
        }
        return isDescent;
    }

    private static int ID_OFFSET = 0x100;

    /**
     * 把childView放在最后绘制
     * @param childCount childView的总数
     * @param i
     * @param focusedIndex
     * @return
     */
    public static int getChildDrawingOrder(int childCount, int i, int focusedIndex) {

        if (focusedIndex < 0)
            return i;

        if (i < (childCount - 1)) {
            if (focusedIndex == i)
                i = childCount - 1;
        } else {
            if (focusedIndex < childCount)
                i = focusedIndex;
        }

        return i;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
