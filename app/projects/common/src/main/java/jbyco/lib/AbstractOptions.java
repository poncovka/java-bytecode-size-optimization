package jbyco.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for managing command line options.
 */
public abstract class AbstractOptions {

    /**
     * The map of names mapped to options.
     */
    Map<String, AbstractOption> map = new HashMap<>();

    /**
     * Instantiates a new abstract options.
     */
    public AbstractOptions() {

        // init map
        for (AbstractOption option : all()) {
            for (String name : option.getNames()) {
                map.put(name, option);
            }
        }
    }

    /**
     * Gets all options.
     *
     * @return the abstract option[]
     */
    public abstract AbstractOption[] all();

    /**
     * Gets the option.
     *
     * @param name the name of the option
     * @return the option
     */
    public AbstractOption getOption(String name) {
        return map.get(name);
    }

    /**
     * Checks if the name is an option.
     *
     * @param name the name
     * @return true, if it is an option
     */
    public boolean isOption(String name) {
        return map.containsKey(name);
    }

    /**
     * Help.
     */
    public void help() {

        // TODO print usage
        // TODO print description

        // print options and description
        for (AbstractOption option : all()) {
            System.out.printf("%-30s %s\n", Utils.arrayToString(option.getNames(), ", "), option.getDescription());
        }
    }


}
