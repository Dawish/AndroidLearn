package com.danxx.javalib;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
/**
 * 数据存储仓库和操作
 * 一个缓冲区，缓冲区有最大限制，当缓冲区满
 * 的时候，生产者是不能将产品放入到缓冲区里面的，
 * 当然，当缓冲区是空的时候，消费者也不能从中拿出来产品，
 * 这就涉及到了在多线程中的条件判断
 * Created by dawish on 2017/7/13.
 */
public class Storage {

    private List<String> storage;//生产者和消费者共享的仓库
    public Storage() {
        storage = new ArrayList<String>();
    }
    public List<String> getStorage() {
        return storage;
    }
    public void setStorage(List<String> storage) {
        this.storage = storage;
    }


}
