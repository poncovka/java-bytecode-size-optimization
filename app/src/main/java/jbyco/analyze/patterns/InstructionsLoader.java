package jbyco.analyze.patterns;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.Instruction;

import jbyco.analyze.patterns.graph.GraphBuilder;
import jbyco.analyze.patterns.graph.SuffixTree;

public class InstructionsLoader {
	
	// max length of instruction sequencies
	static final int MAXLENGTH = 10;
	
	// queue of node items
	Queue<Object> queue;
	
	// graph
	SuffixTree graph;
	
	// graph builder
	GraphBuilder builder;
	
	// counter
	int counter;
	
	// map of items
	Map<Object, Object> items;
	
	// transformer
	InstructionToString transformer;
	
	public InstructionsLoader(SuffixTree graph) {
		this.graph = graph;
		this.builder = new GraphBuilder(graph);
		this.queue = new LinkedList<>();
		this.items = new HashMap<>();
		this.transformer = new InstructionToString(0, 0, 0);
	}
	
	public void init(ConstantPool cp) {
		counter = 0;
		queue.clear();
		transformer.setConstantPool(cp);
	}
	
	public Object getItem(Instruction i) {
		
		// get string 
		short opcode = i.getOpcode();
		
		// get item
		Object item = items.get(opcode);
		
		// or create one
		if (item == null) {
			item = new Item(opcode);
			items.put(opcode, item);
		}
		
		// return item
		return item;
	}
	
	public void loadInstruction(Instruction i) {
		
		// inqueue item
		queue.add(getItem(i));
		
		// set counter
		counter++;
		
		// load sequence
		if(counter >= MAXLENGTH) {
			loadSequence();
			queue.remove();			
		}	
	}
	
	public void loadSequence() {
		
		// start path
		builder.startPath();
		
		// add nodes
		for(Object item : queue) {
			builder.addNextNode(item);
		}
		
		// finish path
		builder.finishPath();
	}
	
	public void finish() {
		
		while(!queue.isEmpty()) {
			loadSequence();
			queue.remove();
		}
		
	}
	
	class Item {
		short opcode;
		
		public Item(short opcode) {
			this.opcode = opcode;
		}
		
		@Override
		public String toString() {
			return Constants.OPCODE_NAMES[opcode];
		}
	}

}
