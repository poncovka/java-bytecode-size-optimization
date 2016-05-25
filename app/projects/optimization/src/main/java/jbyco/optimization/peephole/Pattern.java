package jbyco.optimization.peephole;

import java.lang.annotation.*;

/**
 * An annotation for pattern.
 */

@Documented
@Repeatable(Patterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Pattern {

    Symbols[] value();

}
