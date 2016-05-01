package jbyco.analyze;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * The interface for classes, that analyze the content of class files.
 */
public interface Analyzer {
	
	/**
	 * Process the class file.
	 *
	 * @param in 	the input stream of the class file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void processClassFile(InputStream in) throws IOException;
	
	/**
	 * Write results of the analysis.
	 *
	 * @param out 	the output stream, where the results are written
	 */
	public void writeResults(PrintWriter out);

}
