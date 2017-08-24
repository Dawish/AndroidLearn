package com.anno.service.binder;

import android.os.Binder;
import android.os.IInterface;

import com.anno.service.data.Book;

import java.util.List;

/**
 * Created by dawish on 2017/8/24.
 * Binder的子类具体的功能接口
 */

/**
 * IInterface这是任何实现Binder的子类必须实现的接口
 * 用来获取与当前接口关联的Binder对象
 */

public interface IBookManager extends IInterface {

    public static final String DESCRIPTOR = "com.anno.service.binder.iBookManager";
    /**
     * 用来辨别被远程客户端调用的方法，数值必须介于Binder.FIRST_CALL_TRANSACTION到Binder.LAST_CALL_TRANSACTION
     * 一般一个方法一个常量标志，这里不懂没关系，后边还会继续详解
     */
    public static final int REMOTE_ADD_BOOK = Binder.FIRST_CALL_TRANSACTION + 0;
    public static final int REMOTE_GET_BOOK = Binder.FIRST_CALL_TRANSACTION + 1;

    List<Book> getBook();

    void addBook(Book book);
}