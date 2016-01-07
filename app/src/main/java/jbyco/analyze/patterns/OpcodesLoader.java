package jbyco.analyze.patterns;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.Instruction;

import jbyco.analyze.patterns.graph.GraphBuilder;
import jbyco.analyze.patterns.graph.SuffixGraph;

public class OpcodesLoader implements InstructionsLoader {
	
	// max length of instruction sequencies
	static final int MAXLENGTH = 50;
	
	// array with a sequence
	Queue<String> queue;
	
	// graph
	SuffixGraph graph;
	
	// graph builder
	GraphBuilder builder;
	
	// counter
	int counter;
	
	// map of items
	Map<Short, String> items;
	
	public OpcodesLoader(SuffixGraph graph) {
		this.graph = graph;
		this.builder = new GraphBuilder(graph);
		this.queue = new LinkedList<>();
		this.items = new HashMap<>();
	}
	
	public void init() {
		counter = 0;
		queue.clear();
	}
	
	public String getItem(Instruction i) {
		
		// get opcode
		short opcode = i.getOpcode();
		
		// get item
		String item = items.get(opcode);
		
		// or create one
		if (item == null) {
			item = Constants.OPCODE_NAMES[opcode];
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
		for(String item : queue) {
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
}
