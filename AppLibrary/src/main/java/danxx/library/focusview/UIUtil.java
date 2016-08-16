package danxx.library.focusview;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public class UIUtil {

	/**
	 * 根据给出的画布、矩形区域和图片画出焦点框
	 * @author
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
	public static void drawDrawableAt(Canvas canvas, Rect position, Drawable drawable, boolean doPadding) {
		if (null == canvas || null == position || null == drawable) {
			return;
		}

		Rect padding = new Rect();
		drawable.getPadding(padding);
		if (doPadding) {
			Rect bounds = new Rect();
			bounds.left = position.left - padding.left;
			bounds.top = position.top - padding.top;
			bounds.right = position.right + padding.right;
			bounds.bottom = position.bottom + padding.bottom;
			drawable.setBounds(bounds);
		} else {
			drawable.setBounds(position);
		}
		drawable.draw(canvas);
	}

	/**
	 * 
	 * @author
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

	/**
	 * 
	 * @author
	 * @Title: createViewRect
	 * @Description: 根据容器View(parent), 和子View计算并生成Rect
	 * @param @param parent
	 * @param @param focusView 实际响应焦点的view
	 * * @param @param mFoucsViewId 根据其大小画焦点的view,如果不传，默认根据focusView画焦点
	 * @param @param offset
	 * @param @return
	 * @return Rect
	 * @throws
	 * 
	 * @param parent
	 * @param focusView
	 * @param offset
	 * @return
	 */
	public static Rect createViewRect(View parent, View focusView, int mFoucsViewId, int offset) {

		View child = getDrawFoucsView(focusView, mFoucsViewId);

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

	public static View getDrawFoucsView(View focusView, int mFoucsViewId) {
		View child = null;
		if (mFoucsViewId >= 0) {

			if (focusView.getTag(mFoucsViewId) != null) {
				child = (View) focusView.getTag(mFoucsViewId);
			} else {
				child = focusView.findViewById(mFoucsViewId);
			}

			if (child != null) {
				child.setTag(mFoucsViewId, child);

			} else {
				child = focusView;
			}
		} else {
			child = focusView;
		}
		return child;
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

	public static void getViewRect(View view, View focusView, Rect rect) {
		if (null == view || null == rect) {
			return;
		}

		int offsetX = 0;
		int offsetY = 0;
		if (focusView != null) {
			View child = focusView;
			if (child != null) {
				offsetX = view.getLeft();
				offsetY = view.getTop();
				view = child;
			}
		}

		rect.left = view.getLeft();
		rect.top = view.getTop();
		rect.right = view.getRight();
		rect.bottom = view.getBottom();

		rect.offset(offsetX, offsetY);
	}

	public static void getViewRect(View view, int focusViewId, Rect rect) {
		if (null == view || null == rect) {
			return;
		}

		int offsetX = 0;
		int offsetY = 0;
		if (focusViewId >= 0) {
			View child = null;
			if (view.getTag(focusViewId) != null) {
				child = (View) view.getTag(focusViewId);
			} else {
				child = view.findViewById(focusViewId);
			}

			if (child != null) {
				view.setTag(focusViewId, child);
				offsetX = view.getLeft() + view.getPaddingLeft();
				offsetY = view.getTop() + view.getPaddingTop();
				view = child;
			}
		}

		rect.left = view.getLeft();
		rect.top = view.getTop();
		rect.right = view.getRight();
		rect.bottom = view.getBottom();

		rect.offset(offsetX, offsetY);

	}

	@SuppressLint("NewApi")
	public static void scaleView(View view, float scaleX, float scaleY, float progress) {
		if (null == view) {
			return;
		}
		view.setScaleX(1f + (scaleX - 1f) * (1f - progress));
		view.setScaleY(1f + (scaleY - 1f) * (1f - progress));
	}

	/**
	 * 获取大的画布
	 * @param rects
	 * @return
     */
	public static Rect combineRects(Rect... rects) {
		// quick initialize
		int minLeft = rects[0].left;
		int minTop = rects[0].top;
		int maxRight = rects[0].right;
		int maxBottom = rects[0].bottom;

		// find min/max values
		for (Rect rect : rects) {
			minLeft = Math.min(minLeft, rect.left);
			minTop = Math.min(minTop, rect.top);
			maxRight = Math.max(maxRight, rect.right);
			maxBottom = Math.max(maxBottom, rect.bottom);
		}

		return new Rect(minLeft, minTop, maxRight, maxBottom);
	}

	/**
	 * 添加padding
	 * @param bounds
	 * @param padding
     * @return
     */
	public static Rect includPadding(Rect bounds, Rect padding) {
		return new Rect(bounds.left - padding.left, bounds.top - padding.top, bounds.right + padding.right, bounds.bottom + padding.bottom);
	}

	/**
	 * 缩放矩形区域
	 * @param rect
	 * @param ratioX
	 * @param ratioY
     */
	public static void scaleRect(Rect rect, float ratioX, float ratioY) {
		if (null == rect) {
			return;
		}

		int offsetX = (int) ((float) (rect.right - rect.left) * (ratioX - 1f) / 2);
		int offsetY = (int) ((float) (rect.bottom - rect.top) * (ratioY - 1f) / 2);

		rect.left -= offsetX;
		rect.right += offsetX;
		rect.top -= offsetY;
		rect.bottom += offsetY;
	}

	/**
	 * 计算焦点位置
	 * @param focusRect
	 * @param fromRect
	 * @param toRect
     * @param progress
     */
	public static void calculateFocusPosition(Rect focusRect, Rect fromRect, Rect toRect, float progress) {
		focusRect.left = (int) ((float) (toRect.left - fromRect.left) * progress + fromRect.left);
		focusRect.top = (int) ((float) (toRect.top - fromRect.top) * progress + fromRect.top);
		focusRect.right = (int) ((float) (toRect.right - fromRect.right) * progress + fromRect.right);
		focusRect.bottom = (int) ((float) (toRect.bottom - fromRect.bottom) * progress + fromRect.bottom);
	}
}
