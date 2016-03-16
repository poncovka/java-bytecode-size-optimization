package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import jbyco.io.file.BytecodeFile;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;

public class BytecodePrinter {
	
	static boolean CODE 	= true;
	static boolean DEBUG 	= false;
	static boolean FRAMES 	= false;
	static boolean EXPANDED = false;
	static boolean POOL 	= false;
	
	static public int getFlags() {
		
		int flags = 0;
		
		if (!CODE) 			flags |= ClassReader.SKIP_CODE;
		if (!DEBUG)			flags |= ClassReader.SKIP_DEBUG;
		if (EXPANDED)		flags |= ClassReader.EXPAND_FRAMES;
		else if (!FRAMES)	flags |= ClassReader.SKIP_FRAMES;
		
		return flags;
	}
	
	public void print(BytecodeFile file) {
		
		printFile(file);
		
		if (POOL) {
			System.out.println();
			printConstanPool(file);
		}
		
	}
	
	public void printFile(BytecodeFile file) {
		
		try {
			
			// get input stream
			InputStream in = file.getInputStream();
			
			// read input stream
			ClassReader reader = new ClassReader(in);
			
			// print bytecode
			ClassVisitor visitor = new TraceClassVisitor(new PrintWriter(System.out));  
			
			// start the visit
			reader.accept(visitor, getFlags());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void printConstanPool(BytecodeFile file) {
				
		try {		
			// get input stream
			InputStream stream = file.getInputStream();
			
			// parse class file
			ClassParser parser = new ClassParser(stream, file.getName());
			JavaClass klass = parser.parse();
			
			// init printer
			ConstantPoolPrinter printer = new ConstantPoolPrinter();
			
			// print
			printer.print(klass);
			
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
		BytecodePrinter printer = new BytecodePrinter();
		
		// print files
		for(; i < args.length; i++) {
			
			// get files
			BytecodeFiles files = new BytecodeFiles(args[i]);
			
			// process files
			for (BytecodeFile file : files) {
				
				// print
				printer.print(file);
			}
			
		}
		
	}
}
