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

public class PatternsAnalyzer implements Analyzer {
	
	// number of wild cards in a pattern
	final int WILDCARDS;
	
	// max length of patterns
	final int MAX_LENGTH;
	
	// minimal frequency of printed patterns
	final int MIN_FREQUENCY;
		
	// max memory usage in percents
	final int MEMORY_USAGE;
	
	// graph
	Tree<AbstractInstruction> graph;
			
	// graph builder
	TreeBuilder<AbstractInstruction> builder;
	
	// threshold for tree pruning
	int pruningThreshold = 0;
	
	// instruction abstractor
	Abstractor abstractor;
	
	// instruction cache
	Cache cache;
	
	// labels for begin and end of the method
	Label begin = null;
	Label end = null;
	
	// number of processed sequenced
	long numseq = 0;
	
	// factories
	AbstractOperationFactory operations;
	AbstractParameterFactory parameters;
	AbstractLabelFactory labels;
	
	// active labels of the current method
	ActiveLabelsFinder activeLabels;
			
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
	
	public Tree<AbstractInstruction> getGraph() {
		return graph;
	}
	
	@Override
	public void processFile(CommonFile file) {
		
		try {
			
			// get input stream
			InputStream in = file.getInputStream();
			
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

			// close stream
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
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
	
	public void processInstructions(InsnList list) {
		
		if (WILDCARDS == 0) {
			processSuffixes(list);
		}
		else {
			processSuffixesWithWildCards(list);
		}
	}
	
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
	
	
	public boolean isFirstNode(AbstractInsnNode node) {
		// any active node
		return isActiveNode(node);
	}
	
	protected boolean isActiveNode(AbstractInsnNode node) {
		
		// only LabelNode with active label is active
		if (node instanceof LabelNode) {
			return activeLabels.isActive(((LabelNode)node).getLabel());
		}
		
		// or any other node
		return true;
	}
	
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
	
	public void finishProcessing() {

		// prune graph for the last time
		if (pruningThreshold > 0) {
			builder.pruneTree(pruningThreshold);
		}
	}

	@Override
	public void writeResults(PrintWriter out) {
		writeResults(out, ";");		
	}
		
	public void writeResults(PrintWriter out, String delimiter) {
		
		// print total number of sequences
		out.printf("%s\t%s\t%s\n", numseq, 0, "TOTAL");
		
		// print patterns
		PatternsWriter writer = new PatternsWriter(out);
		writer.write(graph, delimiter, MIN_FREQUENCY, WILDCARDS);		
	}
	
	///////////////////////////////////////////////////////////////// MAIN
	
	enum Option implements AbstractOption {
		
		MAX_LENGTH			("Set the maximal length of a pattern. Default: 10.",
							 "--max-length"),
		MIN_FREQUENCY		("Set the minimal frequency of a printed pattern. Default: 100.",
							 "--min-frequency"),
		DELIMITER			("Set a string used to separate instruction in patterns. Default: '; '.",
							 "--delimiter"),
		GENERAL_OPERATIONS ("Use a general form of operations.",
							 "-o1", "--general-operations"),
		TYPED_OPERATIONS 	("Use a typed form of operations. Default option.",
							 "-o2", "--typed-operations"),
		GENERAL_PARAMETERS	("Use a general form of parameters.",
							 "-p1", "--general-parameters"),
		NUMBERED_PARAMETERS	("Use a numbered form of parameters.",
							 "-p2", "--numbered-parameters"),
		FULL_PARAMETERS		("Use a full form of parameters. Default option.",
							 "-p3", "--full-parameters"),
		WILDCARDS			("Set the number of wildcards in a pattern. Default: 0.",
							"-w", "--wildcards"),
		NO_PROGRESS			("Don't show information about progress.",
							"--no-progress"),
		HELP				("Show this message.", 
							 "-h", "--help");

		String description;
		String[] names;
		
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
	
	static class Options extends AbstractOptions {

		@Override
		public AbstractOption[] all() {
			return Option.values();
		}
		
	}
	
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
					analyzer.processFile(file);
					
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
