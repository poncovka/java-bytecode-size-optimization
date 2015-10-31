package jbyco.io;

import java.nio.file.Path;
import java.util.Iterator;

import jbyco.io.file.BytecodeFile;

public class Files implements Iterable<BytecodeFile>{

	Path path;
	
	public Files(Path path) {
		this.path = path;
	}
	
	@Override
	public Iterator<BytecodeFile> iterator() {
		return new FilesIterator(path);
	}

}
