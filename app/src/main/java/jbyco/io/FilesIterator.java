package jbyco.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Stack;

public class FilesIterator implements Iterator<FileAbstraction>, Iterable<FileAbstraction> {

	FileAbstraction next;
	Stack<DirectoryIterator> stack;
	
	public FilesIterator(Path path) {
		
		// init
		this.stack = new Stack<>();
				
		// find next general file
		next = processFile(path, null);
		updateStack(next);
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public FileAbstraction next() {
				
		FileAbstraction file = next;
		next = findNextFile();
		
		return file;
	}	
	
	protected Path getAbstractRoot() {
		return Paths.get("root");
	}
	
	protected FileAbstraction processFile(Path path, Path parentAbstractPath) {
		
		// create file
		java.io.File file = path.toFile();
		String name = file.getName();
		
		// create real and abstract paths
		Path realPath = path;
		Path abstractPath = (parentAbstractPath != null) ? parentAbstractPath : getAbstractRoot();
		abstractPath = abstractPath.resolve(name);
		
		// return new file
		return new FileAbstraction(realPath, abstractPath);
	}
	
	protected void updateStack(FileAbstraction file) {
		
		try {
			if (file != null && file.isDirectory()) {
				
				// push directory on the stack
				stack.push(new DirectoryIterator(file));
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected FileAbstraction findNextFile() {
				
		FileAbstraction file = null;

		// get file in directories
		while (file == null && !stack.isEmpty()) {
			
			// get directory
			DirectoryIterator dir = stack.peek();
			
			// get file
			if (dir.hasNext()) {
				
				Path path = dir.next();
				file = processFile(path, dir.getFile().getAbstractPath());		
				updateStack(file);
				
			}
			// close and remove directory
			else {
				
				try {
					dir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				stack.pop();
			}
		}
		
		// return new file
		return file;
	}

	@Override
	public Iterator<FileAbstraction> iterator() {
		return this;
	}
}
