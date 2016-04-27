package jbyco.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

/**
 * A tool for printing the content of the class files.
 */
public class BytecodePrinter {
	
	/** The flag for printing the code. */
	static boolean CODE 	= true;
	
	/** The flag for printing the debug information. */
	static boolean DEBUG 	= false;
	
	/** The flag for printing frames. */
	static boolean FRAMES 	= false;
	
	/** The flag for printing expanded frames. */
	static boolean EXPANDED = false;
	
	/** The flag for printing the constant pool. */
	static boolean POOL 	= false;
	
	/** The output. */
	PrintWriter out;
	
	/**
	 * Instantiates a new bytecode printer.
	 *
	 * @param out the out
	 */
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
	
	/**
	 * Prints the content of the input class file.
	 *
	 * @param in the input
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void print(InputStream in) throws IOException {
		
		// print only the byte code without the pool
		if (!POOL) {
			printFile(in);
		}
		
		// print the byte code and the pool
		else {
			
			// read input to array
			byte[] bytes = Utils.toByteArray(in);
			
			// print file
			in = new ByteArrayInputStream(bytes);
			printFile(in);
			in.close();
			
			// print new line
			out.println();
			
			// print pool
			in = new ByteArrayInputStream(bytes);
			printConstantPool(in);
			in.close();
		}
	}
	
	/**
	 * Prints the class file.
	 *
	 * @param in the input
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void printFile(InputStream in) throws IOException {
		
		// read input stream
		ClassReader reader = new ClassReader(in);
			
		// print bytecode
		ClassVisitor visitor = new TraceClassVisitor(out);  
			
		// start the visit
		reader.accept(visitor, getFlags());
		
	}
	
	/**
	 * Prints the constant pool.
	 *
	 * @param in the input
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void printConstantPool(InputStream in) throws IOException {
						
		// parse class file
		ClassParser parser = new ClassParser(in, null);
		JavaClass klass = parser.parse();
			
		// init writer
		ConstantPoolWriter writer = new ConstantPoolWriter(out);
			
		// print
		writer.write(klass);
			
	}
		

	///////////////////////////////////////////////////////////////// MAIN
	
	/**
	 * The command line options.
	 */
	enum Option implements AbstractOption {
		
		/** The option for printing the basic information. */
		PRINT_BASIC 	("Print withount code.",
						 "--basic"),
		
		/** The option for printing the debug information. */
		PRINT_DEBUG 	("Print debug information.",
						 "--debug"),
		
		/** The option for printing frames. */
		PRINT_FRAMES	("Print frames.",
						 "--frames"),
		
		/** The option for printing extended frames. */
		PRINT_EFRAMES 	("Print expanded frames.",
						 "--expanded-frames"),
		
		/** The option for printing the constant pool. */
		PRINT_POOL		("Print the constant pool.",
						 "--pool"),
		
		/** The option for printing help. */
		HELP			("Show this message.",
						 "-h", "--help");
		
		/** The description. */
		String description;
		
		/** The names. */
		String[] names;
		
		/**
		 * Instantiates a new option.
		 *
		 * @param description the description
		 * @param names the names
		 */
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
	
	/**
	 * Command line options.
	 */
	static class Options extends AbstractOptions {

		/* (non-Javadoc)
		 * @see jbyco.lib.AbstractOptions#all()
		 */
		@Override
		public AbstractOption[] all() {
			return Option.values();
		}
		
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		Options options = new Options();
		Collection<Path> paths = new ArrayList<>();
		
		// process options
		int index = 0;
		for (; index < args.length; index++) {
			
			String arg = args[index];
			Option option = (Option)options.getOption(arg);
			
			// files
			if (option == null) {
				
				while (index < args.length) {
					paths.add(Paths.get(args[index++]));
				}
				
				continue;
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
		
		// create temporary directory
		Path workingDirectory = TemporaryFiles.createDirectory();		
		
		try {
			// print files
			for(Path path : paths) {
				
				// print class files
				for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {
					
					InputStream in = file.getInputStream();
					printer.print(in);
					in.close();
				}
			}
		}
		finally {
			TemporaryFiles.deleteDirectory(workingDirectory);
		}
		
		// close output
		out.close();
	}
}
