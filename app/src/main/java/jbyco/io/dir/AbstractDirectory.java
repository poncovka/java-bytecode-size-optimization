package jbyco.io.dir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import jbyco.io.file.AbstractFile;

public interface AbstractDirectory extends AbstractFile, Iterator<AbstractFile> {
	
	public void open() throws IOException;
	public void close() throws IOException;
	
}
