package jbyco;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import jbyco.io.Files;
import jbyco.io.file.BytecodeFile;
import jbyco.stat.ClassStatistic;
import jbyco.stat.Statistics;

public class App {

	public static void main(String[] args) throws IOException {
		
		// print arguments
		System.out.println("--- ARGUMENTS ---");
		
		for(String arg:args) {
			System.out.println(arg);
		}
		
		System.out.println();
		
		// process input directory
		if (args.length > 0) {
			
			Files files = new Files(args[0]);
			Statistics stat = new Statistics();
			
			// proces each bytecode file
			for(BytecodeFile file:files) {
				
				// print name of file
				//System.out.println(file);
				
				// open stream
				InputStream input = file.getInputStream();
				
				// open reader
				ClassReader reader = new ClassReader(input);
				
				// chain of visitors
				//PrintWriter writer = new PrintWriter(System.out);
				//ClassVisitor tracer = new TraceClassVisitor(writer);
				
				// compute statistic data
				ClassVisitor statistic = new ClassStatistic(stat);
				
				// run reader
				reader.accept(statistic, 0);
				
				// close stream
				input.close();
			}
			
			// pritn statistics
			stat.print(System.out);
			
		}

	}

}
