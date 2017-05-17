package aop.statistics.life;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Dawish on 2017/5/17.
 * Activity 生命周期统计
 */
@Aspect
public class LifeCycleHandleAop {

    private final static String TAG = "LifeCycleHandleAop";

    private long onCreateTime;

    /**
     * 使用切片的方式
     */
    @Pointcut("execution(* android.app.Activity.onCreate(..))" )
    public void lifeOnCreate(){
    }

    @Pointcut("execution(* android.app.Activity.onDestroy(..))")
    public void lifeOnDestroy(){
    }

    @Before("lifeOnCreate()")
    public void lifeOnCreateHandle(JoinPoint joinPoint){
        Log.v(TAG, "lifeOnCreate -->");
        onCreateTime = System.currentTimeMillis();
//        Log.v(TAG, "onCreateTime -->"+onCreateTime);
    }

    @Before("lifeOnDestroy()")
    public void lifeOnDestroyHandle(JoinPoint joinPoint){
        Log.v(TAG, "lifeOnDestroy -->");
        long toatalTime = System.currentTimeMillis() - onCreateTime;
        Log.v(TAG, "lifeCycle toatalTime -->"+toatalTime+"ms");
    }

}
