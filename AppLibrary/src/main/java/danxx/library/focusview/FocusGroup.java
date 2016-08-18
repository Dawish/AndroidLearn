package danxx.library.focusview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.LinearLayout;

public class FocusGroup extends LinearLayout {

    /// Is it currently animating?
    private boolean isAnimating = false;

    /// Drawables that hold focus resources.
    private Drawable focusHighlightDrawable = null;
    private Drawable focusShadowDrawable = null;

    /// How much to scale focused view.
    private float scaleRatioX = 1.0f;
    private float scaleRatioY = 1.0f;

    /// Index of current selected view
    private int selectedViewIndex = -1;

    /// View and it's positions holder
    private View fromFocusedView = null;
    private View toFocusedView = null;
    private Rect fromFocusedViewRect = null;
    private Rect toFocusedViewRect = null;

    /// Animation parameters
    private long focusMovingDuration = 300;
    private long focusAnimationStartTime = 0;

    /// Listener
    private OnChildViewSelectedListener listener = null;
    
    private boolean drawFocusOnTop = true;
    
	/** 有时候高亮的不是最外面的子view， 而是更下层的view **/
	private int	mFocusRealId = -1;
    
    public void setDrawFocusOnTop(boolean drawFocusOnTop) {
    	this.drawFocusOnTop = drawFocusOnTop;
    }

    public FocusGroup(Context context) {
        super(context);
        init(context);
    }

