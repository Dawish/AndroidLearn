package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dawish on 2016/12/29.
 * 贝塞尔曲线测试
 */

public class Bezier extends View {

    //画笔
    private Paint paint;
    // 起始点，结束点，控制点
    private PointF startP,endP,controlP;
    // 整个view的中心点，别的点以这个点为基础来偏移
    private int centerX, centerY;

    public Bezier(Context context) {
        super(context);
        init();
    }

    public Bezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(60);

        startP = new PointF(0, 0);
        endP = new PointF(0, 0);
        controlP = new PointF(0, 0);
    }

    /**
     * View大小改变的时候调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("danxx", "onSizeChanged--->");
        centerX = w/2;
        centerY = h/2;
        // 初始化三个点的位置
        startP.x = centerX - 400;
        startP.y = centerY;

        endP.x = centerX + 400;
        endP.y = centerY;

        controlP.x = centerX;
        controlP.y = centerY -600;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        //触摸屏幕时重绘,不断改变控制点的位置
        controlP.x = event.getX();
        controlP.y = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制起点、终点和控制点
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(20);
        //起点
        canvas.drawPoint(startP.x, startP.y, paint);
        //终点
        canvas.drawPoint(endP.x, endP.y, paint);
        //控制点
        canvas.drawPoint(controlP.x, controlP.y, paint);

        //绘制辅助线
        paint.setStrokeWidth(4);
        canvas.drawLine(startP.x, startP.y, controlP.x, controlP.y, paint);
        canvas.drawLine(controlP.x, controlP.y, endP.x, endP.y, paint);

        //绘制贝塞尔曲线
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        Path path = new Path();
        //曲线起始点
        path.moveTo(startP.x, startP.y);
        path.quadTo(controlP.x, controlP.y, endP.x, endP.y);

        canvas.drawPath(path, paint);

    }
}
