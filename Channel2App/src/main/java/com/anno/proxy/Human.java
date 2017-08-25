package com.anno.proxy;

import danxx.library.tools.MyLog;

/**
 * Created by dawish on 2017/8/25.
 */

public class Human implements IFunction {
    @Override
    public void eat(String s) {
        MyLog.i("danxx", "eat");
    }

    @Override
    public void speak(String s) {
        MyLog.i("danxx", "speak");
    }

    @Override
    public void sleep(int time) {
        MyLog.i("danxx", "sleep");
    }
}
