package danxx.library.focusview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import danxx.library.R;

public class FocusLinearLayout extends LinearLayout {

    private int focusedItemIndex = -1;
    private View focusItem = null;
    private View fromFocusedView = null;
    private int focusMoveAnim=200;
    private int scaleAnim=0;

    // 画焦点动画
    private DrawFocus mDrawFocus;

    public FocusLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);
        mDrawFocus = new DrawFocus(this);
        mDrawFocus.setFocusHightlightDrawable(R.drawable.home_select_focus);
//        mDrawFocus.setFocusShadowDrawable(R.drawable.focus_shadow);
//		mDrawFocus.setFocusMovingDuration(focusMoveAnim);
//		mDrawFocus.setScaleDuration(scaleAnim);
        mDrawFocus.isCanScale(true);
        mDrawFocus.setScaleValue(1.2f, 1.2f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            final int finalI = i;
            childView.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setFocusedItemIndex(v, finalI, hasFocus);
                }
            });
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d("danxx", "dispatchDraw----");
        if (!hasFocus()) {
            this.fromFocusedView = null;
            return;
        }
        if (focusItem != null) {
            mDrawFocus.drawFocusDynamic(canvas);
            mDrawFocus.drawFocusStatic(canvas);

        }

    }

    /**
     * 获取子控件dispatchDraw的次序
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        if (focusedItemIndex < 0) {
            return i;
        }
        if (i < (childCount - 1)) {
            if (focusedItemIndex == i)
                i = childCount - 1;
        } else {
            if (focusedItemIndex < childCount)
                i = focusedItemIndex;
        }
        return i;
    }

    public void setSpecialFocusedItemIndex(View toItem, int focusedItemIndex, boolean hasFocus) {
        isFromVideoView=false;
        if (hasFocus) {// 如果是异性图得到焦点
            this.focusItem = toItem;
            this.focusedItemIndex = focusedItemIndex;
        } else {
            if (hasFocus()) {
                this.fromFocusedView = toItem;
            } else {// 如果全部失去焦点
                this.fromFocusedView = null;
            }
        }

    }

    private boolean isFromVideoView;

    public void setVideoFocusedItemIndex(View toItem, int focusedItemIndex, boolean hasFocus) {
        isFromVideoView=false;
        if (hasFocus) {// 如果是视频小窗口得到焦点
            this.focusItem = toItem;
            this.focusedItemIndex = focusedItemIndex;
        } else {
            isFromVideoView=true;
            this.fromFocusedView = null;

        }

    }



    public void setFocusedItemIndex(View toItem, int focusedItemIndex, boolean hasFocus) {
        mDrawFocus.setFocusMovingDuration(focusMoveAnim);
        if (hasFocus) {
            if (isFromVideoView) {//如果是来自视频小窗口的
                mDrawFocus.setFocusMovingDuration(0);
            }
            this.focusItem = toItem;
            this.focusedItemIndex = focusedItemIndex;
            mDrawFocus.startFocusAnimation(fromFocusedView, toItem);
            isFromVideoView=false;
        } else {
            isFromVideoView=false;
            mDrawFocus.endFocusAnimation();
            mDrawFocus.startResetAnimation(toItem);
            if (hasFocus()) {
                this.fromFocusedView = toItem;
                postInvalidate();
                return;
            }

            this.fromFocusedView = null;
            postInvalidate();

        }

    }
}