package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BytecodeFilesIterator extends FilesIterator {

	Path workingDirectory;
	
	public BytecodeFilesIterator() throws IOException {
		// nothing
	}
	
	public BytecodeFilesIterator(Path path, Path workingDirectory) throws IOException {
		super();
		init(path, workingDirectory);
	}

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
	
	public boolean checkFile(CommonFile file) throws IOException {
		return file.isClassFile();
	}
	
	protected Path getAbstractPath(String name, CommonFile parent) throws IOException {
		
		Path path;
		
		// parent is extracted file
		if (parent instanceof ExtractedFile) {
			path = ((ExtractedFile)parent).getAbstractedPath(); 
		}
		
		// get abstracted path from parent dirs
		else {
			path = Paths.get("");
			for (DirectoryIterator i : stack) {
				path = path.resolve(i.getFile().getName());
			}
		}
		
		return path.resolve(name);
	}
	
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
