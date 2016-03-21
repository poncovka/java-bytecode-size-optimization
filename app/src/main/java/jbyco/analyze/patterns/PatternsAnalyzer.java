package jbyco.analyze.patterns;

import java.io.IOException;
import java.io.InputStream;
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
import jbyco.analyze.patterns.graph.GraphBuilder;
import jbyco.analyze.patterns.graph.Node;
import jbyco.analyze.patterns.graph.SuffixTree;
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
import jbyco.io.BytecodeFiles;
import jbyco.io.file.BytecodeFile;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;

public class PatternsAnalyzer implements Analyzer {
		
	// max length of patterns
	static int MAX_LENGTH = 10;
	
	// minimal frequency of printed patterns
	static int MIN_FREQUENCY = 100;
	
	// delimiter used to separate the instructions in patterns
	static String DELIMITER = ";";
			
	// graph
	SuffixTree graph;
			
	// graph builder
	GraphBuilder builder;
	
	// instruction cache
	Cache cache;
	
	// factories
	AbstractOperationFactory operations;
	AbstractParameterFactory parameters;
	AbstractLabelFactory labels;
		
	public PatternsAnalyzer (
			AbstractOperationFactory operations, 
			AbstractParameterFactory parameters, 
			AbstractLabelFactory labels
			) {
		
		this.operations = operations;
		this.parameters = parameters;
		this.labels = labels;
		
		this.graph = new SuffixTree();
		this.builder = new GraphBuilder(graph);
		this.cache = new Cache();
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void processMethod(MethodNode method) {
						
		// create abstractor
		Abstractor abstractor = 
				new Abstractor(
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
		ActiveLabelsFinder finder = new ActiveLabelsFinder();
		method.accept(finder);
		
		// get list of instructions
		InsnList list = method.instructions;
		
		// visit every suffix
		AbstractInsnNode start = list.getFirst();
		while (start != null) {
			
			// TODO restart after RETURN or JSR!!!
			
			// visit every instruction of the suffix
			AbstractInsnNode node = start;
			int visited = 0;
			
			while (node != null && visited < MAX_LENGTH) {
				
				// process the node if it is active
				if (isActive(node, finder)) {
					
					// get abstracted instruction
					node.accept(abstractor);
					
					// end of the visit
					visited++;
				}
				
				// get next node
				node = node.getNext();
			}
			
			// add cached instructions to the graph
			builder.addPath(useCache(abstractor.getList()));
			
			// start from next node
			start = start.getNext();
			
			// clear abstractor list
			abstractor.getList().clear();
			
			// restart factories
			operations.restart();
			parameters.restart();
			labels.restart();
		}
	}
	
	protected boolean isActive(AbstractInsnNode node, ActiveLabelsFinder finder) {
		return     !(node instanceof LabelNode) 
				||  (finder.isActive(((LabelNode)node).getLabel()));
	}
	
	protected Collection<?> useCache(Collection<AbstractInstruction> l) {
		
		// create new list
		Collection<AbstractInstruction> l2 = new ArrayList<>(l.size());
		
		// cache all instructions
		for (AbstractInstruction instruction : l) {
			l2.add(cache.getCachedInstruction(instruction));
		}
		
		// return created list
		return l2;
	}
	
	public void finish() {
		builder.pruneGraph();
	}

	@Override
	public void print() {
		
		// print graph
		//System.out.println("Graph:");
		//graph.print(System.out);		
		//System.out.println();
				
		// print patterns
		System.out.println("Patterns:");
		PatternsPrinter printer = new PatternsPrinter();
		printer.print(graph, DELIMITER, MIN_FREQUENCY);		
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
		
		// set default values
		MAX_LENGTH = 10;
		MIN_FREQUENCY = 100;
		DELIMITER = ";";
		
		AbstractOperationFactory operations = new TypedOperationFactory();
		AbstractParameterFactory parameters = new FullParameterFactory();
		AbstractLabelFactory labels = new NumberedLabelFactory();
		
		int i = 0;
		for (;i < args.length; i++) {
			
			// get option
			String arg = args[i];
			Option option = (Option)options.getOption(arg);
			
			// help
			if (option == Option.HELP) {
				options.help();
				return;
			}
			
			// set max length
			else if (option == Option.MAX_LENGTH) {
				
				if (++i < args.length) {
					MAX_LENGTH = Integer.parseUnsignedInt(args[i]);
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set min frequency
			else if (option == Option.MIN_FREQUENCY) {
				
				if (++i < args.length) {
					MIN_FREQUENCY = Integer.parseUnsignedInt(args[i]);
				}
				else {
					throw new IllegalArgumentException("Missing argument for " + arg);
				}
			}
			
			// set delimiter
			else if (option == Option.DELIMITER) {
				
				if (++i < args.length) {
					DELIMITER = args[i];
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
		
		// init analyzer
		Analyzer analyzer = new PatternsAnalyzer(operations, parameters, labels);
		
		// analyze files
		for (; i < args.length; i++) {
			
			// get files on the path
			BytecodeFiles files = new BytecodeFiles(args[i]);
			
			// process files on the path
			for (BytecodeFile file : files) {
				analyzer.processFile(file);
			}
		}
		
		// last prune
		analyzer.finish();
		
		// print results
		analyzer.print();
	}

}
