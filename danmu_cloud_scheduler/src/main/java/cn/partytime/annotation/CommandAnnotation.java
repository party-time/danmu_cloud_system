package cn.partytime.annotation;

import java.lang.annotation.*;

/**
 * Created by admin on 2018/3/14.
 */

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {

    String author() default "";

    String command();

    String comments() default "";

}
