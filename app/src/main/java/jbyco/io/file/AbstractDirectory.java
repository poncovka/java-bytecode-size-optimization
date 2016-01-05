package jbyco.io.file;

import java.io.IOException;
import java.util.Iterator;

public interface AbstractDirectory extends AbstractFile, Iterator<AbstractFile> {
	
	public void open() throws IOException;
	public void close() throws IOException;
	
}
