package jbyco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import jbyco.analyze.Analyzer;
import jbyco.analyze.locals.LocalsAnalyzer;
import jbyco.analyze.patterns.instr.label.AbstractLabelFactory;
import jbyco.analyze.patterns.instr.label.NumberedLabelFactory;
import jbyco.analyze.patterns.instr.operation.AbstractOperationFactory;
import jbyco.analyze.patterns.instr.operation.GeneralOperationFactory;
import jbyco.analyze.patterns.instr.operation.TypedOperationFactory;
import jbyco.analyze.patterns.instr.param.AbstractParameterFactory;
import jbyco.analyze.patterns.instr.param.FullParameterFactory;
import jbyco.analyze.patterns.instr.param.GeneralParameterFactory;
import jbyco.analyze.patterns.instr.param.NumberedParameterFactory;
import jbyco.analyze.size.SizeAnalyzer;
import jbyco.io.BytecodeFiles;
import jbyco.io.BytecodePrinter;
import jbyco.io.file.BytecodeFile;
import jbyco.analyze.patterns.PatternsAnalyzer;

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
		
		// process params
		JCommander commander = new JCommander(mainArgs);
		commander.setProgramName("jbyco");
		commander.addCommand("print", printArgs);
		commander.addCommand("analyze", analyzeArgs);
		
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
		
		// TODO run check!
		
		// run app
		switch(command) {
			case "print" 	: app.run(printArgs); break;
			case "analyze" 	: app.run(analyzeArgs); break;
			default			: commander.usage(); break;
		}
		
	}
	
	public void run(PrintArgs args) {
		System.out.println("Printing...");
		
		// create printer
		BytecodePrinter printer = new BytecodePrinter();
		
		// process all given paths
		for (String path : args.paths) {
			 
			// start to search files
			BytecodeFiles files = new BytecodeFiles(path);
			
			// iterate over class files
			for (BytecodeFile file:files) {
				
				// set file
				printer.setFile(file);
				
				// print default
				if (!args.isDefault()) {
					printer.printSummary();
				}
				else {				
					// print summary
					if (args.summary) {
						printer.printSummary();
					}
					
					// print constant pool
					if (args.pool) {
						printer.printConstantPool();
					}
					
					// print code
					if (args.code) {
						printer.printMethods();
					}
				}
			}	
		}
	}

	public void run(AnalyzeArgs args) {
		System.out.println("Analyzing...");
		
		Analyzer analyzer;
		
		if (args.size) {
			analyzer = new SizeAnalyzer();
		}
		else if (args.locals) {
			analyzer = new LocalsAnalyzer();
		}
		else if (args.patterns) {
			
			// init
			AbstractOperationFactory operations;
			AbstractParameterFactory parameters;
			AbstractLabelFactory labels;
			
			//operations = new GeneralOperationFactory();
			operations = new TypedOperationFactory();
			
			//parameters = new GeneralParameterFactory();
			//parameters = new NumberedParameterFactory();
			parameters = new FullParameterFactory();
			
			labels = new NumberedLabelFactory();
			
			analyzer = new PatternsAnalyzer(operations, parameters, labels);
		}
		else {
			// TODO default statistics
			return;
		}
		
		// process all given paths
		for (String path : args.paths) {
			 
			// start to search files
			BytecodeFiles files = new BytecodeFiles(path);
			
			// iterate over class files
			for (BytecodeFile file:files) {
				analyzer.processFile(file);
			}	
		}
		
		analyzer.print();
	}

}

@Parameters() 
class MainArgs {
	
	@Parameter(names = "--help", description = "Print this help message and quit.", help = true)
	public boolean help;
	
}

@Parameters(commandDescription = "Print the text representation of class files.")
class PrintArgs {

	@Parameter(description = "PATHS", required = true)
	public List<String> paths = new ArrayList<>();
	
	@Parameter(names = "--summary", description = "Print the summary.")
	public boolean summary = false;
			
	@Parameter(names = "--pool", description = "Print the constant pool.")
	public boolean pool = false;
	
	@Parameter(names = "--code", description = "Print the code.")
	public boolean code = false;
	
	public boolean isDefault() {
		return (summary || pool || code);
	}
	
}

@Parameters(commandDescription = "Analyze all class files.")
class AnalyzeArgs {
	
	@Parameter(description = "PATHS", required = true)
	public List<String> paths = new ArrayList<>();

	@Parameter(names = "--statistics", description = "Print statistics.")
	public boolean statistics = false;

	@Parameter(names = "--size", description = "Analyze the size.")
	public boolean size = false;
	
	@Parameter(names = "--locals", description = "Analyze the usage of local variables.")
	public boolean locals = false;
	
	@Parameter(names = "--patterns", description = "Analyze patterns in instruction sequencies.")
	public boolean patterns = false;
	
}
