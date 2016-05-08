package jbyco.optimization;

import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vendy on 3.5.16.
 */
public class Application {

    public static void main(String[] args) throws IOException {

        // command line options
        Options options = new Options();

        // parameters
        Path outputDirectory = Paths.get(System.getProperty("user.dir"));
        Collection<Path> paths = new ArrayList<>();

        // process command line options
        for (int index = 0; index < args.length; index++) {

            String arg = args[index];
            Option option = (Option) options.getOption(arg);

            if (option == null) {

                while (index < args.length) {
                    paths.add(Paths.get(args[index++]));
                }
            }
            else {

                switch (option) {
                    case OUTPUT:
                        outputDirectory = Paths.get(args[++index]);
                        break;
                    case HELP:
                        options.help();
                        return;
                }
            }

        }

        // create file optimizer
        Optimizer optimizer = new BytecodeOptimizer();

        // create temporary directory
        Path workingDirectory = TemporaryFiles.createDirectory();

        try {

            // process files
            for (Path path : paths) {

                // process files on the path
                for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {

                    // get the bytes
                    InputStream in = file.getInputStream();
                    byte[] bytes = Utils.toByteArray(in);
                    in.close();

                    // optimization the class file
                    bytes = optimizer.optimizeClassFile(bytes);

                    // recreate the file in a new path
                    Path path2 = file.resolveRelativePath(outputDirectory);
                    CommonFile file2 = file.copy(path2.toFile());
                    file2.toFile().getParentFile().mkdirs();

                    // write the optimized bytes to the prepared file
                    OutputStream out = file2.getOutputStream();
                    out.write(bytes);
                    out.close();
                }
            }
        } finally {
            TemporaryFiles.deleteDirectory(workingDirectory);
        }

    }

    /**
     * The command line options.
     */
    enum Option implements AbstractOption {

        OUTPUT  ("Set the output directory. Default: '.'.",
                "-o", "--output"),
        HELP    ("Show this message.",
                "-h", "--help");

        /**
         * The description.
         */
        String description;

        /**
         * The names.
         */
        String[] names;

        /**
         * Instantiates a new option.
         *
         * @param description the description
         * @param names       the names
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
     * Command line options.
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
