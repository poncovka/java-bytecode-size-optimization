package jbyco.analyze.patterns;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import jbyco.analyze.Analyzer;
import jbyco.analyze.patterns.graph.GraphBuilder;
import jbyco.analyze.patterns.graph.SuffixTree;
import jbyco.analyze.patterns.instr.Abstractor;
import jbyco.analyze.patterns.instr.ActiveLabelsFinder;
import jbyco.io.PatternsPrinter;
import jbyco.io.file.BytecodeFile;

public class PatternsAnalyzer implements Analyzer {
		
	// max length of instruction sequences
	static final int MAXLENGTH = 10;
			
	// graph
	SuffixTree graph;
			
	// graph builder
	GraphBuilder builder;
	
	// graph abstractor
	Abstractor abstractor;
		
	public PatternsAnalyzer() {
		graph = new SuffixTree();
		builder = new GraphBuilder(graph);
		abstractor = new Abstractor();
	}
	
	@Override
	public void processFile(BytecodeFile file) {
		
		try {
			
			// get input stream
			InputStream in = file.getInputStream();
			
			// read input stream
			ClassReader reader = new ClassReader(in);
			
			// create method node and process it
			ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5) {
								
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					
					MethodNode method = new MethodNode(access, name, desc, signature, exceptions) {
						
						@Override
						public void visitEnd() {
							super.visitEnd();
							processMethod(this);
						}
					};
					
					return method;
				}
			};
			
			// process the file
			reader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void processMethod(MethodNode method) {
				
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
			
			// add instructions to the graph
			builder.addPath(abstractor.getList());
			
			// start from next node
			start = start.getNext();
			
			// clear the instruction list
			abstractor.clear();
		}
	}
	
	protected boolean isActive(AbstractInsnNode node, ActiveLabelsFinder finder) {
		return     !(node instanceof LabelNode) 
				||  (finder.isActive(((LabelNode)node).getLabel()));
	}
	

	@Override
	public void print() {
		
		// print graph
		System.out.println("Graph:");
		graph.print(System.out);
				
		System.out.println();
				
		// print patterns
		System.out.println("Patterns:");
		PatternsPrinter printer = new PatternsPrinter();
		printer.print(graph, ";", 100);	
		
	}

}
