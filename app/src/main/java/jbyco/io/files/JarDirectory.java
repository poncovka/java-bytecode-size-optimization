package jbyco.io.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class JarDirectory implements AbstractDirectory {

	Path path;
	JarFile jar;
	Iterator<JarEntry> iterator;
	Stream<JarEntry> stream;
	AbstractFile nextFile;
	
	public JarDirectory(Path path) {		
		this.path = path;
	}

	@Override
	public void open() throws IOException {
		jar = new JarFile(path.toFile());
		stream = jar.stream();
		iterator = stream.iterator();
	}

	@Override
	public void close() throws IOException {
		stream.close();
		jar.close();
	}	
	
	@Override
	public boolean hasNext() {
		nextFile = search();
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
			
			JarEntry entry = iterator.next();
			file = getFile(jar, entry);
		}
		
		// nothing found
		return file;
	}
	
	static public AbstractFile getFile(JarFile jar, JarEntry entry) {
		
		String name = entry.getName();
		
		// check files, skip directories
		if (!entry.isDirectory()) {

			if(name.endsWith(".class")) {
				return new JarClassFile(jar, entry);
			}
			else if (name.endsWith(".jar")) {
				//return new JarDirectory(path?);
				System.err.println("File " + entry.getName() + " was skipped.");
			}				
		}	
		
		// nothing found
		return null;
	}

}
