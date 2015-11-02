package jbyco.stat;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
	
	public int totalBytes;
	public int totalClasses;
	public int totalMethods;
	public int totalClassVars;
	public int totalLocalVars;
	public int totalInstructions;
	public int totalSizeOfStrings;
	public int totalMaxStack;
	public int totalMaxLocals;
	public int maxStack;
	public int maxLocals;
	
	public Frequency<String> stringsFrequency;
	public Frequency<String> opcodesFrequency;
	
	//loadsFrequency;
	//storesFrequency;
	
	public Statistics() {
		
		stringsFrequency = new Frequency<String>();
		opcodesFrequency = new Frequency<String>();
	}
		
	void updateSize(int size) {
		totalBytes = totalBytes + size;
	}
	
	void updateClasses(String name) {
		totalClasses++;
		updateStrings(name);
	}
	
	void updateMethods(String name) {
		totalMethods++;
		updateStrings(name);
	}
	
	void updateClassVariables(String name) {
		totalClassVars++;
		updateStrings(name);
	}
	
	void updateLocalVariables(String name) {
		totalLocalVars++;
		updateStrings(name);
	}

	void updateOpcodes(String opcode) {
		
		// update total
		totalInstructions++;
		
		// update map
		opcodesFrequency.increment(opcode);
	}

	void updateStrings(String str) {

		// update total size
		totalSizeOfStrings = totalSizeOfStrings + str.length();
		
		// update map
		stringsFrequency.increment(str);
	}

	
	public void print(PrintStream out) {
		
		int sum = totalClasses + totalMethods + totalClassVars + totalLocalVars;
		
		out.println(
			"--- STATISTICS ---\n"
			+ "Total size in bytes: \t"  
			+ totalBytes + "\n"
			
			+ "Number of classes: \t"
			+ totalClasses + "\n"
			
			+ "Number of methods: \t" 
			+ totalMethods + "\n"
			
			+ "Number of class variables: \t" 
			+ totalClassVars + "\n"
			
			+ "Number of local variables: \t" 
			+ totalLocalVars + "\n"
			
			+ "Number of opcodes: \t" 
			+ totalInstructions + "\n"
			
			+ "Total size of descriptive strings: \t" 
			+ totalSizeOfStrings + "\n"
			
			+ "Total number of descriptors: \t"  
			+ sum + "\n"
			
			+ "Methods per class: \t" 
			+ (totalMethods * 0.1 / totalClasses)  + "\n"
			
			+ "Class variables per class: \t" 
			+ (totalClassVars * 0.1 / totalClasses) + "\n"
			
			+ "Local variables per method: \t" 
			+ (totalLocalVars * 0.1 / totalMethods) + "\n"
			
			+ "Size of strings per descriptors: \t" 
			+ (totalSizeOfStrings * 0.1 / sum) + "\n"
						
			+ "Average max locals: \t" 
			+ (totalMaxLocals / totalMethods) + "\n"
						
			+ "Average max stack: \t" 
			+ (totalMaxStack / totalMethods) + "\n"
			
			+ "Max locals: \t" 
			+ (maxLocals) + "\n"
			
			+ "Max stack: \t" 
			+ (maxStack) + "\n"

		);			
					
		
		out.println("Most used descriptive strings:");
		printDescriptiveStrings(out);
		
		out.println("Most loaded variables:");
		out.println("Most stored variables:");
		
		out.println("Most used opcodes:");
		printOpcodes(out);
		
		
	}

	private void printOpcodes(PrintStream out) {
		int i = 0;
		int max = 100;
		
		// recalculate frequencies to percentils:
		
		Remapper<String, Integer, String, Double> remapper = new Remapper<String, Integer, String, Double>() {

			
			@Override
			public Double remapValue(String key1, Integer value1) {
				return value1 * 100.0 / totalInstructions;
			}

			@Override
			public String remapKey(String key1, Integer value1) {
				return key1;
			}
			
		};
		
		Map<String, Double> percentils = remapper.remap(opcodesFrequency, new HashMap<>());
		
		
		List<String> sortedKeys = MapValuesComparator.getSortedList(percentils);
		
		for(String key: sortedKeys) {
			if (i >= max) break;
			out.println(percentils.get(key) + "\t" + opcodesFrequency.get(key) + "\t" + key);
			i++;
		}
		
		
	}

	public void printDescriptiveStrings(PrintStream out) {
		
		int i = 0;
		int max = 20;
		int sum = totalClasses + totalMethods + totalClassVars + totalLocalVars;
		
		
		// recalculate frequencies to percentils:
		
		Remapper<String, Integer, String, Double> remapper = new Remapper<String, Integer, String, Double>() {

			
			@Override
			public Double remapValue(String key1, Integer value1) {
				return value1 * key1.length() * 100.0 / sum;
			}

			@Override
			public String remapKey(String key1, Integer value1) {
				return key1;
			}
			
		};
		
		Map<String, Double> percentils = remapper.remap(stringsFrequency, new HashMap<>());
		
		// sort by values
		List<String> sortedKeys = MapValuesComparator.getSortedList(percentils);
		
		// print statistics
		for(String key: sortedKeys) {
			
			if (i >= max) break;			
			out.println(percentils.get(key) + "\t" + stringsFrequency.get(key) + "\t" + key);
			i++;
		}
		
	}

	public void updateMaxs(int maxStack, int maxLocals) {
		
		// update total
		totalMaxStack = totalMaxStack + maxStack;
		totalMaxLocals = totalMaxLocals + maxLocals;
		
		// update max
		this.maxStack = (this.maxStack > maxStack) ? this.maxStack : maxStack;
		this.maxLocals = (this.maxLocals > maxLocals) ? this.maxLocals : maxLocals;
		
	}
}
