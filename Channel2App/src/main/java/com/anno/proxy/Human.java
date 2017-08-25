package com.anno.proxy;

import android.util.Log;

/**
 * Created by dawish on 2017/8/25.
 */

public class Human implements IFunction {
    @Override
    public void eat(String s) {
        Log.i("danxx", "不工作专门 eat");
    }

    @Override
    public void speak(String s) {
        Log.i("danxx", "不工作专门 speak");
    }

    @Override
    public void sleep(int time) {
        Log.i("danxx", "不工作专门 sleep");
    }

    @Override
    public void toGoToTheOffice() {
        Log.i("danxx", "不想toGoToTheOffice");
    }

    @Override
    public void work() {
        Log.i("danxx", "不想work");
    }

    @Override
    public void getOffWork() {
        Log.i("danxx", "不想getOffWork");
    }

    @Override
    public IFunction asHuman() {
        return this;
    }
}
