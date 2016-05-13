package jbyco.analysis.patterns;

import jbyco.analysis.Analyzer;
import jbyco.analysis.patterns.instructions.AbstractInstruction;
import jbyco.analysis.patterns.instructions.Abstractor;
import jbyco.analysis.patterns.instructions.Cache;
import jbyco.analysis.patterns.labels.AbstractLabelFactory;
import jbyco.analysis.patterns.labels.RelativeLabelFactory;
import jbyco.analysis.patterns.operations.AbstractOperationFactory;
import jbyco.analysis.patterns.operations.GeneralOperationFactory;
import jbyco.analysis.patterns.operations.TypedOperationFactory;
import jbyco.analysis.patterns.parameters.AbstractParameterFactory;
import jbyco.analysis.patterns.parameters.FullParameterFactory;
import jbyco.analysis.patterns.parameters.GeneralParameterFactory;
import jbyco.analysis.patterns.parameters.NumberedParameterFactory;
import jbyco.analysis.patterns.tree.Tree;
import jbyco.analysis.patterns.tree.TreeBuilder;
import jbyco.analysis.patterns.wildcards.WildSequenceGenerator;
import jbyco.io.BytecodeFilesCounter;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

/**
 * PatternsAnalyzer is a class for finding common sequences of instructions.
 * <p>
 * The class builds a representation of the common instruction sequences and
 * prints the content of this representation. The sequences are represented
 * by {@link Tree}. The class {@link Abstractor} allows to use a different
 * levels of abstraction for the instructions. The class {@link Cache} is
 * used to reduce the number of objects with instructions.
 */
public class PatternsAnalyzer implements Analyzer {

    /**
     * The number of wild cards in a sequence.
     */
    final int WILDCARDS;

    /**
     * The maximal length of the sequence.
     */
    final int MAX_LENGTH;

    /**
     * The minimal frequency of the printed sequence.
     */
    final int MIN_FREQUENCY;

    /**
     * The maximal memory usage in percents.
     */
    final int MEMORY_USAGE;

    /**
     * The tree of sequences.
     */
    Tree<AbstractInstruction> graph;

    /**
     * The tree builder.
     */
    TreeBuilder<AbstractInstruction> builder;

    /**
     * The instructions' abstractor.
     */
    Abstractor abstractor;

    /**
     * The instructions' cache.
     */
    Cache cache;

    /**
     * The label marks the start of the current method.
     */
    Label begin = null;

    /**
     * The label marks the end of the current method.
     */
    Label end = null;

    /**
     * The current pruning threshold.
     */
    int pruningThreshold = 0;

    /**
     * The number of processed instructions.
     */
    long numbefOfInsn = 0;

    /**
     * The factory for operations' abstraction.
     */
    AbstractOperationFactory operations;

    /**
     * The factory for parameters' abstraction.
     */
    AbstractParameterFactory parameters;

    /**
     * The factory for labels' abstraction.
     */
    AbstractLabelFactory labels;

