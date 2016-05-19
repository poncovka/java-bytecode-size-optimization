package jbyco.optimization.common;

/**
 * Created by vendy on 19.5.16.
 */
public interface ActionLoader<T>  {

    void loadActions(Class<?> ...libraries);
    void loadActions(T ...actions);
    void loadAction(T action);

}
