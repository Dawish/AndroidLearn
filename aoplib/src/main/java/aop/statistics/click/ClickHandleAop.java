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
 */
@Aspect
public class ClickHandleAop {

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @After("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onUserAction(JoinPoint joinPoint){
        Log.e("danxx", "aop2 statistics click");
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        View clickView = (View) args[0];
        //你想做的操作
    }

    static final String TAG = "statistics";

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @After("execution(* android.view.View.OnClickListener.onClick(..))")
    public void click(JoinPoint joinPoint) {

        Log.e(TAG, "aop click");

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        View v = (View) args[0];

//        String params = (String) v.getTag(R.id.statistics_click_key);

//        if (params == null) {
//            return;
//        }

//        Log.e(TAG, "页面点击埋点触发");
//        try {
//            JSONObject jsonObject = new JSONObject(params);
//            RequestStack.send(jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

}
