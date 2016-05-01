package jbyco.analyze.patterns.tree;

import java.io.PrintWriter;

/**
 * GML exporter.
 */
public class TreeExporter {
	
	/** The output. */
	PrintWriter out;
	
	/**
	 * Instantiates a new tree exporter.
	 *
	 * @param out the out
	 */
	public TreeExporter(PrintWriter out) {
		this.out = out;
	}
	
	/**
	 * Prints the document head.
	 */
	public void printDocumentHead() {
		out.print(
			"Creator \"jbyco\"\n" +
			"Version \"2.12\"\n"
		);
	}
	
	/**
	 * Prints the graph start.
	 */
	public void printGraphStart() {
		out.print(
			"graph\n" +
			"[\n" +
			"label \"G1\"\n" +
			"directed 1\n" +
			"hierarchic 1\n"
		);
	}
		
	/**
	 * Prints the node.
	 *
	 * @param id the id
	 * @param label the label
	 */
	public void printNode(int id, Object label) {
		out.printf(
			"node\n" +
			"[\n" +
			"  id %d\n" +
			"  label \"%s\"\n" +
			"]\n",
			id,
			label
		);
	}

	/**
	 * Prints the edge.
	 *
	 * @param source the source
	 * @param target the target
	 * @param label the label
	 */
	public void printEdge(int source, int target, Object label) {
		printEdge(source, target, label, 1.0);
	}
	
	/**
	 * Prints the edge.
	 *
	 * @param source the source
	 * @param target the target
	 * @param label the label
	 * @param weight the weight
	 */
	public void printEdge(int source, int target, Object label, double weight) {
		
		// check and corrent the value of weight
		if (weight > 1.0) {
			weight = 1.0;
		}
		else if (weight < 0.0) {
			weight = 0.0;
		}
		
		// calculate width and color
		int width = 1;//(int)(7 * weight);
		int color = 255 - (int)(255 * weight);
		
		// print
		out.printf(
			"edge\n" +
			"[\n" +
			"  source %d\n" +
			"  target %d\n" +
			"  label \"%s\"\n" +
			"  graphics\n" +
			"  [\n" +
			"    width %d\n" +
			"    fill \"#%02X%02X%02X\"\n" +
			"    targetArrow \"standard\"\n" +
			"  ]\n" +
			"]\n",
			source,
			target,
			label,
			width,
			color,
			color,
			color
		);
	}

	/**
	 * Prints the end.
	 */
	public void printGraphEnd() {
		out.print("]\n");
	}

}
