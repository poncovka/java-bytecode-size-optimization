package jbyco.optimization.common;

/**
 * An interface for optimization action loader.
 */
public interface ActionLoader<T extends Action>  {

    /**
     * Loads inner classes that implement actions of type T.
     * @param libraries  classes that contain actions
     */
    void loadActions(Class<?> ...libraries);

    /**
     * Loads actions.
     * @param actions actions
     */
    void loadActions(T ...actions);

    /**
     * Loads an action.
     * @param action action
     */
    void loadAction(T action);

}
