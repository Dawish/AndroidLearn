package danxx.library.tools;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class AnimTools {
	public static Animation zoomAnim(float fromX, float toX, float fromY,
			float toY, long durationMillis,float pivotX,float pivotY) {
		ScaleAnimation myAnimation_Scale = new ScaleAnimation(fromX, toX,
				fromY, toY, Animation.RELATIVE_TO_SELF, pivotX,
				Animation.RELATIVE_TO_SELF, pivotY);
		myAnimation_Scale.setDuration(durationMillis);
		myAnimation_Scale.setInterpolator(new AccelerateInterpolator());
		return myAnimation_Scale;
	}

	public static ObjectAnimator shock(Object obj) {
		//左右抖动范围
		int delta = 10;

		PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe("translationX",
				Keyframe.ofFloat(0f, 0),
				Keyframe.ofFloat(.10f, -delta),
				Keyframe.ofFloat(.26f, delta),
				Keyframe.ofFloat(.42f, -delta),
				Keyframe.ofFloat(.58f, delta),
				Keyframe.ofFloat(.74f, -delta),
				Keyframe.ofFloat(.90f, delta),
				Keyframe.ofFloat(1f, 0f)
		);
		return ObjectAnimator.ofPropertyValuesHolder(obj, pvhTranslateX).
				setDuration(500);
	}

	/**
	 * 左右移动
	 * @param obj
	 * @param startDelta
	 * @param endDelta
     * @return
     */
	public static ObjectAnimator translate(Object obj, int startDelta, int endDelta) {

		PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofFloat("translationX", startDelta, endDelta);
		return ObjectAnimator.ofPropertyValuesHolder(obj, pvhTranslateX).
				setDuration(200);
	}
}
