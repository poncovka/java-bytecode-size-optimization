package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BytecodeFilesIterator extends FilesIterator {

	public BytecodeFilesIterator(Path path) {
		super(path);
		
		if (!checkFile(next)) {
			next = findNextFile();
		}
	}
	
	public boolean checkFile(FileAbstraction file) {
		return file.isClassFile();
	}
	
	@Override
	protected FileAbstraction findNextFile() {
		
		FileAbstraction file;
		
		while ((file = super.findNextFile()) != null) {
			if (checkFile(file)) {
				return file;
			}
		}
		
		return null;
	}
	
	@Override
	protected FileAbstraction processFile(Path path, Path parentAbstractPath) {
		
		// get general file
		FileAbstraction file = super.processFile(path, parentAbstractPath);
		String name = file.getName();
		
		// is jar file?
		if(file.isFile() && file.isJar()) {
					
			try {
				// create temporary file
				Path tmp = Files.createTempDirectory(name);
						
				// extract files
				JarExtractor.extract(path, tmp);
				
				// return recreated file
				return new FileAbstraction(tmp, file.getAbstractPath());
						
			} catch (IOException e) {
						e.printStackTrace();
			}
		}
		
		return file;
	}
	
}
