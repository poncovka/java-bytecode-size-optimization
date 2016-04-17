package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public class TemporaryFiles {

	final static String DEFAULT_SUFFIX = "jbyco";
	
	static public Path createDirectory() throws IOException {
		return Files.createTempDirectory(DEFAULT_SUFFIX);
	}
	
	static public Path createDirectory(Path dir, String suffix) throws IOException {
		return Files.createTempDirectory(dir, suffix);
	}
	
	static public void cleanDirectory(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = deleteFiles(dir);
		
		// delete all directories except dir
		while (dirs.size() > 1) {
			dirs.pop().delete();
		}
	}
	
	static public void deleteDirectory(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = deleteFiles(dir);
		
		// delete directories
		while (!dirs.isEmpty()) {
			dirs.pop().delete();
		}
		
	}

	static protected Deque<CommonFile> deleteFiles(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = new ArrayDeque<>();
		
		// process files
		for (CommonFile file : (new FilesIterator(dir))) {
		
			// push directories on the stack
			if (file.isDirectory()) {
				dirs.push(file);
			}
			// delete files
			else {
				file.delete();
			}
		}
		
		return dirs;
	}
}
