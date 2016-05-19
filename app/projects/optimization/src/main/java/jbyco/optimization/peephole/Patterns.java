package jbyco.optimization.peephole;

import jbyco.optimization.peephole.Pattern;

import java.lang.annotation.*;

/**
 * Created by vendy on 2.5.16.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Patterns {

    Pattern[] value();

}
