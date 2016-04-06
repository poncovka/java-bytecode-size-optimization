package jbyco.analyze.patterns;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.objectweb.asm.ClassReader;
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
import jbyco.analyze.patterns.labels.NumberedLabelFactory;
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
import jbyco.io.BytecodeFiles;
import jbyco.io.files.BytecodeFile;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

public class PatternsAnalyzer implements Analyzer {
		
	// max length of patterns
	static int MAX_LENGTH = 10;
	
	// minimal frequency of printed patterns
	static int MIN_FREQUENCY = 100;
	
	// delimiter used to separate the instructions in patterns
	static String DELIMITER = ";";
	
	// number of wild cards in a pattern
	static int WILDCARDS = 0;
	
	// max memory usage in percents
	static int MEMORY_USAGE = 70;
	
	// graph
	Tree graph;
			
	// graph builder
	TreeBuilder builder;
	
	// threshold for tree pruning
	int pruningThreshold = 0;
	
	// instruction abstractor
	Abstractor abstractor;
	
	// instruction cache
	Cache cache;
	
	// factories
	AbstractOperationFactory operations;
	AbstractParameterFactory parameters;
	AbstractLabelFactory labels;
	
	// active labels of the current method
	ActiveLabelsFinder activeLabels;
		
	public PatternsAnalyzer (
			AbstractOperationFactory operations, 
			AbstractParameterFactory parameters, 
			AbstractLabelFactory labels
			) {
		
		this.operations = operations;
		this.parameters = parameters;
		this.labels = labels;
		
		this.graph = new Tree();
		this.builder = new TreeBuilder(graph);
		this.cache = new Cache();
	}
	
	public Tree getGraph() {
		return graph;
	}
	
