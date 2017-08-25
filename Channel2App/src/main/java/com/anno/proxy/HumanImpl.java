package com.anno.proxy;

import android.util.Log;

/**
 * 真实对象HumanImpl给服务端调用
 * Created by dawish on 2017/8/25.
 */
public class HumanImpl extends Human {

    @Override
    public void toGoToTheOffice() {
        super.toGoToTheOffice();
        Log.i("danxx", "ManManager toGoToTheOffice完成");
    }

    @Override
    public void work() {
        super.work();
        Log.i("danxx", "ManManager work完成");
    }

    @Override
    public void getOffWork() {
        super.getOffWork();
        Log.i("danxx", "ManManager getOffWork完成");
    }

    @Override
    public IFunction asHuman() {
        return this;
    }

    /**
     * 返回代理对象
     * @param iFunction
     * @return
     */
    public static IFunction asFunction(IFunction iFunction){
        return new Proxy(iFunction);
    }

    /**
     * 代理对象Proxy给客户端调用
     */
    public static class Proxy implements IFunction {
        IFunction remoteFunction;

        public Proxy(IFunction iFunction){
            this.remoteFunction = iFunction;
        }

        @Override
        public void eat(String s) {
            remoteFunction.eat(s);
        }
        @Override
        public void speak(String s) {
            remoteFunction.speak(s);
        }
        @Override
        public void sleep(int time) {
            remoteFunction.sleep(time);
        }

        @Override
        public void toGoToTheOffice() {
            Log.i("danxx", "Proxy toGoToTheOffice");
            Log.i("danxx", "Proxy 挤公交挤地铁挤死人");
            Log.i("danxx", "Proxy 挤了一早上终于到了公司");
            remoteFunction.toGoToTheOffice(); //代理对象完成后通知主人
            Log.i("danxx", "Proxy 开电脑");
        }

        @Override
        public void work() {
            Log.i("danxx", "Proxy work");
            Log.i("danxx", "Proxy 在公司撸代码 往死里撸");
            Log.i("danxx", "Proxy 代码撸到死");
            Log.i("danxx", "Proxy 终于撸完了");
            remoteFunction.work();
            Log.i("danxx", "Proxy 关电脑");
        }

        @Override
        public void getOffWork() {
            Log.i("danxx", "Proxy getOffWork");
            Log.i("danxx", "Proxy 挤公交挤地铁挤死人");
            Log.i("danxx", "Proxy 挤了一晚上终于到家了");
            remoteFunction.getOffWork();
            Log.i("danxx", "Proxy 脱鞋进门");
        }

        @Override
        public IFunction asHuman() {
            return remoteFunction;
        }
    }

}
