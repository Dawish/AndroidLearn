package com.dximageloader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dawish on 2017/6/25.
 */
public class DxImageLoader {
    /**
     * Android DiskLruCache完全解析，硬盘缓存的最佳方案 http://blog.csdn.net/guolin_blog/article/details/28863651
     * Android性能优化之使用线程池处理异步任务 http://blog.csdn.net/u010687392/article/details/49850803
     * Android开发之高效加载Bitmap http://www.cnblogs.com/absfree/p/5361167.html
     * Android线程同步 http://blog.csdn.net/peng6662001/article/details/7277851/
     */
    private final static String TAG = "DxImageLoader";
    /**单例*/
    private static DxImageLoader mInstance;
    /**最大的文件缓存量*/
    private static final int MAX_DISK_CACHE_SIZE = 10 * 1024 * 1024;
    /**cpu核心数*/
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**线程池线程数*/
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**最大线程数*/
    private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;
    /**线程存活时间(单位：TimeUnit.SECONDS)*/
    private static final long KEEP_ALIVE = 5L;

    /**图片加载线程池*/
    public static final Executor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    /**内存缓存(软引用)*/
    private LruCache<String, Bitmap> mMemoryCache;
    /**文件缓存*/
    private DiskLruCache mDiskLruCache;

    private DxImageLoader(){
    }
    private boolean inited = false;

    /**单例模式*/
    public static DxImageLoader getInstance(){
        if(null == mInstance){
            synchronized (DxImageLoader.class){
                if(null == mInstance){
                    mInstance = new DxImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int memoryCacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(memoryCacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;// 获取图片占用运行内存大小
            }
        };
        File diskCacheDir = getDiskCacheDir(context,"dx_cache_images");
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if(diskCacheDir.getUsableSpace() >= MAX_DISK_CACHE_SIZE){  //返回分区可用字节的大小
            try {
                //缓存文件， app版本号， 一个key对于多少个value， 最大缓存空间，
                mDiskLruCache = DiskLruCache.open(diskCacheDir, getAppVersion(context), 1, MAX_DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inited = true;
    }

    /**
     * 如果不存在于内存就add
     * @param key
     * @param bitmap
     */
    private void addToMemoryCache(String key, Bitmap bitmap) {
        if (getFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 根据key值获取内存中的bitmap
     * @param key
     * @return
     */
    private Bitmap getFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 从文件缓存中获取图片
     * @param url url（也是存取图片的key）
     * @param dstWidth
     * @param dstHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadFromDisk(String url, int dstWidth, int dstHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "should not Bitmap in main thread");
        }

        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = getKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(0);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = decodeSampledBitmapFromFD(fileDescriptor, dstWidth, dstHeight);
            if (bitmap != null) {
                addToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 压缩从文件取出的bitmap
     * @param fd
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromFD(FileDescriptor fd, int dstWidth, int dstHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calInSampleSize(options, dstWidth, dstHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }
    /**
     * 根据给出的宽高计算图片的采样率
     * @param options
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    private int calInSampleSize(BitmapFactory.Options options, int dstWidth, int dstHeight) {
        //图片本来的宽高
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        int inSampleSize = 1;
        //当图片本来的宽高 大于 需要的苦宽高 就减小采样率
        if (rawWidth > dstWidth || rawHeight > dstHeight) {
            float ratioWidth = (float) rawWidth / dstHeight;
            float ratioHeight = (float) rawHeight / dstHeight;
            inSampleSize = (int) Math.min(ratioWidth, ratioHeight);
        }
        return inSampleSize;
    }
    /**
     * 获取设备的缓存路径
     * @param context
     * @param dirName  缓存文件夹dx_cache_images
     * @return
     */
    public File getDiskCacheDir(Context context, String dirName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dirName);
    }

    /**
     * 获取app版本号
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 图片url作为存取的key做md5处理
     * @param url
     * @return
     */
    private String getKeyFromUrl(String url) {
        String key;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            byte[] m = messageDigest.digest();
            return byteToString(m);
        } catch (NoSuchAlgorithmException e) {
            key = String.valueOf(url.hashCode());
        }
        return key;
    }

    /**
     * byte To String
     * @param b
     * @return
     */
    private static String byteToString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }
    /**
     * step 1（必须步骤）
     * 加载图片的url地址，返回RequestCreator对象
     * @param url
     * @return
     */
    public RequestCreator load(String url) {
        return new RequestCreator(url);
    }

    /**
     * 图片创建类
     */
    private static class RequestCreator implements Runnable{

        String      url;         //图片请求url
        int         holderResId; //默认显示的图片
        int         errorResId;  //加载失败的图片
        ImageView   imageView;   //
        /**
         *
         * @param url 初始化图片的url地址
         */
        public RequestCreator(String url) {
            this.url = url;
        }

        /**
         * step 2(可无步骤)
         * 设置默认图片，占位图片
         * @param holderResId
         */
        public RequestCreator placeholder(int holderResId) {
            this.holderResId = holderResId;
            return this;
        }
        /**
         * step 3(可无步骤)
         * 发生错误加载的图篇
         * @param errorResId
         */
        public RequestCreator error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        /**
         * step 4（必须步骤）
         * 提供设置图片的核心方法
         *
         * @param imageView
         */
        public void into(ImageView imageView) {
            // 变成全局的
            this.imageView = imageView;
            //开始加载图片
        }
        @Override
        public void run() {
            //做网络请求
        }
    }

}
