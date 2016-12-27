package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Dawish on 2016/12/27.
 */

public class InvalidateTest extends View {

    private Paint paint;

    private boolean isFirstDraw = true;

    public InvalidateTest(Context context) {
        super(context);
        init();
    }

    public InvalidateTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InvalidateTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init(){
        Log.d("danxx", "init---->");
        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Log.d("danxx", "onDraw---->");
        if(isFirstDraw){
            paint.setColor(Color.parseColor("#212121"));
            isFirstDraw = false;
        }else{
            paint.setColor(Color.parseColor("#aa2cc1"));
        }
        canvas.drawCircle(280, 380, 180, paint);
        canvas.drawCircle(280, 480, 180, paint);
    }
}
