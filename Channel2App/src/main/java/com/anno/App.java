package com.anno;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.os.Bundle;

import com.anno.ui.ActivityDetail;

import java.util.HashMap;
import java.util.Stack;


/**
 * Created by dawish on 2017/2/16.
 */
public class App extends Application {
    private static App mApp;
    public static Stack<ActivityDetail> store;
    //商品详情页最多个数,这里为了测试只写了2,大家根据自己的情况设值
    private static final int MAX_ACTIVITY_DETAIL_NUM = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    public static App getAppContext() {
        return mApp;
    }

    /**
     *
     * @param id
     * @return
     */
    public static boolean toGoodsDetail(String id){

        if(store == null || store.empty()){
            return false;
        }
        for(ActivityDetail activityDetail : store){
            if(id.equalsIgnoreCase(activityDetail.getID())){ //当前商品的详情页已经打开
                activityDetail.finish();
//                这是你需要在AndroidManifest.xml中添加"Android.permission.STOP_APP_SWITCHES"用户权限，前提是必须是系统应用才可以。
//                ActivityManager am = (ActivityManager) getAppContext().getSystemService(Activity.ACTIVITY_SERVICE);
//                am.moveTaskToFront(activityDetail.getTaskId(), 0);
                return true;
            }
        }
        return false;
    }

    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            if(activity instanceof ActivityDetail) {
                if(store.size() >= MAX_ACTIVITY_DETAIL_NUM){
                    store.peek().finish(); //移除栈底的详情页并finish,保证商品详情页个数最大为10
                }
                store.add((ActivityDetail) activity);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }
}

