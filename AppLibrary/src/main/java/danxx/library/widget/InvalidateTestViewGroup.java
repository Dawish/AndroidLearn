package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Dawish on 2016/12/28.
 */

public class InvalidateTestViewGroup extends LinearLayout {
    private Paint paint;
    private boolean isFirstDraw = true;
    public InvalidateTestViewGroup(Context context) {
        super(context);
        init();
    }

    public InvalidateTestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InvalidateTestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Log.d("danxx", "init---->2");
        paint = new Paint();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d("danxx", "dispatchDraw---->2");
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("danxx", "onDraw---->2");
        super.onDraw(canvas);

        if(isFirstDraw){
            paint.setColor(Color.parseColor("#ee2121"));
            isFirstDraw = false;
        }else{
            paint.setColor(Color.parseColor("#aaccdd"));
        }

        canvas.drawCircle(380, 480, 180, paint);
        canvas.drawCircle(380, 980, 180, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("danxx", "draw---->2");
        super.draw(canvas);
    }
}
