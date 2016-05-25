package jbyco.optimization.peephole;

import jbyco.optimization.peephole.Pattern;

import java.lang.annotation.*;

/**
 * An annotation for patterns.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Patterns {

    Pattern[] value();

}
