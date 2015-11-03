package jbyco.stat;

import java.io.PrintStream;
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
	
	// frequency of descriptive strings
	public Frequency<String> stringsFrequency;
	// frequency of operational codes
	public Frequency<String> opcodesFrequency;
	// frequency of max stack sizes
	public Frequency<Integer> maxStackFrequency;
	// frequency of max local variables
	public Frequency<Integer> maxLocalsFrequency;
	
	//loadsFrequency;
	//storesFrequency;
	
	public Statistics() {
		
		stringsFrequency = new Frequency<String>();
		opcodesFrequency = new Frequency<String>();
		maxStackFrequency = new Frequency<Integer>();
		maxLocalsFrequency = new Frequency<Integer>();
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

	public void updateMaxs(int maxStack, int maxLocals) {
		
		// update total
		totalMaxStack = totalMaxStack + maxStack;
		totalMaxLocals = totalMaxLocals + maxLocals;
		
		// update max
		this.maxStack = (this.maxStack > maxStack) ? this.maxStack : maxStack;
		this.maxLocals = (this.maxLocals > maxLocals) ? this.maxLocals : maxLocals;
		
		// update frequencies
		this.maxLocalsFrequency.increment(maxLocals);
		this.maxStackFrequency.increment(maxStack);
		
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
					
		
		out.println("Max stack distribution:");
		printStackMaxs(out, maxStackFrequency, 30);
		
		out.println("Max locals distribution:");
		printLocalsMaxs(out, maxLocalsFrequency, 30);
		
		out.println("Most used opcodes:");
		printInstructions(out, opcodesFrequency, 20);
		
		out.println("Most used descriptive strings:");
		printDescStrings(out, stringsFrequency, 20);
		
		out.println("Most loaded variables:");
		out.println("Most stored variables:");

	}


	public Map<String, Double> getKeyLengthProportionMap(Frequency<String> map, int total) {
		
		Remapper<String, Integer, String, Double> remapper = 
			new Remapper<String, Integer, String, Double>() {

			@Override
			public Double remapValue(String key, Integer value) {
				return value * key.length() * 100.0 / total;
			}

			@Override
			public String remapKey(String key, Integer value) {
				return key;
			}
			
		};
		
		return remapper.remap(map);
		
	}
	
	public void printDescStrings(PrintStream out, Frequency<String> freq, int max) {
		
		int i = 0;
		
		// create proportion map
		Map<String, Double> prop = getKeyLengthProportionMap(freq, freq.getTotal());
		
		// create list of sorted keys
		List<String> keys = new MapValuesComparator<String, Double>().getSortedKeys(prop);
		
		// print
		printHeader(out, "[freq]", "[%]", "[string]");
		
		for(String key:keys) {
			
			if (i >= max) break;
			print(out, freq.get(key));
			print(out, prop.get(key));
			print(out, key);
			out.println();
			i++;
		}
		
	}

	public void printInstructions(PrintStream out, Frequency<String> freq, int max) {
		
		int i = 0;
		
		// create proportion map
		Map<String, Double> prop = freq.getProportionMap();
		
		// create list of sorted keys
		List<String> keys = new MapValuesComparator<String, Double>().getSortedKeys(prop);
		
		// print
		// print
		printHeader(out, "[freq]", "[%]", "[inst]");
		
		for(String key:keys) {
			
			if (i >= max) break;
			print(out, freq.get(key));
			print(out, prop.get(key));
			print(out, key);
			out.println();
			i++;
		}		
	}

	public void printStackMaxs(PrintStream out, Frequency<Integer> freq, int max) {
		
		int i = 0;
		
		// create proportion map
		Map<Integer, Double> prop = freq.getProportionMap();
		
		// create list of sorted keys
		List<Integer> keys = new MapValuesComparator<Integer, Double>().getSortedKeys(prop);
		
		// print
		printHeader(out, "[freq]", "[%]", "[stack size]");
		
		for(Integer key:keys) {
			
			if (i >= max) break;
			print(out, freq.get(key));
			print(out, prop.get(key));
			print(out, key);
			out.println();
			i++;
		}
		
	}
	
	public void printLocalsMaxs(PrintStream out, Frequency<Integer> freq, int max) {
		
		int i = 0;
		
		// create proportion map
		Map<Integer, Double> prop = freq.getProportionMap();
		
		// create list of sorted keys
		List<Integer> keys = new MapValuesComparator<Integer, Double>().getSortedKeys(prop);
		
		// print
		printHeader(out, "[freq]", "[%]", "[local vars]");
		
		for(Integer key:keys) {
			
			if (i >= max) break;
			print(out, freq.get(key));
			print(out, prop.get(key));
			print(out, key);
			out.println();
			i++;
		}	
	}

	public void print(PrintStream out, int i) {
		out.printf("%-15d", i);
	}

	public void print(PrintStream out, double d) {
		out.printf("%-15.1f", d);
	}

	public void print(PrintStream out, String s) {
		out.printf("%-15s", s);
	}
	
	public void printHeader(PrintStream out, String s1, String s2, String s3) {
		out.printf("%-15s", s1);
		out.printf("%-15s", s2);
		out.printf("%-15s", s3);
		out.println();
	}

}
