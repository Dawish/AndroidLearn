package com.anno.service.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dawish on 2017/8/24.
 * http://blog.csdn.net/qq_25722767/article/details/51895992
 */

public class Book implements Parcelable {
    private String name;
    private int id;

    public Book() {
    }

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected Book(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }
    /**
     * 必须实现且名称必须是CREATOR
     */
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        /**
         * 将数据反序列化
         * @param in
         */
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /**
     * 通常情况下返回0，当对象数据含有文件描述符的时候返回1
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * 将数据序列化，flags通常为0，为1的时候表示当前对象需要做为返回值返回不能立即释放资源
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }
}


