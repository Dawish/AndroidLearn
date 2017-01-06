package danxx.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dawish on 2016/12/31.
 * 用五个二阶贝塞尔曲线实现一加天气的多云云朵
 */

public class OnePlusCloudy extends View {

    private Paint paint;
    //默认的基色
    private String DEFAULT_BASE_COLOR = "FFFFFF";

    private PointF startPoint1,startPoint2,startPoint3,startPoint4,startPoint5;
    private PointF endPoint1,endPoint2,endPoint3,endPoint4,endPoint5;
    private PointF controlPoint1,controlPoint2,controlPoint3,controlPoint4,controlPoint5;
    private PointF sunPoint;

    public OnePlusCloudy(Context context) {
        super(context);
        init();
    }

    public OnePlusCloudy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OnePlusCloudy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        startPoint1 = new PointF(0, 0);
        endPoint1 = new PointF(0, 0);
        controlPoint1 = new PointF(0, 0);

        startPoint2 = new PointF(0, 0);
        endPoint2 = new PointF(0, 0);
        controlPoint2 = new PointF(0, 0);

        startPoint3 = new PointF(0, 0);
        endPoint3 = new PointF(0, 0);
        controlPoint3 = new PointF(0, 0);

        startPoint4 = new PointF(0, 0);
        endPoint4 = new PointF(0, 0);
        controlPoint4 = new PointF(0, 0);

        startPoint5 = new PointF(0, 0);
        endPoint5 = new PointF(0, 0);
        controlPoint5 = new PointF(0, 0);

        sunPoint = new PointF(0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        startPoint1.x = w/4;
        startPoint1.y = 0;

        endPoint1.x = w + w;
        endPoint1.y = 0;

        controlPoint1.x = w/2;
        controlPoint1.y = h/2;
    //-------------------------------

        sunPoint.x = w/3*2;
        sunPoint.y = h/5;

    //-------------------------------
        startPoint2.x = w/3;
        startPoint2.y = 0;

        endPoint2.x = w + w;
        endPoint2.y = 0;

        controlPoint2.x = w/2;
        controlPoint2.y = h/3*2;
    //--------------------------------
        startPoint3.x = 0;
        startPoint3.y = 0;

        endPoint3.x = w + w;
        endPoint3.y = 0;

        controlPoint3.x = w/2;
        controlPoint3.y = h/5*3;

    //--------------------------------
        startPoint4.x = -w;
        startPoint4.y = 0;

        endPoint4.x = w/7*5;
        endPoint4.y = -(h/6);

        controlPoint4.x = w/2;
        controlPoint4.y = h/5*3;

    //--------------------------------
        startPoint5.x = -w;
        startPoint5.y = 0;

        endPoint5.x = w/2;
        endPoint5.y = -(h/6);

        controlPoint5.x = w/3;
        controlPoint5.y = h/7*3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path1 = new Path();
        paint.setColor(Color.parseColor("#44"+DEFAULT_BASE_COLOR));
        path1.moveTo(startPoint1.x, startPoint1.y);
        path1.quadTo(controlPoint1.x, controlPoint1.y, endPoint1.x, endPoint1.y);
        canvas.drawPath(path1, paint);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(sunPoint.x, sunPoint.y, 100, paint);

        Path path2 = new Path();
        paint.setColor(Color.parseColor("#66"+DEFAULT_BASE_COLOR));
        path2.moveTo(startPoint2.x, startPoint2.y);
        path2.quadTo(controlPoint2.x, controlPoint2.y, endPoint2.x, endPoint2.y);
        canvas.drawPath(path2, paint);

        Path path4 = new Path();
        paint.setColor(Color.parseColor("#55"+DEFAULT_BASE_COLOR));
        path4.moveTo(startPoint4.x, startPoint4.y);
        path4.quadTo(controlPoint4.x, controlPoint4.y, endPoint4.x, endPoint4.y);
        canvas.drawPath(path4, paint);

        Path path3 = new Path();
        paint.setColor(Color.parseColor("#88"+DEFAULT_BASE_COLOR));
        path3.moveTo(startPoint3.x, startPoint3.y);
        path3.quadTo(controlPoint3.x, controlPoint3.y, endPoint3.x, endPoint3.y);
        canvas.drawPath(path3, paint);

        Path path5 = new Path();
        paint.setColor(Color.parseColor("#77"+DEFAULT_BASE_COLOR));
        path5.moveTo(startPoint5.x, startPoint5.y);
        path5.quadTo(controlPoint5.x, controlPoint5.y, endPoint5.x, endPoint5.y);
        canvas.drawPath(path5, paint);

    }

    /**
     * 部分控制点的Y坐标是加部分控制点的是减
     */
    private void anim(){
        controlPoint1.y += 2;
        controlPoint2.y -= 2;
        controlPoint3.y += 2;
        controlPoint4.y -= 2;
        controlPoint5.y += 2;
        postInvalidate();
    }
    private void resetAnim(){
        controlPoint1.y -= 2;
        controlPoint2.y += 2;
        controlPoint3.y -= 2;
        controlPoint4.y += 2;
        controlPoint5.y -= 2;
        postInvalidate();
    }

    int animCount = 0;

    int resetAnimCount = 0;

    Handler handler = new Handler();

    private boolean isStop = false;
    /**
     * 不断刷新界面
     * 不断来回改变控制点的Y坐标不断重绘界面。
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(isStop){
                return;
            }
            if(animCount < 40){
                anim();
                animCount++;
                if(animCount == 40){
                    resetAnimCount = 0;
                }
            }else{
                resetAnim();
                resetAnimCount++;
                if(resetAnimCount == 40){
                    animCount = 0;
                }
            }
            handler.postDelayed(this, 100);
        }
    };

    public void start(){
        if(!isStop){
            return;
        }
        isStop = false;
        handler.post(runnable);
    }

    public void stop(){
        isStop = true;
        handler.removeCallbacksAndMessages(null);
    }
}
