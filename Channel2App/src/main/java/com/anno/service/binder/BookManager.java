package com.anno.service.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.anno.service.data.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawish on 2017/8/24.
 * 自己实现Binder造作，服务端和客户端的
 */

public class BookManager extends Binder implements IBookManager {
    private List<Book> books = new ArrayList<>();

    /**
     * 给binder接口绑定token
     */
    public BookManager(){
        //这个方法用于给当前的Binder对象添加标签，用于后续系统查找目标Binder对象。
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * 将实际的Binder对象返回给服务端
     * @return
     */
    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    public List<Book> getBook() {
        return books;
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * 将Binder的代理对象返回给客户端
     * @param binder
     * @return
     */
    public static IBookManager asInterface(IBinder binder) {
        if (binder == null)
            return null;
        //当当前Binder对象在本地进程查不到指定标记的Binder接口时，
        // 就说明这个binder对象是一个远程对象，
        // 所以应该将本地的一个代理Binder对象返回给远程客户端。
        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if ((iInterface != null) && (iInterface instanceof IBookManager))
            return (IBookManager) iInterface;
        return new Proxy(binder);  //需要实际操作交给代理对象
    }
    /**
     * 这是Binder中最重要的回调方法，当客户端执行了transact方法时，
     * 就会回调对应的远程Binder对象的onTransact方法。
     * 其中code是用来区分当前客户端所调用的方法，data是输入参数，
     * 这个参数不能为NULL，即使你不需要传递任何参数你也应该传递一个空的Parcel对象。
     * reply是含有远程客户端执行接口的Parcel对象，flags是附加的执行远程操作的标志，
     * 0表示正常的远程调用，1表示不需要返回值的one-way调用。
     *
     * @param code  唯一标识，客户端传递标识执行服务端代码
     * @param data  客户端传递过来的参数
     * @param reply 服务器返回回去的值
     * @param flags 是否有返回值 0:有 1:没有
     * @return
     * @throws RemoteException 异常
     *
     */
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case REMOTE_ADD_BOOK:
                //与客户端的writeInterfaceToken对用，标识远程服务的名称
                //用于说明当前的parcel对象是和制指定了DESCRIPTOR接口相关联的
                data.enforceInterface(DESCRIPTOR);
                Book book = null;
                if (data.readInt() != 0) {
                    book = Book.CREATOR.createFromParcel(data);
                    this.addBook(book); //代理对象操作完成后结果反馈给实际的对象
                    //说明当前操作没有出现异常。
                    reply.writeNoException();
                    return true;
                } else {
                    reply.writeException(new NullPointerException("参数为空"));
                    return false;
                }
            case REMOTE_GET_BOOK:
                data.enforceInterface(DESCRIPTOR);
                List<Book> list = null;
                list = this.getBook(); //代理对象操作完成后结果反馈给实际的对象
                reply.writeNoException();
                reply.writeTypedList(list);
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    /**
     * 实际操作代理类,代理类是客户端使用的
     */
    public static class Proxy implements IBookManager {
        private IBinder remote;

        public Proxy(IBinder binder) {
            remote = binder;
        }

        @Override
        public List<Book> getBook() {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> list = null;
            //writeInterfaceToken用于说明给当前的数据加上中介数据用来标识data是给
            // 含有DESCRIPTOR标志的Binder接口的参数
            data.writeInterfaceToken(DESCRIPTOR);
            try {
                //remote.transact方法的参数和onTransact方法是一样的，当执行到transact方法，
                // 当前线程会阻塞，并调用远程对象的onTransact方法。
                // 注意parcel对象不再使用时必须调用recycle方法进行释放资源，以免造成内存泄漏。
                remote.transact(REMOTE_GET_BOOK, data, reply, 0);
                reply.readException();
                list = reply.createTypedArrayList(Book.CREATOR);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                data.recycle();
                reply.recycle();
            }
            return list;
        }

        @Override
        public void addBook(Book book) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(DESCRIPTOR);
            book.writeToParcel(data, 0);
            try {
                remote.transact(REMOTE_ADD_BOOK, data, reply, 0);
                reply.readException();
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                data.recycle();
                reply.recycle();
            }

        }

        @Override
        public IBinder asBinder() {
            return remote;
        }

        public static String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
    }
}