package aop.statistics.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dawish on 2017/8/6.
 * 自定义切面
 * http://www.jianshu.com/p/e94cdbe67a84
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD})
public @interface AopLog {

}