	@Override
	public void processFile(BytecodeFile file) {
		
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
		
		// find all active labels
		activeLabels = new ActiveLabelsFinder();
		method.accept(activeLabels);
		
		// process instructions
		processInstructions(method.instructions);	
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
			if (!WildSequenceGenerator.check(nodes, WILDCARDS)) {
				continue;
			}
			
			// init a creator of suffixes
			WildSequenceGenerator<AbstractInsnNode> creator = 
					new WildSequenceGenerator<>(
							nodes, 
							null, 
							WILDCARDS);
			
			// iterate over all suffixes with wild cards
			while(creator.hasNext()) {
				
				// get the suffix with nulls as wild cards
				Collection<AbstractInsnNode> suffix = creator.next();
				
				// add the suffix to the graph
				addSuffix(suffix);
			}	
		}
	}
	
	public Collection<AbstractInsnNode> getSuffix(InsnList list, int index, int length) {

		// init
		Collection<AbstractInsnNode> suffix = new ArrayList<>();
		
		// create a list of active nodes of the given length starting from the index 
		for (int i = index; i < list.size() && (i - index + 1) <= length; i++) {
			
			// get the node, list uses cache -> constant time operation
			AbstractInsnNode node = list.get(i);
			
			// add the node if it is active
			if (isActive(node)) {
				suffix.add(node);
			}
		}
				
		return suffix;
	}
		
	protected boolean isActive(AbstractInsnNode node) {
		return     !(node instanceof LabelNode) 
				||  (activeLabels.isActive(((LabelNode)node).getLabel()));
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
	}
	
	public Collection<AbstractInstruction> getAbstractedSuffix(Collection<AbstractInsnNode> suffix) {
		
		// init, operations don't need to be initialized again
		abstractor.init();
		parameters.init();
		labels.init();
				
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
			AbstractInstruction i2 = (i != null) ? cache.getCachedInstruction(i) : null;
						
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
		int memoryUsage = (int)((runtime.totalMemory() - runtime.freeMemory()) * 100.0 / runtime.maxMemory());
			
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
		PatternsWriter writer = new PatternsWriter(out);
		writer.write(graph, DELIMITER, MIN_FREQUENCY, WILDCARDS);		
	}
		
	
	///////////////////////////////////////////////////////////////// MAIN
	
	enum Option implements AbstractOption {
		
		MAX_LENGTH			("Set the maximal length of a pattern. Default: 10.",
							 "--max-length"),
		MIN_FREQUENCY		("Set the minimal frequency of a printed pattern. Default: 100.",
							 "--min-frequency"),
		DELIMITER			("Set a string used to separate instruction in patterns. Default: ';'.",
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
	
	public static void main(String[] args) {
		
		// get options and map of options
		Options options = new Options();
		
		// show progress?
		boolean showProgress = true;
		
		// set default values
		MAX_LENGTH = 10;
		MIN_FREQUENCY = 100;
		DELIMITER = ";";
		
		AbstractOperationFactory operations = new TypedOperationFactory();
		AbstractParameterFactory parameters = new FullParameterFactory();
		AbstractLabelFactory labels = new NumberedLabelFactory();
		
		// process parameters
		
		int index = 0;
		for (;index < args.length; index++) {
			
			// get option
			String arg = args[index];
			Option option = (Option)options.getOption(arg);
			
			// help
			if (option == Option.HELP) {
				options.help();
				return;
			}
			
			// no progress
			else if (option == Option.NO_PROGRESS) {
				showProgress = false;
			}
			
			// set max length
			else if (option == Option.MAX_LENGTH) {
				
				if (++index < args.length) {
					MAX_LENGTH = Integer.parseUnsignedInt(args[index]);
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set min frequency
			else if (option == Option.MIN_FREQUENCY) {
				
				if (++index < args.length) {
					MIN_FREQUENCY = Integer.parseUnsignedInt(args[index]);
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set delimiter
			else if (option == Option.DELIMITER) {
				
				if (++index < args.length) {
					DELIMITER = args[index];
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set wildcards
			else if (option == Option.WILDCARDS) {
				
				if (++index < args.length) {
					WILDCARDS = Integer.parseUnsignedInt(args[index]);
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set type of operations
			else if (option == Option.GENERAL_OPERATIONS) {
				operations = new GeneralOperationFactory();
			}
			else if (option == Option.TYPED_OPERATIONS) {
				operations = new TypedOperationFactory();
			}
			
			// set type of parameters
			else if (option == Option.GENERAL_PARAMETERS) {
				parameters = new GeneralParameterFactory();
			}
			else if (option == Option.NUMBERED_PARAMETERS) {
				parameters = new NumberedParameterFactory();
			}
			else if (option == Option.FULL_PARAMETERS) {
				parameters = new FullParameterFactory();
			}
			
			// files
			else {
				break;
			}
		}
		
		
		// count files 
		int counter = 0;
		int total = 0;
		
		if (showProgress) {
			
			for (int i = index; i < args.length; i++) {
							
				// get files on the path
				BytecodeFiles files = new BytecodeFiles(args[i]);
							
				// process files on the path
				for (BytecodeFile file : files) {
					total++;
				}
			}
		}
		
		// init analyzer
		PatternsAnalyzer analyzer = new PatternsAnalyzer(operations, parameters, labels);
		
		// analyze files
		for (int i = index; i < args.length; i++) {
			
			// get files on the path
			BytecodeFiles files = new BytecodeFiles(args[i]);
			
			// process files on the path
			for (BytecodeFile file : files) {
								
				// analyze file
				analyzer.processFile(file);
				
				// show progress
				if (showProgress) {
					counter++;
					
					System.err.printf("Processed %s%%, pruned %d times.\r",
							Utils.intDivToString(counter * 100, total),
							analyzer.pruningThreshold);
				}
			}
		}
		
		// last prune
		analyzer.finishProcessing();
		
		// print results
		PrintWriter writer = new PrintWriter(System.out, true);
		analyzer.writeResults(writer);
		writer.close();
		
		// show progress
		System.err.printf("Processed 100%%, pruned %d times.\n", analyzer.pruningThreshold);
		
	}

}
