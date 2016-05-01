package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A library of functions for working with temporary files.
 */
public class TemporaryFiles {

	/** The default suffix of the main temporary directory. */
	final static String DEFAULT_SUFFIX = "jbyco";
	
	/**
	 * Creates the directory.
	 *
	 * @return the path to the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static public Path createDirectory() throws IOException {
		return Files.createTempDirectory(DEFAULT_SUFFIX);
	}
	
	/**
	 * Creates the directory.
	 *
	 * @param dir the path to the parent
	 * @param suffix the suffix
	 * @return the path to the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static public Path createDirectory(Path dir, String suffix) throws IOException {
		return Files.createTempDirectory(dir, suffix);
	}
	
	/**
	 * Clean the directory.
	 *
	 * @param dir the path to the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static public void cleanDirectory(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = deleteFiles(dir);
		
		// delete all directories except dir
		while (dirs.size() > 1) {
			dirs.pop().delete();
		}
	}
	
	/**
	 * Delete the directory.
	 *
	 * @param dir the path to the directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static public void deleteDirectory(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = deleteFiles(dir);
		
		// delete directories
		while (!dirs.isEmpty()) {
			dirs.pop().delete();
		}
		
	}

	/**
	 * Delete files.
	 *
	 * @param dir the path to the directory
	 * @return the collection of directories
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static protected Deque<CommonFile> deleteFiles(Path dir) throws IOException {
		
		// init stack of directories
		Deque<CommonFile> dirs = new ArrayDeque<>();
		
		// process files
		for (CommonFile file : (new CommonFilesIterator(dir))) {
		
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
