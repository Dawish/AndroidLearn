package danxx.library.focusview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

@SuppressLint("NewApi")
public class DrawFocus {

	// private int focusedItemIndex = -1;
	private View focusItem = null;

	// / Is it currently animating?
	private boolean isAnimating = false;

	private Drawable mFocusHightlightDrawable = null;
	private Drawable mFocusShadowDrawable = null;

	// / How much to scale focused view.
	private float scaleRatioX = 1.2f;
	private float scaleRatioY = 1.2f;

	// / View and it's positions holder
	private View fromFocusedView = null;
	private Rect fromFocusedViewRect = null;
	private Rect toFocusedViewRect = null;

	// / Animation parameters
	private long focusMovingDuration = 200;
	private long scaleDuration = 200;
	private long focusAnimationStartTime = 0;
	private static final int DrawFocusMSG = 1;

	private Canvas focusCanvas;

	private ViewGroup parent;

	private boolean canScale;

	public DrawFocus(ViewGroup parent) {
		this.parent = parent;
		fromFocusedViewRect = new Rect();
		toFocusedViewRect = new Rect();

	}

	/** 有时候高亮的不是最外面的子view， 而是更下层的view **/
	private int mFocusRealId = -1;

	/**
	 * 设置画焦点的id， 不设置说明是整个view
	 *
	 * @param id
	 */
	public void setDrawFoucsId(int id) {
		mFocusRealId = id;
	}

	public void isCanScale(boolean canScale) {
		this.canScale = canScale;
	}

	public void setScaleValue(float scaleRatioX, float scaleRatioY) {
		this.scaleRatioX = scaleRatioX;
		this.scaleRatioY = scaleRatioY;
	}

	/**
	 * 焦点设置
	 *
	 * @param resId
	 */
	public void setFocusHightlightDrawable(int resId) {
		mFocusHightlightDrawable = parent.getResources().getDrawable(resId);
	}

	/**
	 * 阴影设置
	 *
	 * @param resId
	 */
	public void setFocusShadowDrawable(int resId) {
		mFocusShadowDrawable = parent.getResources().getDrawable(resId);
	}

	private int mFocusEdgeOffset = 0;

	public void doValidate() {
		// 计算可绘制区域
		Rect padding = new Rect();
		Rect invalidRect = UIUtil.combineRects(fromFocusedViewRect, toFocusedViewRect);
		if (null != mFocusShadowDrawable) {
			mFocusShadowDrawable.getPadding(padding);
			invalidRect = UIUtil.includPadding(invalidRect, padding);
		}
		if (null != mFocusHightlightDrawable) {
			mFocusHightlightDrawable.getPadding(padding);
			invalidRect = UIUtil.includPadding(invalidRect, padding);
		}

		parent.postInvalidate(invalidRect.left, invalidRect.top, invalidRect.right, invalidRect.bottom);
	}

	@SuppressLint("NewApi")
	public void startFocusAnimation(View fromView, View toView) {
		if (null == fromView && null == toView) {
			return;
		}
		fromFocusedView = fromView;
		focusItem = toView;
		toFocusedViewRect = UIUtil.createViewRect(parent, focusItem, mFocusRealId, mFocusEdgeOffset);
//		parent.offsetDescendantRectToMyCoords(focusItem, toFocusedViewRect);
		if (canScale) {
//			toFocusedViewRect.left = (int) (toFocusedViewRect.left - (toFocusedViewRect.width() * (scaleRatioX - 1)) / 2);
//			toFocusedViewRect.right = (int) (toFocusedViewRect.right + (toFocusedViewRect.width() * (scaleRatioX - 1)) / 2);
//			toFocusedViewRect.top = (int) (toFocusedViewRect.top - (toFocusedViewRect.height() * (scaleRatioY - 1)) / 2);
//			toFocusedViewRect.bottom = (int) (toFocusedViewRect.bottom + (toFocusedViewRect.height() * (scaleRatioY - 1)) / 2);
//			UIUtil.scaleRect(toFocusedViewRect, scaleRatioX, scaleRatioY);
		}

		if (fromFocusedView != null) {
//			fromFocusedViewRect = UIUtil.createViewRect(parent, fromFocusedView, mFocusRealId, mFocusEdgeOffset);
			// UIUtil.getViewRect(fromFocusedView, mFocusRealId,
			// fromFocusedViewRect);
			parent.offsetRectIntoDescendantCoords(fromFocusedView, fromFocusedViewRect);
		}

		long duration = System.currentTimeMillis() - focusAnimationStartTime;
		if (duration >= focusMovingDuration) {
			duration = focusMovingDuration;
		}
		focusMovingDuration = duration;
//		endFocusAnimation();

		// 开启缩放动画
		startScaleAnimation();

		if (toView != null) {
			focusAnimationStartTime = System.currentTimeMillis();
		}
		isAnimating = true;
		// 计算可绘制区域并重新绘制
		doValidate();
	}

	protected AnimatorSet animSet;

	public void startScaleAnimation() {
		if (!canScale) {
			return;
		}

		if (focusItem == null) {
			return;
		}
		View scaleView = UIUtil.getDrawFoucsView(focusItem, mFocusRealId);
		if (scaleDuration == 0) {
			scaleView.setScaleX(scaleRatioX);
			scaleView.setScaleY(scaleRatioY);
		} else {
			if (animSet != null) {
				animSet.cancel();
				animSet = null;
			}
			animSet = new AnimatorSet();
			ObjectAnimator animatorX = ObjectAnimator.ofFloat(scaleView, "scaleX", scaleRatioX);
			ObjectAnimator animatorY = ObjectAnimator.ofFloat(scaleView, "scaleY", scaleRatioY);
			LinearInterpolator interpolator = new LinearInterpolator();
			animSet.setInterpolator(interpolator);
			animSet.playTogether(animatorX, animatorY);
			animSet.setDuration(scaleDuration);
			animSet.start();

		}

	}

