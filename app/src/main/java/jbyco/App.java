package jbyco;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import jbyco.io.Files;
import jbyco.io.file.BytecodeFile;
import jbyco.stat.ClassStatistic;
import jbyco.stat.Statistics;

// java jbyco print -h -c -f -m -a filename
// java jbyco help
// java jbyco analyze filename
// java jbyco patterns --generateGraph filename
// java jbyco patterns filename
// java jbyco patterns graphfilename

public class App {
	
	public static void main(String[] args) throws IOException {
		
		// create app
		App app = new App();
		
		// create commands
		MainArgs mainArgs = new MainArgs();
		PrintArgs printArgs = new PrintArgs();
		AnalyzeArgs analyzeArgs = new AnalyzeArgs();
		PatternsArgs patternsArgs = new PatternsArgs();
		
		// process params
		JCommander commander = new JCommander(mainArgs);
		commander.setProgramName("jbyco");
		commander.addCommand("print", printArgs);
		commander.addCommand("analyze", analyzeArgs);
		commander.addCommand("patterns", patternsArgs);
		
		try {
			commander.parse(args);
		}
		catch(ParameterException e) {
			System.err.println(e.getMessage());
			commander.usage();
			return;
		}
		
		// get command
		String command = commander.getParsedCommand();
		command = (command == null) ? "" : command;
		
		// run app
		switch(command) {
			case "print" 	: app.run(printArgs); break;
			case "analyze" 	: app.run(analyzeArgs); break;
			case "patterns" : app.run(patternsArgs); break;
			default			: commander.usage(); break;
		}
		
	}
	
	public void run(PrintArgs args) {
		System.out.println("Printing...");
	}

	public void run(AnalyzeArgs args) {
		System.out.println("Analyzing...");
	}
	
	public void run(PatternsArgs args) {
		System.out.println("Analyzing patterns...");
	}

	public void run() {
		
		
		/*
		
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
		*/

	}
}

@Parameters() 
class MainArgs {
	
	@Parameter(names = "--help", description = "Print this help message and quit.", help = true)
	private boolean help;
	
}

@Parameters(commandDescription = "Print the text representation of class files.")
class PrintArgs {

	@Parameter(description = "FILE", required = true)
	private List<String> filename = new ArrayList<>();
	
	@Parameter(names = "-h", description = "Print the header.")
	private boolean header = false;
			
	@Parameter(names = "-c", description = "Print the constant pool.")
	private boolean constants = false;
	
	@Parameter(names = "-f", description = "Print fields.")
	private boolean fields = false;
	
	@Parameter(names = "-m", description = "Print methods.")
	private boolean methods = false;
	
	@Parameter(names = "-a", description = "Print attributes.")
	private boolean attributes = false;

}

@Parameters(commandDescription = "Analyze all class files.")
class AnalyzeArgs {
	
	@Parameter(description = "FILE", required = true)
	private List<String> filename = new ArrayList<>();

	@Parameter(names = "--statistics", description = "Print statistics.")
	private boolean statistics = true;

	@Parameter(names = "--size", description = "Analyze the size.")
	private boolean size = true;
	
}

@Parameters(commandDescription = "Analyze byte code patterns in class files methods.")
class PatternsArgs {
	
	@Parameter(description = "FILE", required = true)
	private List<String> filename = new ArrayList<>();

	@Parameter(names = "--generate", description = "Generate a file with a suffix graph.")
	private boolean generateGraph = false;

	@Parameter(names = "--simplify", description = "Simplify the patterns.")
	private boolean simplify = false;
}
