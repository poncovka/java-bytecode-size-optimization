package jbyco.stat;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;

public class Statistics {
	
	public int sizeInBytes;
	public int numOfClasses;
	public int numOfMethods;
	public int numOfClassVars;
	public int numOfLocalVars;
	public int numOfOpcodes;
	public int sizeOfStrings;
	
	public Frequency<String> stringsFrequency;
	public Frequency<Opcodes> opcodesFrequency;
	
	//loadsFrequency;
	//storesFrequency;
	
	public Statistics() {
		
		sizeInBytes = 0;
		numOfClasses = 0;
		numOfMethods = 0;
		numOfClassVars = 0;
		numOfLocalVars = 0;
		numOfOpcodes = 0;
		sizeOfStrings = 0;
		
		stringsFrequency = new Frequency<String>();
		opcodesFrequency = new Frequency<Opcodes>();
	}
		
	void updateSize(int size) {
		sizeInBytes = sizeInBytes + size;
	}
	
	void updateClasses(String name) {
		numOfClasses++;
		updateStrings(name);
	}
	
	void updateMethods(String name) {
		numOfMethods++;
		updateStrings(name);
	}
	
	void updateClassVariables(String name) {
		numOfClassVars++;
		updateStrings(name);
	}
	
	void updateLocalVariables(String name) {
		numOfLocalVars++;
		updateStrings(name);
	}

	void updateOpcodes(Opcodes opcode) {
		
		// update total
		numOfOpcodes++;
		
		// update map
		opcodesFrequency.increment(opcode);
	}

	void updateStrings(String str) {

		// update total size
		sizeOfStrings = sizeOfStrings + str.length();
		
		// update map
		stringsFrequency.increment(str);
	}

	
	public void print(PrintStream out) {
		
		int sum = numOfClasses + numOfMethods + numOfClassVars + numOfLocalVars;
		
		out.println(
			"--- STATISTICS ---\n"
			+ "Total size in bytes: \t"  
			+ sizeInBytes + "\n"
			
			+ "Number of classes: \t"
			+ numOfClasses + "\n"
			
			+ "Number of methods: \t" 
			+ numOfMethods + "\n"
			
			+ "Number of class variables: \t" 
			+ numOfClassVars + "\n"
			
			+ "Number of local variables: \t" 
			+ numOfLocalVars + "\n"
			
			+ "Number of opcodes: \t" 
			+ numOfOpcodes + "\n"
			
			+ "Total size of descriptive strings: \t" 
			+ sizeOfStrings + "\n"
			
			+ "Total number of descriptors: \t"  
			+ sum + "\n"
			
			+ "Methods per class: \t" 
			+ (numOfMethods * 0.1 / numOfClasses)  + "\n"
			
			+ "Class variables per class: \t" 
			+ (numOfClassVars * 0.1 / numOfClasses) + "\n"
			
			+ "Local variables per method: \t" 
			+ (numOfLocalVars * 0.1 / numOfMethods) + "\n"
			
			+ "Size of strings per descriptors: \t" 
			+ (sizeOfStrings * 0.1 / sum)
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
		int max = 20;
		
		List<Opcodes> sortedKeys = MapValuesComparator.getSortedList(opcodesFrequency);
		
		for(Opcodes key: sortedKeys) {
			if (i >= max) break;
			out.println(opcodesFrequency.get(key) + "\t" + key);
			i++;
		}
		
		
	}

	public void printDescriptiveStrings(PrintStream out) {
		
		int i = 0;
		int max = 20;
		int sum = numOfClasses + numOfMethods + numOfClassVars + numOfLocalVars;
		
		
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
}