    public FocusGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);
        fromFocusedViewRect = new Rect();
        toFocusedViewRect = new Rect();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (selectedViewIndex < 0)
            return i;

        if (i < (childCount - 1)) {
            if (selectedViewIndex == i) {
                return childCount - 1;
            } else {
                return i;
            }
        } else {
            return selectedViewIndex;
        }
    }

    private void doValidate(){
        /// Tell system to keep drawing us.
        Rect padding = new Rect();
        Rect invalidRect = UIUtil.combineRects(fromFocusedViewRect, toFocusedViewRect);
        if (null != focusShadowDrawable) {
            focusShadowDrawable.getPadding(padding);
            invalidRect = UIUtil.includPadding(invalidRect, padding);
        } else if (null != focusHighlightDrawable) {
            focusHighlightDrawable.getPadding(padding);
            invalidRect = UIUtil.includPadding(invalidRect, padding);
        }
        postInvalidate(invalidRect.left, invalidRect.top, invalidRect.right, invalidRect.bottom);
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
    	if (drawFocusOnTop) {
    		super.dispatchDraw(canvas);
    		drawFocusDynamic(canvas);
    		drawFocusStatic(canvas);
    	} else {
    		drawFocusDynamic(canvas);
    		drawFocusStatic(canvas);
    		super.dispatchDraw(canvas);
    	}
    }

    private void playSound(int keyCode){
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(FOCUS_LEFT));
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
        	playSoundEffect(SoundEffectConstants.getContantForFocusDirection(FOCUS_UP));
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        	playSoundEffect(SoundEffectConstants.getContantForFocusDirection(FOCUS_RIGHT));
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
        	playSoundEffect(SoundEffectConstants.getContantForFocusDirection(FOCUS_DOWN));
            break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("danxx" , "fg onKeyDown");
        /// find current selected view
        View currSelectedView = getSelectedView();
        if (currSelectedView == null){
        	return super.onKeyDown(keyCode, event);
        }

        /// find the view that to be selected by 'focus direction'
        View nextSelectedView = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                nextSelectedView = currSelectedView.focusSearch(FOCUS_LEFT);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                nextSelectedView = currSelectedView.focusSearch(FOCUS_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                nextSelectedView = currSelectedView.focusSearch(FOCUS_RIGHT);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                nextSelectedView = currSelectedView.focusSearch(FOCUS_DOWN);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            	currSelectedView.performClick();
            	return super.onKeyDown(keyCode, event);
        }

        if (null != nextSelectedView) {
            if (nextSelectedView.getParent() == this) {
                /// If Focus changing has occurred, start to draw focus animation.
                startFocusAnimation(currSelectedView, nextSelectedView);
                setSelectedViewIndex(indexOfChild(nextSelectedView));
                if (null != listener) {
                    listener.OnChildViewSelected(currSelectedView, false);
                    listener.OnChildViewSelected(nextSelectedView, true);
                }
                playSound(keyCode);
                return true;
            } else {
                // Focus is going somewhere else
            }
        }

       
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NewApi")
	private void startFocusAnimation(View fromView, View toView) {
    	 if (null == fromView && null == toView) {
             return;
         }
    	 
 		if (fromView != null && fromView.isSelected()){
 			fromView.setSelected(false);
		}
		if (toView != null && !toView.isSelected()){
			toView.setSelected(true);
		}

        endFocusAnimation();
        
        /// Set initial data.
        fromFocusedView = fromView;
        toFocusedView = toView;
        
         /// Reset states
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (null != fromFocusedView) {
                fromFocusedView.setScaleX(1.0f);
                fromFocusedView.setScaleY(1.0f);
            }
            if (null != toFocusedView) {
                toFocusedView.setScaleX(scaleRatioX);
                toFocusedView.setScaleY(scaleRatioY);
            }
        }
         
         if (toView != null && fromView != null){
             focusAnimationStartTime = System.currentTimeMillis(); 
         }
         isAnimating = true;

         /// Calculate Focus' starting position.
         if (null != fromFocusedView) {
        	 UIUtil.getViewRect(fromFocusedView,mFocusRealId, fromFocusedViewRect);
        	 UIUtil.scaleRect(fromFocusedViewRect,scaleRatioX, scaleRatioY);
         } else {
             /// A little trick here.
        	 UIUtil.getViewRect(toFocusedView,mFocusRealId, fromFocusedViewRect);
         }

         /// Calculate Focus' ending position.
         if (null != toFocusedView) {
        	 UIUtil.getViewRect(toFocusedView,mFocusRealId, toFocusedViewRect);
        	 UIUtil.scaleRect(toFocusedViewRect,scaleRatioX, scaleRatioY);
         } else {
             /// Same trick here.
        	 UIUtil.getViewRect(fromFocusedView,mFocusRealId, toFocusedViewRect);
         }

         /// start animating
         doValidate();
    }

    private void endFocusAnimation() {
//        fromFocusedView = null;
//        toFocusedView = null;
        focusAnimationStartTime = -1;
        isAnimating = false;
    }

    private void drawFocusStatic(Canvas canvas) {
        /// is it animating or have no selected view?
        if(isAnimating || selectedViewIndex < 0 || !hasFocus()) {
            return;
        }

        //计算最后的位置，确保不出现错乱
        if (toFocusedView != null){
        	UIUtil.getViewRect(toFocusedView,mFocusRealId, toFocusedViewRect);
        }
        
        /// draw shadow and highlighted focus.
        drawDrawableAt(canvas, toFocusedViewRect, focusShadowDrawable);
        drawDrawableAt(canvas, toFocusedViewRect, focusHighlightDrawable);
    }

    private void drawFocusDynamic(Canvas canvas) {
        /// Quick check.
        if (!isAnimating) {
            return;
        }

        /// Calculate time based progress.
        float progress = 0 < focusMovingDuration ?
                (float) (System.currentTimeMillis() - focusAnimationStartTime) /focusMovingDuration :
                1.0f;
        if (1.0f <= progress) {
            progress = 1.0f;
        }
        
        /// Scale Views
//        if (null != fromFocusedView) {
//            scaleView(fromFocusedView, scaleRatioX, scaleRatioY, progress);
//        }
//        if (null != toFocusedView) {
//            scaleView(toFocusedView, scaleRatioX, scaleRatioY, 1f - progress);
//        }

        /// Calculate the position of the focus, and draw it.
        Rect movingFocusRect = new Rect();
        UIUtil.calculateFocusPosition(movingFocusRect, fromFocusedViewRect, toFocusedViewRect, progress);
        drawDrawableAt(canvas, movingFocusRect,focusHighlightDrawable);

        if (progress >= 1.0f) {
            /// Animation should have stopped
            endFocusAnimation();
        }

        doValidate();
    }

    @SuppressLint("NewApi")
	private void scaleView(View view, float scaleX, float scaleY, float progress) {
        if (null == view) {
            return;
        }
        view.setScaleX(1f + (scaleX - 1f) * (1f - progress));
        view.setScaleY(1f + (scaleY - 1f) * (1f - progress));
    }

    private void drawDrawableAt(Canvas canvas, Rect position, Drawable drawable) {
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
    
    protected int findFirstFocusableViewIndex() {
    	int left = Integer.MAX_VALUE;
    	int top = Integer.MAX_VALUE;
    	
//    	int index = -1;
    	for (int i=0; i<getChildCount(); i++) {
    		View view = getChildAt(i);
    		if (View.VISIBLE != view.getVisibility() || !view.isFocusable())
    			continue;

    		if (view.getLeft() < left) {
    			left = view.getLeft();
    			selectedViewIndex = i;
    		}
    	}
    	
    	// FIXME: whatif no visible view?
    	return selectedViewIndex;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (true == gainFocus) {

            /// This layout is being activated, let find the first selected item.
            if (-1 == getSelectedViewIndex()) {
                setSelectedViewIndex(findFirstFocusableViewIndex());
            }
            View view = getSelectedView();
            startFocusAnimation(null, view);

            if (null != listener)
                listener.OnChildViewSelected(view, true);

        } else {
            /// This layout is being deactivated
            View view = getSelectedView();
            startFocusAnimation(view, null);

            if (null != listener)
                listener.OnChildViewSelected(view, false);
        }

        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public View getSelectedView() {
        return getChildAt(selectedViewIndex );
    }

    public void setSelectedViewIndex(int index) {
        selectedViewIndex = index;
    }
    
    public void setSelectView(View nextSelectedView){
    	View currSelectedView = getSelectedView();
    	if (null != nextSelectedView) {
            if (nextSelectedView.getParent() == this) {
                /// If Focus changing has occurred, start to draw focus animation.
                startFocusAnimation(currSelectedView, nextSelectedView);
                setSelectedViewIndex(indexOfChild(nextSelectedView));
                if (null != listener) {
                    listener.OnChildViewSelected(currSelectedView, false);
                    listener.OnChildViewSelected(nextSelectedView, true);
                }
            } else {
            }
        }
    }

    public int getSelectedViewIndex() {
        return selectedViewIndex ;
    }



    public void setFocusHighlightDrawable(int resId) {
        focusHighlightDrawable = getContext().getResources().getDrawable(resId);
    }

    public void setFocusShadowDrawable(int resId) {
        focusShadowDrawable = getContext().getResources().getDrawable(resId);
    }

    public void setFocusScale(float scaleX, float scaleY) {
        scaleRatioX = scaleX;
        scaleRatioY = scaleY;
    }

    public void setFocusMovingDuration(long millSec) {
        focusMovingDuration = millSec;
    }
    
	/**
	 * 设置实际focus的id， 不设置说明是整个view
	 * @param id
	 */
	public void setFocusRealId(int id){
		mFocusRealId = id;
	}

    public void setOnChildViewSelectedListener(OnChildViewSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnChildViewSelectedListener {
        void OnChildViewSelected(View view, boolean selected);
    }
}