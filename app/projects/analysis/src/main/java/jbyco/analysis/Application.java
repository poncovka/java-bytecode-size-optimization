package jbyco.analysis;

import jbyco.analysis.content.BytecodePrinter;
import jbyco.analysis.patterns.PatternsAnalyzer;
import jbyco.analysis.size.SizeAnalyzer;
import jbyco.analysis.statistics.StatisticsCollector;
import jbyco.analysis.variables.VariablesAnalyzer;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Application is the main class of the jbyco package.
 * It reads command line options and run one of the tools.
 */
public class Application {

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {

        // get options and map of options
        Options options = new Options();

        // process the first argument
        if (args.length > 0 && options.isOption(args[0])) {

            // get option and remaining arguments
            Option option = (Option) options.getOption(args[0]);
            String[] arguments = Arrays.copyOfRange(args, 1, args.length);

            // run command
            switch (option) {
                case HELP:
                    options.help();
                    return;
                case PRINT:
                    BytecodePrinter.main(arguments);
                    break;
                case STATISTICS:
                    StatisticsCollector.main(arguments);
                    break;
                case ANALYZE_VARIABLES:
                    VariablesAnalyzer.main(arguments);
                    break;
                case ANALYZE_PATTERNS:
                    PatternsAnalyzer.main(arguments);
                    break;
                case ANALYZE_SIZE:
                    SizeAnalyzer.main(arguments);
                    break;
            }
            ;
        } else {
            options.help();
            throw new IllegalArgumentException("Unkown argument in '" + Utils.arrayToString(args, " ") + "'.");
        }
    }

    /**
     * The command line options for Application.
     */
    enum Option implements AbstractOption {

        /**
         * The option to analysis size.
         */
        ANALYZE_SIZE("Print sizes of items in classfiles.",
                "--analysis-size"),

        /**
         * The option to analysis variables.
         */
        ANALYZE_VARIABLES("Print usage of local variables and parameters.",
                "--analysis-variables"),

        /**
         * The option to analysis patterns.
         */
        ANALYZE_PATTERNS("Print frequent instruction sequencies.",
                "--analysis-patterns"),

        /**
         * The option to get statistics.
         */
        STATISTICS("Print statistics.",
                "--statistics"),

        /**
         * The option to print bytecode.
         */
        PRINT("Print the content of class files.",
                "--print"),

        /**
         * The option to print help.
         */
        HELP("Show this message.",
                "-h", "--help");

        /**
         * The description of the option.
         */
        String description;

        /* The names of the option. */
        String[] names;

        /**
         * Instantiates a new command line option.
         *
         * @param description the description of the option
         * @param names       the names of the option
         */
        private Option(String description, String... names) {
            this.description = description;
            this.names = names;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String[] getNames() {
            return this.names;
        }
    }

    /**
     * The class of all command line options for Application.
     */
    static class Options extends AbstractOptions {

        /* (non-Javadoc)
         * @see jbyco.lib.AbstractOptions#all()
         */
        @Override
        public AbstractOption[] all() {
            return Option.values();
        }

    }
}
