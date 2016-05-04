package jbyco.analysis.patterns.parameters;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating NumberedParameter objects.
 */
public class NumberedParameterFactory extends FullParameterFactory {

    /**
     * The map of parameters mapped to numbered parameters.
     */
    Map<AbstractParameter, AbstractParameter> map;

    /**
     * The maximal numbers used in numbered parameters.
     */
    Map<ParameterType, Integer> numbers;

    /**
     * Instantiates a new numbered parameter factory.
     */
    public NumberedParameterFactory() {
        init();
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.parameters.FullParameterFactory#init()
     */
    @Override
    public void init() {
        map = new HashMap<>();
        numbers = new EnumMap<>(ParameterType.class);
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.parameters.FullParameterFactory#createParameter(jbyco.analysis.patterns.parameters.ParameterType, java.lang.Object[])
     */
    @Override
    public AbstractParameter createParameter(ParameterType type, Object... components) {

        // get full parameter
        AbstractParameter p1 = super.createParameter(type, components);

        // try to find parameter in a map
        AbstractParameter p2 = map.get(p1);

        // create new parameter of the given type
        if (p2 == null) {

            // get number of the type
            int number = numbers.getOrDefault(type, 0);

            // create new parameter
            p2 = new NumberedParameter(type, number);

            // update maps
            numbers.put(type, number + 1);
            map.put(p1, p2);
        }

        // return the parameter
        return p2;
    }

}
