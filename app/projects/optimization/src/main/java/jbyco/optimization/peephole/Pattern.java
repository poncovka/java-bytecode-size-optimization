package jbyco.optimization.peephole;

import java.lang.annotation.*;

/**
 * Created by vendy on 2.5.16.
 */

@Documented
@Repeatable(Patterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Pattern {

    Symbols[] value();

}