    /**
     * Instantiates a new patterns analyzer.
     *
     * @param operations   the operations' factory
     * @param parameters   the parameters' factory
     * @param labels       the labels' factory
     * @param wildcards    the number of wild cards
     * @param maxLength    the maximal length of sequences
     * @param minFrequency the minimal frequency of printed sequences
     * @param memoryUsage  the maximal memory usage
     */
    public PatternsAnalyzer(
            AbstractOperationFactory operations,
            AbstractParameterFactory parameters,
            AbstractLabelFactory labels,
            int wildcards,
            int maxLength,
            int minFrequency,
            int memoryUsage
    ) {

        // init parameters
        this.WILDCARDS = wildcards;
        this.MAX_LENGTH = maxLength;
        this.MIN_FREQUENCY = minFrequency;
        this.MEMORY_USAGE = memoryUsage;

        // init factories
        this.operations = operations;
        this.parameters = parameters;
        this.labels = labels;

        // create tree and the builder
        this.graph = new Tree<>();
        this.builder = new TreeBuilder<>(graph);

        // create instruction cache
        this.cache = new Cache();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {

        // get options and map of options
        Options options = new Options();

        // set default values
        int wildcards = 0;
        int maxLength = 10;
        int minFrequency = 100;
        int memoryUsage = 70;
        String delimiter = "; ";
        boolean progress = true;

        // set default factories
        AbstractOperationFactory operations = new TypedOperationFactory();
        AbstractParameterFactory parameters = new FullParameterFactory();
        AbstractLabelFactory labels = new RelativeLabelFactory();

        // init list of paths
        Collection<Path> paths = new ArrayList<>();

        // process parameters
        for (int index = 0; index < args.length; index++) {

            // get option
            String arg = args[index];
            Option option = (Option) options.getOption(arg);

            // get paths to input files
            if (option == null) {

                while (index < args.length) {
                    paths.add(Paths.get(args[index++]));
                }

                continue;
            }

            // process parameters
            switch (option) {
                case HELP:
                    options.help();
                    return;
                case NO_PROGRESS:
                    progress = false;
                    break;
                case DELIMITER:
                    delimiter = args[++index];
                    break;
                case MAX_LENGTH:
                    maxLength = Integer.parseUnsignedInt(args[++index]);
                    break;
                case MIN_FREQUENCY:
                    minFrequency = Integer.parseUnsignedInt(args[++index]);
                    break;
                case WILDCARDS:
                    wildcards = Integer.parseUnsignedInt(args[++index]);
                    break;
                case GENERAL_OPERATIONS:
                    operations = new GeneralOperationFactory();
                    break;
                case TYPED_OPERATIONS:
                    operations = new TypedOperationFactory();
                    break;
                case GENERAL_PARAMETERS:
                    parameters = new GeneralParameterFactory();
                    break;
                case NUMBERED_PARAMETERS:
                    parameters = new NumberedParameterFactory();
                    break;
                case FULL_PARAMETERS:
                    parameters = new FullParameterFactory();
                    break;
            }
        }

        // count files
        int counter = 0;
        int total = (new BytecodeFilesCounter()).countAll(paths);
        ;

        // init analyzer
        PatternsAnalyzer analyzer =
                new PatternsAnalyzer(
                        operations,
                        parameters,
                        labels,
                        wildcards,
                        maxLength,
                        minFrequency,
                        memoryUsage
                );

        // create temporary directory
        Path workingDirectory = TemporaryFiles.createDirectory();

        try {

            // analysis files
            for (Path path : paths) {

                // process files on the path
                for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {

                    // analysis file
                    InputStream in = file.getInputStream();
                    analyzer.processClassFile(in);
                    in.close();

                    // show progress
                    if (progress) {
                        System.err.printf(
                                "Processed %s%%, pruned %d times.\r",
                                Utils.longDivToString(++counter * 100, total),
                                analyzer.pruningThreshold
                        );
                    }
                }
            }

            // last prune
            analyzer.finishProcessing();

        }
        // delete temporary directory
        finally {
            TemporaryFiles.deleteDirectory(workingDirectory);
        }

        // print info
        if (progress) {
            System.err.printf(
                    "Processed 100%%, pruned %d times.\n",
                    analyzer.pruningThreshold
            );
        }

        // print results
        PrintWriter writer = new PrintWriter(System.out, true);
        analyzer.writeResults(writer, delimiter);
        writer.close();

    }

    /**
     * Gets the representation of instruction sequences.
     *
     * @return the tree of instruction sequences
     */
    public Tree<AbstractInstruction> getGraph() {
        return graph;
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.Analyzer#processClassFile(java.jbyco.io.InputStream)
     */
    @Override
    public void processClassFile(InputStream in) throws IOException {

        // read input stream
        ClassReader reader = new ClassReader(in);

        // create class node
        ClassNode visitor = new ClassNode(Opcodes.ASM5);

        // start the visit
        reader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // process methods
        for (Object method : visitor.methods) {
            processMethod((MethodNode) method);
        }

        // check memory
        checkMemory();

    }

    /**
     * Processes the method of the current class file.
     * <p>
     * Initializes {@link Abstractor}, adds labels that mark begin and end of the method,
     * finds active labels and calls the method that processes the instructions.
     *
     * @param method the method node representing the method of the class file
     */
    public void processMethod(MethodNode method) {

        // create abstractor
        abstractor = new Abstractor(
                method.access,
                method.name,
                method.desc,
                method.signature,
                null,
                operations,
                parameters,
                labels
        );

        // create begin and end labels
        begin = new Label();
        end = new Label();

        InsnList list = method.instructions;
        list.insert(new LabelNode(begin));
        list.add(new LabelNode(end));

        // update total size
        numbefOfInsn += list.size();

        // process instructions
        processInstructions(list);
    }

    /**
     * Processes instructions.
     *
     * @param list the list of instructions
     */
    public void processInstructions(InsnList list) {

        if (WILDCARDS == 0) {
            processSequences(list);
        } else {
            processSequencesWithWildCards(list);
        }
    }

    /**
     * Processes the list of instructions without wild cards.
     * It adds every valid sequence of instructions with a given maximal length to the graph.
     *
     * @param list the list of instructions
     */
    public void processSequences(InsnList list) {

        // process every suffix of the list
        for (int i = 0; i < list.size(); i++) {

            // get the sequence from the list
            Collection<AbstractInsnNode> nodes= getSequence(list, i, MAX_LENGTH);

            // check the sequence
            if (nodes.isEmpty()) {
                continue;
            }

            // add the sequence to the graph
            addSequence(nodes);
        }
    }

    /**
     * Processes sequences with wild cards.
     * It adds every valid sequence of the list with a given maximal length
     * and a given number of wild cards to the graph.
     *
     * @param list the list of instructions
     */
    public void processSequencesWithWildCards(InsnList list) {

        // process every suffix of the list
        for (int i = 0; i < list.size(); i++) {

            // get the basic sequence from the list
            Collection<AbstractInsnNode> nodes = getSequence(list, i, MAX_LENGTH);

            // check the length of the sequence
            if (nodes.isEmpty()) {
                continue;
            }

            // init a creator of sequences
            WildSequenceGenerator generator =
                    new WildSequenceGenerator(nodes, null, WILDCARDS);

            // iterate over all sequences with wild cards
            while (generator.hasNext()) {

                // get the sequence with nulls as wild cards
                Collection<AbstractInsnNode> sequence = generator.next();

                // add the sequence to the graph
                addSequence(sequence);
            }
        }
    }

    /**
     * Gets the sequence.
     *
     * @param list   the list of instructions
     * @param index  the index of the first instruction in a sequence
     * @param length the maximal length of the sequence
     * @return the sequence of instructions
     */
    public Collection<AbstractInsnNode> getSequence(InsnList list, int index, int length) {

        // init
        Collection<AbstractInsnNode> sequence = new ArrayList<>();

        // create a list of active nodes of the given length starting from the index
        for (int i = index, l = 0; i < list.size() && l < length; i++) {

            // get the node, list uses cache -> constant time operation
            AbstractInsnNode node = list.get(i);

            // add node to the sequence
            sequence.add(node);
            l++;
        }

        // return new sequence
        return sequence;
    }

    /**
     * Adds the sequence of instructions to the graph.
     * The sequence is abstracted, cached and added to the graph.
     *
     * @param nodes the sequence of instructions
     */
    public void addSequence(Collection<AbstractInsnNode> nodes) {

        // init
        Collection<AbstractInstruction> sequence;

        // abstract instructions in a sequence
        sequence = getAbstractedSequence(nodes);

        // cache objects in a sequence
        sequence = getCachedSequence(sequence);

        // add cached sequence to the graph
        builder.addPath(sequence);
    }

    /**
     * Gets the abstracted sequence.
     * Factories are initialized before the abstraction.
     *
     * @param sequence the sequence
     * @return the abstracted sequence
     */
    public Collection<AbstractInstruction> getAbstractedSequence(Collection<AbstractInsnNode> sequence) {

        // init, operations don't need to be initialized again
        abstractor.init();
        parameters.init();
        labels.init(begin, end);

        // process instructions in a sequence
        for (AbstractInsnNode node : sequence) {

            // add null
            if (node == null) {
                abstractor.add(null);
            }
            // add abstracted instruction
            else {
                node.accept(abstractor);
            }
        }

        // return list of abstracted instructions
        return abstractor.getList();
    }

    /**
     * Gets the cached sequence.
     *
     * @param sequence the sequence
     * @return the cached sequence
     */
    public Collection<AbstractInstruction> getCachedSequence(Collection<AbstractInstruction> sequence) {

        // init
        Collection<AbstractInstruction> cached = new ArrayList<>(sequence.size());

        // process instructions
        for (AbstractInstruction i : sequence) {

            // get cached instruction or null
            AbstractInstruction i2 = cache.getCachedInstruction(i);

            // add the instruction to the list
            cached.add(i2);
        }

        // return list of cached instructions
        return cached;
    }

    /**
     * Checks if the current memory usage is less then {@link PatternsAnalyzer#MEMORY_USAGE}}.
     * If it is higher, then it increments the pruning threshold and prunes the graph.
     * In the end, it tries to run the garbage collector.
     */
    public void checkMemory() {

        // get runtime
        Runtime runtime = Runtime.getRuntime();

        // check available memory
        int memoryUsage = (int) (
                (runtime.totalMemory() - runtime.freeMemory()) * 100.0 / runtime.maxMemory()
        );

        // prune?
        if (memoryUsage > MEMORY_USAGE) {

            // increment threshold
            pruningThreshold++;

            // prune
            builder.pruneTree(pruningThreshold);

            // try to run gc
            runtime.gc();
        }
    }

    /**
     * Finish processing.
     * The graph is pruned one more times.
     */
    public void finishProcessing() {

        // prune graph for the last time
        if (pruningThreshold > 0) {
            builder.pruneTree(pruningThreshold);
        }
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.Analyzer#writeResults(java.jbyco.io.PrintWriter)
     */
    @Override
    public void writeResults(PrintWriter out) {
        writeResults(out, ";");
    }

    /**
     * Write results.
     *
     * @param out       the output stream
     * @param delimiter the delimiter between instructions
     */
    public void writeResults(PrintWriter out, String delimiter) {

        // print total number of processed instructions
        //out.printf("%s\t%s\t%s\n", numbefOfInsn, 0, "NUMBER_OF_INSTRUCTIONS");

        // print patterns
        PatternsWriter writer = new PatternsWriter(out);
        writer.write(graph, numbefOfInsn, MIN_FREQUENCY, WILDCARDS, delimiter);
    }

    /**
     * The command line options for this tool.
     */
    enum Option implements AbstractOption {

        /**
         * The option to set the maximal length of the pattern.
         */
        MAX_LENGTH("Set the maximal length of a pattern. Default: 10.",
                "--max-length"),

        /**
         * The option to set the minimal frequency of the pattern.
         */
        MIN_FREQUENCY("Set the minimal frequency of a printed pattern. Default: 100.",
                "--min-frequency"),

        /**
         * The option to set the delimiter to separate the instructions.
         */
        DELIMITER("Set a string used to separate instruction in patterns. Default: '; '.",
                "--delimiter"),

        /**
         * The option to use the general form of operations.
         */
        GENERAL_OPERATIONS("Use a general form of operations.",
                "-o1", "--general-operations"),

        /**
         * The option to use the typed form of operations.
         */
        TYPED_OPERATIONS("Use a typed form of operations. Default option.",
                "-o2", "--typed-operations"),

        /**
         * The option to use the general form of parameters.
         */
        GENERAL_PARAMETERS("Use a general form of parameters.",
                "-p1", "--general-parameters"),

        /**
         * The option to use the numbered form of parameters.
         */
        NUMBERED_PARAMETERS("Use a numbered form of parameters.",
                "-p2", "--numbered-parameters"),

        /**
         * The option to use the full form parameters.
         */
        FULL_PARAMETERS("Use a full form of parameters. Default option.",
                "-p3", "--full-parameters"),

        /**
         * The option to set the number of wildcards.
         */
        WILDCARDS("Set the number of wildcards in a pattern. Default: 0.",
                "-w", "--wildcards"),

        /**
         * The option to do not show the progress.
         */
        NO_PROGRESS("Don't show information about progress.",
                "--no-progress"),

        /**
         * The option for help.
         */
        HELP("Show this message.",
                "-h", "--help");

        String description;
        String[] names;

        /**
         * Instantiates a new option.
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
            return description;
        }

        @Override
        public String[] getNames() {
            return names;
        }

    }

    /**
     * The command line options.
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
