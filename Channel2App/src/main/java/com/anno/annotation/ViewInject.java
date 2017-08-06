package com.anno.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dawish on 2017/8/5.
 */
/**
 ********** @Documented注解是否将包含在JavaDoc中，默认为false **********

 ********** @Inherited是否可以被继承，默认为false **********

    我们的这个字段值为true的时候，并修饰的我们的class类表示的什么。
    如果一个使用了@Inherited修饰的annotation类型被用于一个class，
    则这个annotation将被用于该class的子类。

 ***************** @Target作用范围 **************************
    ElementType.FIELD           注解作用于变量
    ElementType.METHOD          注解作用于方法
    ElementType.PARAMETER       注解作用于参数
    ElementType.CONSTRUCTOR     注解作用于构造方法
    ElementType.LOCAL_VARIABLE  注解作用于局部变量
    ElementType.PACKAGE         注解作用于包

 ******************** @Retention这注解保留方式 *****************

    RetentionPolicy.SOURCE : 只保留在源码中，不保留在class中，同时也不加载到虚拟机中 。
    在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。

    RetentionPolicy.CLASS : 保留在源码中，同时也保留到class中，但是不加载到虚拟机中 。
    在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式

    RetentionPolicy.RUNTIME : 保留到源码中，同时也保留到class中，最后加载到虚拟机中。
    始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。
    我们自定义的注解通常使用这种方式。

 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    //value也是默认的，在填写参数的时候可以不之名参数名字
    int value() default -1;
}