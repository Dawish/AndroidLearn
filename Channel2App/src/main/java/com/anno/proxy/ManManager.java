package com.anno.proxy;

import danxx.library.tools.MyLog;

/**
 * Created by zjyh on 2017/8/25.
 */

public class ManManager extends Human implements IInterface {

    @Override
    public void toGoToTheOffice() {
        MyLog.i("danxx", "toGoToTheOffice");
    }

    @Override
    public void work() {
        MyLog.i("danxx", "work");
    }

    @Override
    public void getOffWork() {
        MyLog.i("danxx", "getOffWork");
    }

    @Override
    public IFunction asHuman() {
        return this;
    }

    /**
     * 返回代理
     * @param iFunction
     * @return
     */
    public static IInterface asInterface(IFunction iFunction){
        return new Proxy(iFunction);
    }

    public static class Proxy implements IInterface{
        IFunction remoteFunction;

        public Proxy(IFunction iFunction){
            this.remoteFunction = iFunction;
        }

        @Override
        public void toGoToTheOffice() {

        }

        @Override
        public void work() {

        }

        @Override
        public void getOffWork() {

        }

        @Override
        public IFunction asHuman() {
            return remoteFunction;
        }
    }

}
