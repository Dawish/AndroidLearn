package aop.statistics.click;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Dawish on 2017/5/11.
 * 点击统计
 */
@Aspect
public class ClickHandleAop {


    static final String TAG = "statistics";

    @RequiresApi(api = Build.VERSION_CODES.DONUT)

    /**
     *
     *  @注解 访问权限 返回值的类型 包名.函数名(参数)
     *  @注解和访问权限（public/private/protect，以及static/final）属于可选项。如果不设置它们，则默认都会选择。以访问权限为例，如果没有设置访问权限作为条件，那么public，private，protect及static、final的函数都会进行搜索。
     *  返回值类型就是普通的函数的返回值类型。如果不限定类型的话，就用*通配符表示
     *  包名.函数名用于查找匹配的函数。可以使用通配符，包括*和..以及+号。其中*号用于匹配除.号之外的任意字符，而..则表示任意子package，+号表示子类。
     *  比如：
     *  java.*.Date     可以表示java.sql.Date，也可以表示java.util.Date
     *  Test*          可以表示TestBase，也可以表示TestDervied
     *  java..*        表示java任意子类
     *  java..*Model+  表示Java任意package中名字以Model结尾的子类，比如TabelModel，TreeModel 等
*                      最后来看函数的参数。参数匹配比较简单，主要是参数类型，比如：
     *  (int, char)    表示参数只有两个，并且第一个参数类型是int，第二个参数类型是char
     *  (String, ..)   表示至少有一个参数。并且第一个参数类型是String，后面参数类型不限。在参数匹配中，
     *                  ..代表任意参数个数和类型
     *  (Object ...)   表示不定个数的参数，且类型都是Object，这里的...不是通配符，而是Java中代表不定参数的意思
     */

    /**下面第一个*号表示被切入的方法不限定返回类型**/
    @After("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onUserAction(JoinPoint joinPoint){
        Log.e(TAG, "aop statistics click");
        Object[] args = joinPoint.getArgs();
        joinPoint.getTarget();
        if(args == null || args.length == 0){
            return;
        }
        View clickView = (View) args[0];
        //你想做的操作
    }

}
