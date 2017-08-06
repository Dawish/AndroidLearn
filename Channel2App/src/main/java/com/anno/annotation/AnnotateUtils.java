package com.anno.annotation;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dawish on 2017/8/5.
 * 自定义注解反射执行工具
 */

public class AnnotateUtils {
    private static final String TAG = "AnnotateUtils";

    /**
     *
     先说原理，我们都要知道，在Java中，通过反射，我们可以知道每一个类的详细信息，
     比如有什么字段，有什么方法，类名等等，我们通过注解和反射配合，使用反射调用类中的方法，
     然后读取注解的参数来进行方法的执行。简单的说，就是其实我们还是会调用findViewById这个方法，
     但是，这个方法可以放到工具类中执行了，我们只需要像上面那样给出参数就行了。

     先说下反射，反射就是允许我们动态的操作一个类，获取类的字段，执行类的方法，获取类的名字等等，
     在这里我们一般会用到下面的这几个方法：

     getMethod：         获取类中的public方法
     getDeclaredMethod：获取类中的所有方法
     getField：         获取类中的public字段（属性）
     getDeclaredField：获取类中的所有字段
     */

    /**
     * findViewById反射执行工具
     * @param activity 方便获取activity中的注解变量
     */
    @Deprecated
    public static void injectViews(Activity activity) {
        Class<? extends Activity> object = activity.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value(); // 获取字段注解的参数，这就是我们传进去控件Id
                if (viewId != -1) {
                    try {
                        // 获取类中的findViewById方法，参数为int
                        Method method = object.getMethod("findViewById", int.class);
                        // 执行该方法，返回一个Object类型的View实例
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        // 把字段的值设置为该View的实例,这个就是为什么你写一个变量，最后可以使用的原因(有赋值操作)
                        field.set(activity, resView);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * find view in activity
     *
     * @param object
     */
    public static void inject(Object object) {
        long time = System.currentTimeMillis();
        reflectFindView(object, null);
        injectMethod(object, null);
        Log.e(TAG, " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * find view in fragment or ViewHolder
     *
     * @param object Fragment or ViewHolder
     * @param view   there is findViewById method view
     */
    public static void inject(Object object, View view) {
        long time = System.currentTimeMillis();
        reflectFindView(object, view);
        injectMethod(object, view);
        Log.e(TAG, " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * 反射找到View Id
     * Find view id by reflect
     *
     * @param object Activity Fragment or ViewHolder
     * @param view   there is findViewById method view
     */
    private static void reflectFindView(Object object, View view) {
        //Access to all of the fields
        Class<?> fieldClass = object.getClass();
        Field[] fields = fieldClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //if every field have you defined annotation class(ViewInject.class) and get view id.
            Field field = fields[i];
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {//if you defined annotation class's values is not null
                reflectFindView(object, view, field, viewInject.value());
            }
        }
    }


    /**
     * findViewById反射执行
     * 反射找控件
     * find view by reflect class
     *
     * @param field annotation field
     * @param view  findViewById class
     * @param id    view's Id
     */
    private static void reflectFindView(Object object, View view, Field field, int id) {
        if (id == -1) {
            return;
        }
        Class<?> fieldCls = object.getClass();
        Class<?> findViewClass = (view == null ? object : view).getClass();
        try {
            // 获取类中的findViewById方法，参数为int
            Method method = (findViewClass == null ? fieldCls : findViewClass).getMethod("findViewById", int.class);
            // 执行该方法，返回一个Object类型的View实例
            Object resView = method.invoke(view == null ? object : view, id);
            field.setAccessible(true);
            // 把字段的值设置为该View的实例
            field.set(object, resView);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * Annotation method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     */
    private static void injectMethod(Object object, View view) {
        long time = System.currentTimeMillis();
        reflectMethod(object, view);
        Log.e(TAG, " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * reflect method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     */
    private static void reflectMethod(Object object, View view) {
        Class<?> objectClass = object.getClass();
        Method[] objectMethods = objectClass.getDeclaredMethods();
        for (int i = 0; i < objectMethods.length; i++) {
            Method method = objectMethods[i];
            method.setAccessible(true);//if method is private
            //get annotation method
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] values = onClick.value();
                for (int index = 0; index < values.length; index++) {
                    int id = values[index];
                    reflectMethod(object, view, id,index, method);
                }
            }
        }
    }


    /**
     * reflect method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     * @param id     annotation view id
     * @param method annotation method
     */
    private static void reflectMethod(final Object object, View view, int id,int index, final Method method) {
        Class<?> objectClass = object.getClass();
        try {
            //通过id反射获取View对象的findViewById方法
            Method findViewMethod = (view == null ? objectClass : view.getClass()).getMethod("findViewById", int.class);
            //通过id调用findviewbyid获得具体的view对象
            final View resView = (View) findViewMethod.invoke(object, id);
            if (resView == null) {
                Log.e(TAG, "@OnClick annotation value view id (index = "+index+") isn't find any view in " + object.getClass().getSimpleName());
                return;
            }
            resView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        method.invoke(object, resView);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private void getAnnonationData(Class clazz) {
        int id;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ViewInject testAnnonation = method.getAnnotation(ViewInject.class);
            if (testAnnonation != null) {
                id = testAnnonation.value();
            }
        }
    }
}
