package jbyco.analyze;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public interface Analyzer {
	
	public void processClassFile(InputStream in) throws IOException;
	public void writeResults(PrintWriter out);

}
