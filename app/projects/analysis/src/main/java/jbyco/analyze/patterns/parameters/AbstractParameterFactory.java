package jbyco.analyze.patterns.parameters;

/**
 * A factory for creating AbstractParameter objects.
 */
public interface AbstractParameterFactory {

    /**
     * Initializes the factory.
     */
    public void init();

    /**
     * Gets the variable.
     *
     * @param index the index of the variable
     * @return the abstracted parameter
     */
    public AbstractParameter getVariable(int index);

    /**
     * Gets the integer.
     *
     * @param i the integer
     * @return the abstracted parameter
     */
    public AbstractParameter getInt(int i);

    /**
     * Gets the float.
     *
     * @param f the float
     * @return the abstracted parameter
     */
    public AbstractParameter getFloat(float f);

    /**
     * Gets the long.
     *
     * @param l the long
     * @return the abstracted parameter
     */
    public AbstractParameter getLong(long l);

    /**
     * Gets the double.
     *
     * @param d the double
     * @return the abstracted parameter
     */
    public AbstractParameter getDouble(double d);

    /**
     * Gets the string.
     *
     * @param s the string
     * @return the abstracted parameter
     */
    public AbstractParameter getString(String s);

    /**
     * Gets the class.
     *
     * @param internalName the internal name
     * @return the abstracted parameter
     */
    public AbstractParameter getClass(String internalName);

    /**
     * Gets the array.
     *
     * @param internalName the internal name
     * @return the abstracted parameter
     */
    public AbstractParameter getArray(String internalName);

    /**
     * Gets the field.
     *
     * @param name the name
     * @param desc the descriptor
     * @return the abstracted parameter
     */
    public AbstractParameter getField(String name, String desc);

    /**
     * Gets the method.
     *
     * @param name the name
     * @param desc the descriptor
     * @return the method
     */
    public AbstractParameter getMethod(String name, String desc);

    /**
     * Gets the method parameter.
     *
     * @param index the index of the parameter
     * @return the abstracted parameter
     */
    public AbstractParameter getMethodParameter(int index);

    public AbstractParameter getNull();
    public AbstractParameter getThis();
}
