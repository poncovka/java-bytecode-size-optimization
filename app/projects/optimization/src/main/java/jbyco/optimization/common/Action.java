package jbyco.optimization.common;

/**
 * Created by vendy on 19.5.16.
 */
public interface Action {
    default String getName() {
        return getClass().getDeclaringClass().getSimpleName() + "." + getClass().getSimpleName();
    }
}
