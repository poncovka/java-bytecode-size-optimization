package jbyco.pattern;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		//String[] input = {"abc", "abc", "bdc", "abadcaaa"};
		String[] input = {"abc", "abc", "bdc", "badcaaa"};
		PrefixGraph graph = new PrefixGraph();
		
		int path = 0;
		PrefixNode node;
		
		for(String s:input) {
			
			node = graph.getRoot();
			path++;
			
			for(int i=0; i < s.length(); i++) {
				
				// item
				char c = s.charAt(i);
				
				// put item to graph
				node = graph.addNextNode(node, c, path);
			}
		}
		
		graph.print(System.out);
		
		PatternsFinder finder = new PatternsFinder();
		finder.minimize(graph.getRoot());
		
		graph.print(System.out);
		
		PrintWriter out = new PrintWriter("../data/output.gml", "UTF-8");
		graph.printGml(out);
		out.close();
	}
}
