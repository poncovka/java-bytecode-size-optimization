package jbyco.optimization;

import java.lang.annotation.*;

/**
 * Created by vendy on 2.5.16.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Patterns {

    Pattern[] value();

}
