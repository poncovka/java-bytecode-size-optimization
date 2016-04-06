package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;

public class BytecodePrinter {
	
	static boolean CODE 	= true;
	static boolean DEBUG 	= false;
	static boolean FRAMES 	= false;
	static boolean EXPANDED = false;
	static boolean POOL 	= false;
	
	PrintWriter out;
	
	public BytecodePrinter(PrintWriter out) {
		this.out = out;
	}
	
	static public int getFlags() {
		
		int flags = 0;
		
		if (!CODE) 			flags |= ClassReader.SKIP_CODE;
		if (!DEBUG)			flags |= ClassReader.SKIP_DEBUG;
		if (EXPANDED)		flags |= ClassReader.EXPAND_FRAMES;
		else if (!FRAMES)	flags |= ClassReader.SKIP_FRAMES;
		
		return flags;
	}
	
	public void setOutput(PrintWriter out) {
		this.out = out;
	}
	
	public void print(FileAbstraction file) {
		
		printFile(file);
		
		if (POOL) {
			out.println();
			printConstanPool(file);
		}
		
	}
	
	public void printFile(FileAbstraction file) {
		
		try {
			
			// get input stream
			InputStream in = file.getInputStream();
			
			// read input stream
			ClassReader reader = new ClassReader(in);
			
			// print bytecode
			ClassVisitor visitor = new TraceClassVisitor(out);  
			
			// start the visit
			reader.accept(visitor, getFlags());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void printConstanPool(FileAbstraction file) {
				
		try {		
			// get input stream
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, file.getName());
			JavaClass klass = parser.parse();
			
			// init writer
			ConstantPoolWriter writer = new ConstantPoolWriter(out);
			
			// print
			writer.write(klass);
			
		} catch (ClassFormatException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	///////////////////////////////////////////////////////////////// MAIN
	
	enum Option implements AbstractOption {
		
		PRINT_BASIC 	("Print withount code.",
						 "--basic"),
		PRINT_DEBUG 	("Print debug information.",
						 "--debug"),
		PRINT_FRAMES	("Print frames.",
						 "--frames"),
		PRINT_EFRAMES 	("Print expanded frames.",
						 "--expanded-frames"),
		PRINT_POOL		("Print the constant pool.",
						 "--pool"),
		HELP			("Show this message.",
						 "-h", "--help");
		
		String description;
		String[] names;
		
		private Option(String description, String ...names) {
			this.description = description;
			this.names = names;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public String[] getNames() {
			return this.names;
		}
	}
	
	static class Options extends AbstractOptions {

		@Override
		public AbstractOption[] all() {
			return Option.values();
		}
		
	}
	
	public static void main(String[] args) {
		
		Options options = new Options();
		
		// process options
		int i = 0;
		for (; i < args.length; i++) {
			
			String arg = args[i];
			Option option = (Option)options.getOption(arg);
			
			// files
			if (option == null) {
				break;
			}
			
			// process option
			switch(option) {
				case PRINT_POOL:	POOL = true;
									break;
				case PRINT_DEBUG:	DEBUG = true;
									break;
				case PRINT_FRAMES:	FRAMES = true;
									break;
				case PRINT_EFRAMES:	EXPANDED = true;
									break;
				case PRINT_BASIC:	CODE = false;
									DEBUG = false;
									FRAMES = false;
									EXPANDED = false;
									POOL = false;
									break;
				case HELP:			options.help();
									return;
			}
		}
		
		// init printer
		PrintWriter out = new PrintWriter(System.out);
		BytecodePrinter printer = new BytecodePrinter(out);
		
		// print files
		for(; i < args.length; i++) {
			
			// get path
			Path path = Paths.get(args[i]);
			
			// get files
			BytecodeFilesIterator files = new BytecodeFilesIterator(path);
			
			// process files
			for (FileAbstraction file : files) {
				
				// print
				//printer.print(file);
				//System.out.printf("%s   %s   %s\n", file.realPath, file.abstractPath, file.getName());
			}
			
		}
		
		out.close();
	}
}
