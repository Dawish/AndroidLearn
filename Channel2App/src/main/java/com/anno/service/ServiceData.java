package com.anno.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by dawish on 2017/8/22.
 */

public class ServiceData implements Parcelable {
    public String id;
    public String name;
    public String price;
    public String type;

    /**
     * 读数据恢复
     * @param in
     */
    protected ServiceData(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        type = in.readString();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    /**
     *  读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     *  因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板
     *  参数传入。
       为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数
        分别返回单个和多个继承类实例。
     */
    public static final Creator<ServiceData> CREATOR = new Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel in) {
            return new ServiceData(in);
        }

        @Override
        public ServiceData[] newArray(int size) {
            Log.i("danxx", "newArray size--->"+size);
            return new ServiceData[size];
        }
    };

    /**
     内容描述接口，基本不用管
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
      写入接口函数，打包
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(type);
    }
}
