package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An iterator over all class files on a given path.
 * <p>
 * Directories are searched recursively. Jar files 
 * are extracted in a temporary directory and processed there.
 */
public class BytecodeFilesIterator extends CommonFilesIterator {

	/** The working directory. */
	Path workingDirectory;
	
	/**
	 * Instantiates a new bytecode files iterator.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BytecodeFilesIterator() throws IOException {
		// nothing
	}
	
	/**
	 * Instantiates a new bytecode files iterator.
	 *
	 * @param path the path
	 * @param workingDirectory the working directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BytecodeFilesIterator(Path path, Path workingDirectory) throws IOException {
		super();
		init(path, workingDirectory);
	}

	/**
	 * Initializes the iterator.
	 *
	 * @param path the path
	 * @param workingDirectory the working directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void init(Path path, Path workingDirectory) throws IOException {

		// set temporary directory
		this.workingDirectory = workingDirectory;
		
		// init files iterator
		super.init(path);		
		
		// find next file
		if (!checkFile(next)) {
			next = findNextFile();
		}

	}
	
	/**
	 * Check the file.
	 *
	 * @param file the file
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean checkFile(CommonFile file) throws IOException {
		return file.isClassFile();
	}
	
	/**
	 * Gets the abstract path to the file.
	 *
	 * @param name the name of file
	 * @param parent the parent file
	 * @return the abstract path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected Path getAbstractPath(String name, CommonFile parent) throws IOException {
		
		Path path;
		
		// parent is extracted file
		if (parent instanceof ExtractedFile) {
			path = ((ExtractedFile)parent).getAbstractedPath(); 
		}
		
		// get abstracted path from parent dirs
		else {
			path = Paths.get("");
			for (CommonDirectoryIterator i : stack) {
				path = path.resolve(i.getFile().getName());
			}
		}
		
		return path.resolve(name);
	}
	
	/* (non-Javadoc)
	 * @see jbyco.io.CommonFilesIterator#findNextFile()
	 */
	@Override
	protected CommonFile findNextFile() throws IOException {
		
		CommonFile file;
		
		while ((file = super.findNextFile()) != null) {
			if (checkFile(file)) {
				return file;
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see jbyco.io.CommonFilesIterator#processFile(java.nio.file.Path, jbyco.io.CommonFile)
	 */
	@Override
	protected CommonFile processFile(Path path, CommonFile parent) throws IOException {
		
		// get file name
		String name = path.toFile().getName();
		
		// get general file
		CommonFile file = super.processFile(path, parent);
		
		// is jar file?
		if(file.isFile() && file.isJar()) {
					
			// create temporary file
			Path tmp = Files.createTempDirectory(workingDirectory, name);
						
			// extract files
			JarExtractor.extract(path, tmp);
				
			// get abstract path
			Path abstracted = getAbstractPath(name, parent);
			
			// return recreated file
			file = new ExtractedFile(tmp, abstracted);				
		}
		
		// is parent extracted?
		else if (parent instanceof ExtractedFile) {
			
			// get abstract path
			Path abstracted = getAbstractPath(name, parent);
						
			// return recreated file
			file = new ExtractedFile(path, abstracted);
		}
		
		return file;
	}
	
}
