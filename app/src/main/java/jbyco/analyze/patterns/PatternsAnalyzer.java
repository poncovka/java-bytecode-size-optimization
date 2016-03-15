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
import jbyco.analyze.patterns.graph.SuffixTree;
import jbyco.analyze.patterns.instr.AbstractInstruction;
import jbyco.analyze.patterns.instr.Abstractor;
import jbyco.analyze.patterns.instr.InstructionCache;
import jbyco.analyze.patterns.instr.label.AbstractLabelFactory;
import jbyco.analyze.patterns.instr.label.ActiveLabelsFinder;
import jbyco.analyze.patterns.instr.label.NumberedLabelFactory;
import jbyco.analyze.patterns.instr.operation.AbstractOperationFactory;
import jbyco.analyze.patterns.instr.operation.TypedOperationFactory;
import jbyco.analyze.patterns.instr.param.AbstractParameterFactory;
import jbyco.analyze.patterns.instr.param.FullParameterFactory;
import jbyco.io.BytecodeFiles;
import jbyco.io.PatternsPrinter;
import jbyco.io.file.BytecodeFile;

public class PatternsAnalyzer implements Analyzer {
		
	// max length of instruction sequences
	static final int MAXLENGTH = 10;
			
	// graph
	SuffixTree graph;
			
	// graph builder
	GraphBuilder builder;
	
	// instruction cache
	InstructionCache cache;
	
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
		this.cache = new InstructionCache();
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
			
			while (node != null && visited < MAXLENGTH) {
				
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
	
	protected Collection<AbstractInstruction> useCache(Collection<AbstractInstruction> list) {
		
		// create new list
		Collection<AbstractInstruction> list2 = new ArrayList<>(list.size());
		
		// cache all instructions
		for (AbstractInstruction i : list) {
			list2.add(cache.get(i));
		}
		
		// return created list
		return list2;
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
		printer.print(graph, ";", 100);	
		
	}
	
	public static void main(String[] args) {
		
		AbstractOperationFactory operations;
		AbstractParameterFactory parameters;
		AbstractLabelFactory labels;
		
		//operations = new GeneralOperationFactory();
		operations = new TypedOperationFactory();
		
		//parameters = new GeneralParameterFactory();
		//parameters = new NumberedParameterFactory();
		parameters = new FullParameterFactory();
		
		labels = new NumberedLabelFactory();
		
		// init analyzer
		Analyzer analyzer = new PatternsAnalyzer(operations, parameters, labels);
		
		// process files
		for (String path : args) {
			
			BytecodeFiles files = new BytecodeFiles(path);
			
			for (BytecodeFile file : files) {
				analyzer.processFile(file);
			}
		}
		
		// print results
		analyzer.print();
	}

}
