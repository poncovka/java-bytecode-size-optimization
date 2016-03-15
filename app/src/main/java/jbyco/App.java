package jbyco;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jbyco.analyze.locals.LocalsAnalyzer;
import jbyco.analyze.patterns.PatternsAnalyzer;
import jbyco.analyze.size.SizeAnalyzer;
import jbyco.analyze.statistics.StatisticsCollector;
import jbyco.lib.Utils;

public class App {

	enum Command {
		
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
		String[] args;
		
		private Command(String description, String ...args) {
			this.description = description;
			this.args = args;
		}
	}
		
	public static void main(String[] args) {
		
		// get map of commands
		Map<String, Command> map = getCommandMap();
		
		// process the first argument
		if (args.length > 0 && map.containsKey(args[0])) {
			
			// get command and remaining arguments
			String arg = args[0];
			String[] arguments = Arrays.copyOfRange(args, 1, args.length);
			
			Command command = map.get(arg);
			
			// run command
			switch(command) {
				case HELP:					help(); break;
				case PRINT:					break;
				case STATISTICS:			StatisticsCollector.main(arguments);break;
				case ANALYZE_LOCALS:		LocalsAnalyzer.main(arguments);break;
				case ANALYZE_PATTERNS:		PatternsAnalyzer.main(arguments);break;
				case ANALYZE_SIZE:			SizeAnalyzer.main(arguments); break;
			};
		}
		else {
			help();
			throw new IllegalArgumentException("Unkown argument in '" + Utils.arrayToString(args, " ") + "'.");
		}	
	}
	
	public static void help() {
		
		// print arguments and description
		for (Command command : Command.values()) {
			System.out.printf("%-30s %s\n", Utils.arrayToString(command.args, ", "), command.description);
		}
	}
	
	public static Map<String, Command> getCommandMap() {
		
		// create map arg -> command
		Map<String, Command> map = new HashMap<>();
		
		for (Command command : Command.values()) {
			for (String arg : command.args) {
				map.put(arg, command);
			}
		}
		
		return map;
	}
}
