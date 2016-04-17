package jbyco.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class FilesIterator implements Iterator<CommonFile>, Iterable<CommonFile> {

	CommonFile next;
	Deque<DirectoryIterator> stack;
	
	public FilesIterator() throws IOException {
		// nothing
	}
	
	public FilesIterator(Path path) throws IOException {
		init(path);
	}
	
	public void init(Path path) throws IOException {
		
		// check the path
		if (!path.toFile().exists()) {
			throw new FileNotFoundException(path.toString());
		}
		
		// init
		this.stack = new ArrayDeque<>();
				
		// find next general file
		this.next = processFile(path, null);
		updateStack(this.next);		
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
			
			// TODO clean stack and close everything
			
			e.printStackTrace();
			next = null;
		}
		
		return file;
	}	
	
	protected CommonFile processFile (Path path, CommonFile parent) throws IOException {
		return new CommonFile(path);
	}
	
	protected void updateStack(CommonFile file) throws IOException {

		// push directory on the stack
		if (file != null && file.isDirectory()) {
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
				file = processFile(path, dir.getFile());		
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
