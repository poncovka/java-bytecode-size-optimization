package jbyco.io.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Stack;

public class FilesIterator extends SearchIterator<BytecodeFile> {

	Stack<AbstractDirectory> stack;

	public FilesIterator(Path path) {
		stack = new Stack<AbstractDirectory>();
		init(path);
	}

	public void init(Path path) {
		AbstractFile dir = Directory.getFile(path);
		nextItem = processFile(dir);
	}

	public BytecodeFile search() {
		
		// search directories on the stack
		while (!stack.isEmpty()) {
			
			// look at the top directory
			AbstractDirectory dir = stack.peek();
			
			// get next file
			while (dir.hasNext()) {
				
				// process file
				BytecodeFile file = processFile(dir.next());
				
				// return bytecode file
				if (file != null) {
					return file;
				}
				else {
					dir = stack.peek();
				}
			}
			
			// directory was searched	
			// close the directory
			try {
				dir.close();
			} catch (IOException e) {
				processError(e);
			}
			
			// pop the directory
			stack.pop();	
		}
		
		// nothing found
		return null;
	}
	
	public BytecodeFile processFile(AbstractFile file) {
				
		// found bytecode file
		if(file instanceof BytecodeFile) {
			return (BytecodeFile) file;
		}
		// found directory
		else if (file instanceof AbstractDirectory) {
			AbstractDirectory nextdir = (AbstractDirectory) file; 
			
			try {				
				// try to open the directory
				nextdir.open();
				
				// push directory on stack
				stack.push(nextdir);

			} catch (IOException e) {
				processError(e);
			}
		}
		else {
			System.err.println("Unexpected instance of AbstractFile:" + file);
		}
		
		// found directory
		return null;
	}
	
	private void processError(Exception e) {
		e.printStackTrace();
	}
	
}
