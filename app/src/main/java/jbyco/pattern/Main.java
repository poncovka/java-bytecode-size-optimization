package jbyco.pattern;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import jbyco.lib.MapValuesComparator;
import jbyco.pattern.graph.PrefixGraph;
import jbyco.pattern.graph.PrefixNode;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		int wildcard_threshold = 3;
		int pattern_threshold = 1;
		
		//String[] input = {"abc", "abc", "bdc", "abadcaaa"};
		String[] input = {"abc", "abc", "bdc", "badcaaa"};
		//String[] input = {"abc", "bac"};
		PrefixGraph graph = new PrefixGraph();
		
		int path = 0;
		PrefixNode node;
		
		// for every string
		for(String s:input) {
			
			// for every suffix
			for(int i=0; i < s.length(); i++) {
				
				path++;
				
				node = graph.getRoot();
				node.addPath(path);
				
				// insert every character
				for(int j=i; j < s.length(); j++) {
					
					// item
					char c = s.charAt(j);
				
					// put item to graph
					node = graph.addNextNode(node, c, path);
				}
			}
		}
		
		graph.print(System.out);
		
		PrintWriter in = new PrintWriter("../data/input.gml", "UTF-8");
		graph.printGml(in);
		in.close();
		
		PatternsFinder finder = new PatternsFinder();
		finder.minimize(graph.getRoot());
		
		graph.print(System.out);
		
		PrintWriter out = new PrintWriter("../data/output.gml", "UTF-8");
		graph.printGml(out);
		out.close();
		
		finder.simplify(graph.getRoot(), wildcard_threshold);
		
		graph.print(System.out);
		
		PrintWriter out2 = new PrintWriter("../data/simplyfied.gml", "UTF-8");
		graph.printGml(out2);
		out2.close();
		
		Map<String, Integer> patterns = finder.getPatterns(graph.getRoot(), "", pattern_threshold);
		List<String> keys = new MapValuesComparator<String, Integer>().getSortedKeys(patterns);
		
		for(String key:keys) {
			System.out.printf("%-15d%-15s\n", patterns.get(key), key);
		}
	}
}
