package jbyco.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import jbyco.io.file.BytecodeFile;
import jbyco.io.file.FilesIterator;

public class BytecodeFiles implements Iterable<BytecodeFile>{

	Path path;
	
	public BytecodeFiles(String str) {
		this.path = Paths.get(str);
	}
	
	public BytecodeFiles(Path path) {
		this.path = path;
	}
	
	@Override
	public Iterator<BytecodeFile> iterator() {
		return new FilesIterator(path);
	}

}
