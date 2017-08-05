package aop.statistics.log;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by dawish on 2017/8/6.
 */

@Aspect
public class AopLogPointcut {
    //在带有AopLog注解的方法进行切入（注:此处的 * *前面都要有一个空格）
    @Pointcut("execution(@aop.statistics.log.AopLog * *(..))")
    public void logPointcut(){} //注意，这个函数必须要有实现，否则Java编译器会报错

    @After("logPointcut()")
    public void onLogPointcutAfter(JoinPoint joinPoint) throws Throwable{
        Log.i("AOP","onLogPointcutAfter:"+joinPoint.getSignature());
    }
}
