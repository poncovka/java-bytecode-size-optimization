package jbyco.optimization.common;

/**
 * An interface for optimizating action.
 */
public interface Action {
    default String getName() {
        return getClass().getDeclaringClass().getSimpleName() + "." + getClass().getSimpleName();
    }
}