	private void cancleScaleAnimation() {
		if (!canScale) {
			return;
		}
		if (scaleDuration > 0) {
			if (animSet == null) {
				return;
			}
			animSet.cancel();
			animSet = null;
		}
	}

	protected AnimatorSet animSetReset;

	/**
	 * 充值动画
	 * @param resetView
     */
	public void startResetAnimation(View resetView) {
		if (!canScale) {
			return;
		}
		if (resetView == null) {
			return;
		}
		resetView = UIUtil.getDrawFoucsView(resetView, mFocusRealId);
		if (scaleDuration == 0) {
			resetView.setScaleX(1.0f);
			resetView.setScaleY(1.0f);
		} else {
			if (animSetReset != null) {
				if (!animSetReset.isRunning()) {
					animSetReset.cancel();
					animSetReset = null;
				}

			}
			animSetReset = new AnimatorSet();
			ObjectAnimator animatorX = ObjectAnimator.ofFloat(resetView, "scaleX", 1.0f);
			ObjectAnimator animatorY = ObjectAnimator.ofFloat(resetView, "scaleY", 1.0f);
			LinearInterpolator interpolator = new LinearInterpolator();
			animSetReset.setInterpolator(interpolator);
			animSetReset.playTogether(animatorX, animatorY);
			animSetReset.setDuration(focusMovingDuration);
			animSetReset.start();
		}

	}
	public void cancleResetAnimation() {
		if (!canScale) {
			return;
		}
		if (scaleDuration > 0) {
			if (animSetReset == null) {
				return;
			}
			animSetReset.cancel();
			animSetReset = null;
		}

	}

	/**
	 * 结束动画
	 */
	public void endFocusAnimation() {

		cancleScaleAnimation();
		// focusAnimationStartTime = -1;
		isAnimating = false;
	}


	//用于防止用户快速点击
	private long mLastKeyDownTime = 0;

	public void drawFocusStatic(Canvas canvas) {
		// / is it animating or have no selected view?
		focusCanvas = null;
		focusCanvas = canvas;
		if (isAnimating || focusItem == null || !focusItem.hasFocus()) {
			return;
		}

//		cancleScaleAnimation();
//		// 最后校准焦点框

//		UIUtil.drawDrawableAt(canvas, toFocusedViewRect, mFocusShadowDrawable, true);
		//防止快速点击
//		long curentTimeMs = System.currentTimeMillis();
//		long judgeTime = 300;
//		if ((curentTimeMs - mLastKeyDownTime) < judgeTime){
//			return;
//		}
//		mLastKeyDownTime = curentTimeMs;

		toFocusedViewRect = UIUtil.createViewRect(parent, focusItem, mFocusRealId, mFocusEdgeOffset);
		UIUtil.drawDrawableAt(canvas, toFocusedViewRect, mFocusHightlightDrawable, true);


	}

	private final Handler drawFocusHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {


			return false;
		}
	});

	/**
	 * 焦点跳转停止后确定焦点位置
	 * @param canvas
     */
	private void lastDrawFocus(Canvas canvas){

	}

	public void drawFocusDynamic(Canvas canvas) {
		// / Quick check.
		if (!isAnimating) {
			return;
		}
		if (focusMovingDuration==0) {
			isAnimating=false;
			return;
		}

		// / Calculate time based progress.
		float progress = 0 < focusMovingDuration ? (float) (System.currentTimeMillis() - focusAnimationStartTime) / (focusMovingDuration) : 1.0f;
		if (canScale) {
			if (scaleDuration > 0) {
				if (1.0f <= progress) {
					if (animSet.isRunning()) {
						progress = 0.99f;
					}
				}
			}

		}

		if (1.0f <= progress) {
			progress = 1.0f;
		}
		if (focusMovingDuration>0) {
			// AccelerateDecelerateInterpolator mAnimInterpolator = new
			// AccelerateDecelerateInterpolator();
			AccelerateInterpolator mAnimInterpolator = new AccelerateInterpolator();
			progress = mAnimInterpolator.getInterpolation(progress);
		}

		Rect movingFocusRect = new Rect();
		if (fromFocusedView != null) {
			// fromFocusedViewRect = UIUtil.createViewRect(parent,
			// fromFocusedView, mFocusEdgeOffset);

			UIUtil.calculateFocusPosition(movingFocusRect, fromFocusedViewRect, toFocusedViewRect, progress);

		} else {// 如果沒有焦点来源，直接画在目标view上

			UIUtil.calculateFocusPosition(movingFocusRect, fromFocusedViewRect, toFocusedViewRect, 1);
		}
		UIUtil.drawDrawableAt(canvas, movingFocusRect, mFocusShadowDrawable, true);
		UIUtil.drawDrawableAt(canvas, movingFocusRect, mFocusHightlightDrawable, true);

		if (progress >= 1.0f) {
			// / Animation should have stopped
			endFocusAnimation();
		}

		doValidate();

	}

	public void setFocusMovingDuration(long millSec) {
		focusMovingDuration = millSec;
	}

	public void setScaleDuration(long millSec) {
		scaleDuration = millSec;
	}
}