package jbyco.analyze.patterns;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import jbyco.analyze.Analyzer;
import jbyco.analyze.patterns.instructions.AbstractInstruction;
import jbyco.analyze.patterns.instructions.Abstractor;
import jbyco.analyze.patterns.instructions.Cache;
import jbyco.analyze.patterns.labels.AbstractLabelFactory;
import jbyco.analyze.patterns.labels.ActiveLabelsFinder;
import jbyco.analyze.patterns.labels.RelativeLabelFactory;
import jbyco.analyze.patterns.operations.AbstractOperationFactory;
import jbyco.analyze.patterns.operations.GeneralOperationFactory;
import jbyco.analyze.patterns.operations.TypedOperationFactory;
import jbyco.analyze.patterns.parameters.AbstractParameterFactory;
import jbyco.analyze.patterns.parameters.FullParameterFactory;
import jbyco.analyze.patterns.parameters.GeneralParameterFactory;
import jbyco.analyze.patterns.parameters.NumberedParameterFactory;
import jbyco.analyze.patterns.tree.Tree;
import jbyco.analyze.patterns.tree.TreeBuilder;
import jbyco.analyze.patterns.wildcards.WildSequenceGenerator;
import jbyco.io.BytecodeFilesCounter;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

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
	
	/** The number of wild cards in a sequence. */
	final int WILDCARDS;
	
	/** The maximal length of the sequence. */
	final int MAX_LENGTH;
	
	/** The minimal frequency of the printed sequence. */
	final int MIN_FREQUENCY;
		
	/** The maximal memory usage in percents. */
	final int MEMORY_USAGE;
	
	/** The tree of sequences. */
	Tree<AbstractInstruction> graph;
			
	/** The tree builder. */
	TreeBuilder<AbstractInstruction> builder;
		
	/** The instructions' abstractor. */
	Abstractor abstractor;
	
	/** The instructions' cache. */
	Cache cache;
	
	/** The label marks the start of the current method. */
	Label begin = null;
	
	/** The label marks the end of the current method. */
	Label end = null;
	
	/** The current pruning threshold. */
	int pruningThreshold = 0;
	
	/** The number of processed sequenced. */
	long numseq = 0;
	
	/** The factory for operations' abstraction. */
	AbstractOperationFactory operations;
	
	/** The factory for parameters' abstraction. */
	AbstractParameterFactory parameters;
	
	/** The factory for labels' abstraction. */
	AbstractLabelFactory labels;
	
	/** The class for finding active labels. */
	ActiveLabelsFinder activeLabels;
			
	/**
	 * Instantiates a new patterns analyzer.
	 *
	 * @param operations 	the operations' factory
	 * @param parameters 	the parameters' factory
	 * @param labels 		the labels' factory
	 * @param wildcards 	the number of wild cards
	 * @param maxLength 	the maximal length of sequences
	 * @param minFrequency 	the minimal frequency of printed sequences
	 * @param memoryUsage 	the maximal memory usage
	 */
	public PatternsAnalyzer (
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
	 * Gets the representation of instruction sequences.
	 *
	 * @return the tree of instruction sequences
	 */
	public Tree<AbstractInstruction> getGraph() {
		return graph;
	}
	
	
	/* (non-Javadoc)
	 * @see jbyco.analyze.Analyzer#processClassFile(java.io.InputStream)
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
			processMethod((MethodNode)method);
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
		
		// find all active labels
		activeLabels = new ActiveLabelsFinder();
		method.accept(activeLabels);
		activeLabels.add(begin);
		activeLabels.add(end);
		
		// process instructions
		InsnList list = method.instructions;
		list.insert(new LabelNode(begin));
		list.add(new LabelNode(end));
		processInstructions(list);	
	}
	
	/**
	 * Processes instructions.
	 *
	 * @param list the list of instructions
	 */
	public void processInstructions(InsnList list) {
		
		if (WILDCARDS == 0) {
			processSuffixes(list);
		}
		else {
			processSuffixesWithWildCards(list);
		}
	}
	
	/**
	 * Processes the list of instructions without wild cards.
	 * It adds every valid suffix of the list with a given maximal length to the graph.
	 *
	 * @param list the list of instructions
	 */
	public void processSuffixes(InsnList list) {
		
		// process every suffix of the list
		for (int i = 0; i < list.size(); i++) {
			
			// get the suffix from the list
			Collection<AbstractInsnNode> suffix = getSuffix(list, i, MAX_LENGTH);
			
			// check suffix
			if (suffix == null) {
				continue;
			}	
			
			// add the suffix to the graph
			addSuffix(suffix);
		}
	}
	
	/**
	 * Processes suffixes with wild cards.
	 * It adds every valid suffix of the list with a given maximal length 
	 * and a given number of wild cards to the graph. 
	 *
	 * @param list the list of instructions
	 */
	public void processSuffixesWithWildCards(InsnList list) {
		
		// process every suffix of the list
		for (int i = 0; i < list.size(); i++) {
			
			// get the basic suffix from the list
			Collection<AbstractInsnNode> nodes = getSuffix(list, i, MAX_LENGTH);
			
			// check the length of the suffix
			if (nodes == null) {
				continue;
			}
			
			// init a creator of suffixes
			WildSequenceGenerator generator = 
					new WildSequenceGenerator(nodes, null, WILDCARDS);
			
			// iterate over all suffixes with wild cards
			while(generator.hasNext()) {
				
				// get the suffix with nulls as wild cards
				Collection<AbstractInsnNode> suffix = generator.next();
				
				// add the suffix to the graph
				addSuffix(suffix);
			}	
		}
	}
	
	/**
	 * Gets the suffix.
	 *
	 * @param list 		the list of instructions
	 * @param index 	the index of the first instruction in a suffix
	 * @param length 	the maximal length of the suffix
	 * @return the suffix of instructions
	 */
	public Collection<AbstractInsnNode> getSuffix(InsnList list, int index, int length) {
		
		// init
		Collection<AbstractInsnNode> suffix = new ArrayList<>();
		
		// create a list of active nodes of the given length starting from the index 
		for (int i = index, l = 0; i < list.size() && l < length; i++) {
			
			// get the node, list uses cache -> constant time operation
			AbstractInsnNode node = list.get(i);
			
			// check first node
			if (i == index && !isFirstNode(node)) {
				return null;
			}
			
			// ignore non active node
			if (!isActiveNode(node)) {
				continue;
			}
			
			// add node to the suffix
			suffix.add(node);
			l++;
		}
		
		// return new suffix
		return suffix;
	}
	
	
	/**
	 * Checks if is node can be the first node in a suffix.
	 *
	 * @param node the node
	 * @return true, if the node can be the first node in a suffix
	 */
	public boolean isFirstNode(AbstractInsnNode node) {
		// any active node
		return isActiveNode(node);
	}
	
	/**
	 * Checks if is the node is active.
	 * Active node is an active label node or any other node.
	 *
	 * @param node the node
	 * @return true, if the node is active
	 */
	protected boolean isActiveNode(AbstractInsnNode node) {
		
		// only LabelNode with active label is active
		if (node instanceof LabelNode) {
			return activeLabels.isActive(((LabelNode)node).getLabel());
		}
		
		// or any other node
		return true;
	}
	
	/**
	 * Adds the suffix of instructions to the graph.
	 * The suffix is abstracted, cached and added to the graph.
	 *
	 * @param nodes the suffix of instructions
	 */
	public void addSuffix(Collection<AbstractInsnNode> nodes) {
		
		// init
		Collection<AbstractInstruction> suffix;
		
		// abstract instructions in a suffix
		suffix = getAbstractedSuffix(nodes);
		
		// cache objects in a suffix
		suffix = getCachedSuffix(suffix);
		
		// add cached suffix to the graph
		builder.addPath(suffix);
		
		// update counter
		numseq += nodes.size();
	}
	
	/**
	 * Gets the abstracted suffix.
	 * Factories are initialized before the abstraction.
	 *
	 * @param suffix the suffix
	 * @return the abstracted suffix
	 */
	public Collection<AbstractInstruction> getAbstractedSuffix(Collection<AbstractInsnNode> suffix) {
		
		// init, operations don't need to be initialized again
		abstractor.init();
		parameters.init();
		labels.init(begin, end);
				
		// process instructions in a suffix
		for (AbstractInsnNode node : suffix) {
			
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
	 * Gets the cached suffix.
	 *
	 * @param suffix the suffix
	 * @return the cached suffix
	 */
	public Collection<AbstractInstruction> getCachedSuffix(Collection<AbstractInstruction> suffix) {
		
		// init
		Collection<AbstractInstruction> cached = new ArrayList<>(suffix.size());
		
		// process instructions
		for (AbstractInstruction i : suffix) {
			
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
	 * @see jbyco.analyze.Analyzer#writeResults(java.io.PrintWriter)
	 */
	@Override
	public void writeResults(PrintWriter out) {
		writeResults(out, ";");		
	}
		
	/**
	 * Write results.
	 *
	 * @param out 			the output stream
	 * @param delimiter 	the delimiter between instructions
	 */
	public void writeResults(PrintWriter out, String delimiter) {
		
		// print total number of sequences
		out.printf("%s\t%s\t%s\n", numseq, 0, "TOTAL");
		
		// print patterns
		PatternsWriter writer = new PatternsWriter(out);
		writer.write(graph, delimiter, MIN_FREQUENCY, WILDCARDS);		
	}
	
	///////////////////////////////////////////////////////////////// MAIN
	
	/**
	 * The command line options for this tool.
	 */
	enum Option implements AbstractOption {
		
		/** The option to set the maximal length of the pattern. */
		MAX_LENGTH			("Set the maximal length of a pattern. Default: 10.",
							 "--max-length"),
		
		/** The option to set the minimal frequency of the pattern. */
		MIN_FREQUENCY		("Set the minimal frequency of a printed pattern. Default: 100.",
							 "--min-frequency"),
		
		/** The option to set the delimiter to separate the instructions. */
		DELIMITER			("Set a string used to separate instruction in patterns. Default: '; '.",
							 "--delimiter"),
		
		/** The option to use the general form of operations. */
		GENERAL_OPERATIONS ("Use a general form of operations.",
							 "-o1", "--general-operations"),
		
		/** The option to use the typed form of operations. */
		TYPED_OPERATIONS 	("Use a typed form of operations. Default option.",
							 "-o2", "--typed-operations"),
		
		/** The option to use the general form of parameters. */
		GENERAL_PARAMETERS	("Use a general form of parameters.",
							 "-p1", "--general-parameters"),
		
		/** The option to use the numbered form of parameters. */
		NUMBERED_PARAMETERS	("Use a numbered form of parameters.",
							 "-p2", "--numbered-parameters"),
		
		/** The option to use the full form parameters. */
		FULL_PARAMETERS		("Use a full form of parameters. Default option.",
							 "-p3", "--full-parameters"),
		
		/** The option to set the number of wildcards. */
		WILDCARDS			("Set the number of wildcards in a pattern. Default: 0.",
							"-w", "--wildcards"),
		
		/** The option to do not show the progress. */
		NO_PROGRESS			("Don't show information about progress.",
							"--no-progress"),
		
		/** The option for help. */
		HELP				("Show this message.", 
							 "-h", "--help");

		String description;
		String[] names;
		
		/**
		 * Instantiates a new option.
		 *
		 * @param description the description of the option
		 * @param names the names of the option
		 */
		private Option(String description, String ...names) {
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
			Option option = (Option)options.getOption(arg);
			
			// get paths to input files
			if (option == null) {
				
				while (index < args.length) {
					paths.add(Paths.get(args[index++]));
				}
				
				continue;
			}
			
			// process parameters
			switch(option) {	
				case HELP:				options.help();
										return;
				case NO_PROGRESS:		progress = false;
										break;
				case DELIMITER:			delimiter = args[++index];
										break;
				case MAX_LENGTH:		maxLength = Integer.parseUnsignedInt(args[++index]);
										break;
				case MIN_FREQUENCY:		minFrequency = Integer.parseUnsignedInt(args[++index]);
										break;
				case WILDCARDS:			wildcards = Integer.parseUnsignedInt(args[++index]);
										break;
				case GENERAL_OPERATIONS:operations = new GeneralOperationFactory();
										break;
				case TYPED_OPERATIONS:	operations = new TypedOperationFactory();
										break;
				case GENERAL_PARAMETERS:parameters = new GeneralParameterFactory();
										break;
				case NUMBERED_PARAMETERS:parameters = new NumberedParameterFactory();
										break;
				case FULL_PARAMETERS:	parameters = new FullParameterFactory();
										break;
			}
		}
		
		// count files 
		int counter = 0;
		int total = (new BytecodeFilesCounter()).countAll(paths);;
		
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
	
			// analyze files
			for (Path path : paths) {
				
				// process files on the path
				for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {
					
					// analyze file
					InputStream in = file.getInputStream();
					analyzer.processClassFile(in);
					in.close();
					
					// show progress
					if (progress) {
						System.err.printf(
								"Processed %s%%, pruned %d times.\r",
								Utils.intDivToString(++counter * 100, total),
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

}
