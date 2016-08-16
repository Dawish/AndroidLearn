package danxx.library.focusview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.RelativeLayout;

public class FocusRelativeLayout extends RelativeLayout {


    private int selectedViewIndex = -1;

    /// Listener
    private OnChildViewSelectedListener listener = null;
    public void setOnChildViewSelectedListener(OnChildViewSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnChildViewSelectedListener {
        void OnChildViewSelected(View fromView, View toView, boolean selected);
    }


    public FocusRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public FocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);

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
                    listener.OnChildViewSelected(currSelectedView,nextSelectedView, true);
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


    }


    
    protected int findFirstFocusableViewIndex() {
    	int left = Integer.MAX_VALUE;
    	int top = Integer.MAX_VALUE;
    	

    	for (int i=0; i<getChildCount(); i++) {
    		View view = getChildAt(i);
    		if (View.VISIBLE != view.getVisibility() || !view.isFocusable())
    			continue;

    		if (view.getLeft() < left) {
    			left = view.getLeft();
    			selectedViewIndex = i;
    		}
    	}
    	
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
                listener.OnChildViewSelected(null, view, true);

        } else {
            /// This layout is being deactivated
            View view = getSelectedView();
            startFocusAnimation(view, null);

        }

        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    protected View getSelectedView() {
        return getChildAt(selectedViewIndex );
    }

    protected void setSelectedViewIndex(int index) {
        selectedViewIndex = index;
    }


    protected int getSelectedViewIndex() {
        return selectedViewIndex ;
    }

}