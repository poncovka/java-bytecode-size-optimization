package jbyco.analyze.patterns.parameters;

/**
 * A representation of the numbered parameter.
 * <p>
 * Each parameter is distinguished by its type and number.
 */
public class NumberedParameter implements AbstractParameter {

    /** The type of the parameter. */
    final ParameterType type;

    /** The number of the parameter. */
    final int number;

    /**
     * Instantiates a new numbered parameter.
     *
     * @param type the type
     * @param number the number
     */
    public NumberedParameter(ParameterType type, int number) {
        this.type = type;
        this.number = number;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return type.toString() + "(" + number + ")";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        NumberedParameter other = (NumberedParameter) obj;

        if (number != other.number) {
            return false;
        }

        if (type != other.type) {
            return false;
        }

        return true;
    }
}
