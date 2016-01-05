package jbyco.io.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Directory implements AbstractDirectory {

	Path path;
	Iterator<Path> iterator;
	DirectoryStream<Path> stream;
	AbstractFile nextFile;
	
	public Directory(Path path) {
		this.path = path;
	}
	
	@Override
	public void open() throws IOException {
		stream = Files.newDirectoryStream(path);
		iterator = stream.iterator();
	}	
	
	@Override
	public void close() throws IOException {
		stream.close();
	}

	@Override
	public boolean hasNext() {
		this.nextFile = search();
		return (nextFile != null);
	}

	@Override
	public AbstractFile next() {
		
		AbstractFile file = this.nextFile;
		this.nextFile = null;

		if (file == null) {
			throw new NoSuchElementException();
		}
		
		return file;
	}

	
	public AbstractFile search() {
		
		AbstractFile file = null;
		
		// search files in directory
		while (file == null && iterator.hasNext()) {
			
			Path path = iterator.next();
			file = getFile(path);
		}
		
		// nothing found
		return file;
	}
	
	static public AbstractFile getFile(Path path) {
				
		File file = path.toFile();
		String name = file.getName();
		
		// check file
		if (file.isFile()) {
			
			if(name.endsWith(".class")) {
				return new ClassFile(path);
			}
			else if (name.endsWith(".jar")) {
				return new JarDirectory(path);
			}
			else if (name.endsWith(".java")) {
				System.err.println("File " + path + " needs to be compiled.");
			}
			
		}
		// new directory
		else if (file.isDirectory()) {
			return new Directory(path);
		}
		
		return null;
	}

}
