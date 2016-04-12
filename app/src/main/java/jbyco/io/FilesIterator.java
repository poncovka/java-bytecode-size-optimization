package jbyco.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Stack;

public class FilesIterator implements Iterator<CommonFile>, Iterable<CommonFile> {

	CommonFile next;
	Stack<DirectoryIterator> stack;
	
	public FilesIterator(Path path) throws IOException {
		
		// check the path
		if (!path.toFile().exists()) {
			throw new FileNotFoundException(path.toString());
		}
		
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
	public CommonFile next() {
		
		CommonFile file = next;
		
		try {
			
			next = findNextFile();
			
		} catch (IOException e) {
			e.printStackTrace();
			next = null;
		}
		
		return file;
	}	
	
	protected Path getAbstractRoot() throws IOException {
		return Paths.get("root");
	}
	
	protected Path getAbstractPath(String name, Path path) throws IOException {
		
		if (path == null) {
			path = getAbstractRoot();
		}
		
		return path.resolve(name);
	}
	
	protected CommonFile processFile (Path path, Path parentAbstractPath) throws IOException {
		
		// create file
		java.io.File file = path.toFile();
		String name = file.getName();
		
		// get abstract path
		Path abstractPath = getAbstractPath(name, parentAbstractPath);
		
		// return new file
		return new CommonFile(path, abstractPath);
	}
	
	protected void updateStack(CommonFile file) throws IOException {
		
		if (file != null && file.isDirectory()) {
			// push directory on the stack
			stack.push(new DirectoryIterator(file));
		}
	}
	
	protected CommonFile findNextFile() throws IOException {
				
		CommonFile file = null;

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
				dir.close();
				stack.pop();
			}
		}
		
		// return new file
		return file;
	}

	@Override
	public Iterator<CommonFile> iterator() {
		return this;
	}
}
