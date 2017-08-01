package com.java.factory;

/**
 * 川菜厨房做炒饭
 * Created by Dawish on 2016/10/16.
 */

public class ChuanKitchen extends Kitchen {
    @Override
    protected ChaoFan createChaoFan(String type, String userData) {

        if(type.equals("EggChaoFan")){
            return new EggChaoFan(userData);
        }else if(type.equals("ChilliEggChaoFan")){ //加辣椒的蛋炒饭
            return  new EggChaoFan(userData).addChilli();
        }else if(type.equals("RoseChaoFan")){
            return new RoseChaoFan(userData);
        }else if(type.equals("ChilliRoseChaoFan")){
            return new RoseChaoFan(userData).addChilli(); //加辣椒的肉丝炒饭
        }else {
            return null;
        }

    }
}
