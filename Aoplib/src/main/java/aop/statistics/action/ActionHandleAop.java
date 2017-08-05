package aop.statistics.action;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.Object;

/**
 * Created by Dawish on 2017/5/11.
 * 屏幕操作统计
 * http://www.jianshu.com/p/b96a68ba50db
 * http://www.jianshu.com/p/6ccfa7b50f0e
 */

@Aspect  //切面注解
public class ActionHandleAop {

    private final static String TAG = "ActionHandleAop";

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Before("execution(* android.app.Activity.onTouchEvent(..))")
    public void onTouchEvent(JoinPoint joinPoint){
        Log.e(TAG, "onTouchEvent");
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        MotionEvent event = (MotionEvent) args[0];
        //你想做的操作
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
            break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL");
                break;

        }
    }

}
