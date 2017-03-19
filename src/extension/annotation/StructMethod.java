package extension.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lessmoon on 2016/8/18.
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StructMethod {
    String value() default "";
    String[] param() default {};
    String ret() default "void";
    boolean virtual() default false;
    boolean purevirtual() default false;
}
