package jbyco;

import java.util.Arrays;

import jbyco.analyze.patterns.PatternsAnalyzer;
import jbyco.analyze.size.SizeAnalyzer;
import jbyco.analyze.statistics.StatisticsCollector;
import jbyco.analyze.variables.VariablesAnalyzer;
import jbyco.io.BytecodePrinter;
import jbyco.lib.AbstractOption;
import jbyco.lib.AbstractOptions;
import jbyco.lib.Utils;

public class App {
	
	enum Option implements AbstractOption {
		
		ANALYZE_SIZE 		("Print sizes of items in classfiles.",
							 "--analyze-size"),
		ANALYZE_LOCALS 		("Print usage of local variables and parameters.", 
							 "--analyze-locals"),
		ANALYZE_PATTERNS 	("Print frequent instruction sequencies.",
							 "--analyze-patterns" ),
		STATISTICS 			("Print statistics.", 
							 "--stats"),
		PRINT 				("Print the content of class files.", 
							 "--print"),
		HELP 				("Show this message.", 
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
		
		// get options and map of options
		Options options = new Options();
		
		// process the first argument
		if (args.length > 0 && options.isOption(args[0])) {
			
			// get option and remaining arguments
			Option option = (Option)options.getOption(args[0]);
			String[] arguments = Arrays.copyOfRange(args, 1, args.length);
			
			// run command
			switch(option) {
				case HELP:					options.help(); 
											return;
				case PRINT:					BytecodePrinter.main(arguments);
											break;
				case STATISTICS:			StatisticsCollector.main(arguments);
											break;
				case ANALYZE_LOCALS:		VariablesAnalyzer.main(arguments);
											break;
				case ANALYZE_PATTERNS:		PatternsAnalyzer.main(arguments);
											break;
				case ANALYZE_SIZE:			SizeAnalyzer.main(arguments); 
											break;
			};
		}
		else {
			options.help();
			throw new IllegalArgumentException("Unkown argument in '" + Utils.arrayToString(args, " ") + "'.");
		}	
	}
}
